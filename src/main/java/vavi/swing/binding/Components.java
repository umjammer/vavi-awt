/*
 * Copyright (c) 2017 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.binding;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JSlider;

import vavi.beans.BeanUtil;


/**
 * Components.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 Oct 29, 2017 umjammer initial version <br>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Components {

    /** */
    Class<? extends Updater<?>> updater();

    /** */
    class Util {

        private Util() {
        }

        /** */
        public static <T> Updater<T> getUpdater(Object bean) {
            Components components = bean.getClass().getAnnotation(Components.class);
            if (components == null) {
                throw new IllegalArgumentException("bean is not annotated with @Components");
            }
            try {
                @SuppressWarnings("unchecked")
                Updater<T> updater = (Updater<T>) components.updater().newInstance();
                return updater;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }

        /**
         * TODO メソッドにアノテーションされた場合
         * @return only {@link Component} annotated fields
         * @throws IllegalArgumentException bean is not annotated with {@link Components}
         */
        public static List<Field> getComponentFields(Class<?> beanClass) {
            //
            Components components = beanClass.getAnnotation(Components.class);
            if (components == null) {
                throw new IllegalArgumentException("bean is not annotated with @Components");
            }

            // {@link Component} でアノテートされた {@link Field} のセット
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

        public static <T> void bind(T bean, Object swings) throws IOException {
            //
            Components propsEntity = bean.getClass().getAnnotation(Components.class);
            if (propsEntity == null) {
                throw new IllegalArgumentException("bean is not annotated with @Components");
            }

            Updater<T> updater = getUpdater(bean);

            //
            for (Field field : getComponentFields(bean.getClass())) {
                String name = Component.Util.getName(field);
System.err.println("field: " + name);
                for (Field swingField : swings.getClass().getDeclaredFields()) {
                    if (swingField.getName().equals(name)) {
                        Object fieldValue = BeanUtil.getFieldValue(swingField, swings);
                        if (JComponent.class.isInstance(fieldValue)) {
                            if (JCheckBox.class.isInstance(fieldValue)) {
                                JCheckBox checkBox = JCheckBox.class.cast(fieldValue);
                                checkBox.setSelected((boolean) BeanUtil.getFieldValue(field, bean));
                                checkBox.addActionListener(e -> {
                                    BeanUtil.setFieldValue(field, bean, checkBox.isSelected());
                                    updater.update(bean);
                                });
                            } else if (JSlider.class.isInstance(fieldValue)) {
                                JSlider slider = JSlider.class.cast(fieldValue);
                                slider.setValue((int) BeanUtil.getFieldValue(field, bean));
                                slider.addChangeListener(e -> {
                                    JSlider source = (JSlider) e.getSource();
                                    if (source.getValueIsAdjusting()) {
                                        return;
                                    }
                                    BeanUtil.setFieldValue(field, bean, slider.getValue());
                                    updater.update(bean);
                                });
                            }
                        } else {
                            System.err.println("field: " + name + " is not a sub class of JComponent.");
                        }
                    }
                }
            }
        }
    }
}

/* */
