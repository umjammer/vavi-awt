/*
 * Copyright (c) 2017 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.binding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.logging.Level;
import javax.swing.JComponent;

import vavi.beans.BeanUtil;
import vavi.util.Debug;


/**
 * Components.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 Oct 29, 2017 umjammer initial version <br>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Components {

    /**  */
    Class<? extends Updater<?>> updater();

    /**  */
    class Util {

        private Util() {
        }

        private static Updater<?> updaterInstance;

        /**
         * @param bean an object annotated by @{@link Components}
         *             which contains {@link Updater} specified by {@link Components#updater()}
         * @return new instance
         */
        public static <T> Updater<T> getUpdater(Object bean) {
            Components components = bean.getClass().getAnnotation(Components.class);
            if (components == null) {
                throw new IllegalArgumentException("bean is not annotated with @Components");
            }
            try {
                if (updaterInstance != null) {
                    return (Updater<T>) updaterInstance;
                } else {
                    @SuppressWarnings("unchecked")
                    Updater<T> updater = (Updater<T>) components.updater().newInstance();
                    updaterInstance = updater;
                    return updater;
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }

        /**
         * TODO annotation for method
         *
         * @return only {@link Component} annotated fields
         * @throws IllegalArgumentException bean is not annotated with {@link Components}
         */
        public static List<Field> getComponentFields(Class<?> beanClass) {
            //
            Components components = beanClass.getAnnotation(Components.class);
            if (components == null) {
                throw new IllegalArgumentException("bean is not annotated with @Components");
            }

            // set annotated {@link Component} {@link Field}
            List<Field> componentFields = new ArrayList<>();
            for (Field field : beanClass.getDeclaredFields()) {
                Component component = field.getAnnotation(Component.class);
                if (component == null) {
                    continue;
                }
                componentFields.add(field);
            }

            return componentFields;
        }

        /** binders */
        private static ServiceLoader<Binder> binders = ServiceLoader.load(Binder.class);

        /** Sets all binders sabled or not */
        private static void setEnabled(boolean enabled) {
            binders.forEach(b -> b.setEnabled(enabled));
        }

        /** <class deal by the binder, the binder class> */
        private static Map<Class<?>, Binder<?>> binderMap = new HashMap<>();

        /** creates binders map */
        static {
            binders.forEach(binder -> {
                binderMap.put(binder.acceptable(), binder);
            });
        }

        /**
         * @param bean an object annotated by @{@link Components}
         *             which contains {@link Updater} specified by {@link Components#updater()}
         */
        private static <T> void setUpdater(T bean) {
            binders.forEach(binder -> {
                binder.setUpdater(getUpdater(bean));
            });
        }

        /**
         * bean -> swing
         * used for after 2nd time (mainly used for reset)
         *
         * @param bean   an object annotated by @{@link Components}
         *               which contains values for swing components annotated @{@link Component}.
         *               a definition class must be public.
         * @param swings an object
         *               which contains swing components connected to {@link Component#name()}
         */
        public static <T> void rebind(T bean, Object swings) {
            setEnabled(false);
            bind(bean, swings);
            setEnabled(true);
        }

        /**
         * used for 1st time
         *
         * @param bean   an object annotated by @{@link Components}
         *               which contains values for swing components annotated @{@link Component}.
         *               a definition class must be public.
         * @param swings an object
         *               which contains swing components connected to @{@link Component#name()}
         */
        public static <T> void bind(T bean, Object swings) {
            //
            Components propsEntity = bean.getClass().getAnnotation(Components.class);
            if (propsEntity == null) {
                throw new IllegalArgumentException("bean is not annotated with @Components");
            }

            //
            for (Field field : getComponentFields(bean.getClass())) {
                String swingFieldName = Component.Util.getName(field);
                Debug.println(Level.FINE, "field: " + swingFieldName);
                for (Field swingField : swings.getClass().getDeclaredFields()) {
                    if (swingField.getName().equals(swingFieldName)) {
                        Object swing = BeanUtil.getFieldValue(swingField, swings);
                        setUpdater(bean);
                        if (swing instanceof JComponent) {
                            Binder<T> binder = (Binder<T>) binderMap.get(swing.getClass());
Debug.println(Level.FINER, "binder: " + swing.getClass().getSimpleName() + ", " + binder.getClass().getSimpleName());
                            if (binder != null) {
                                binder.bind(bean, field, swing);
                            } else {
Debug.println(Level.WARNING, "field: " + swingFieldName + " (" + swing.getClass().getName() + ") is not implemented.");
                            }
                        } else {
Debug.println(Level.WARNING, "field: " + swingFieldName + " is not a sub class of JComponent.");
                        }
                    }
                }
            }
        }
    }
}

/* */
