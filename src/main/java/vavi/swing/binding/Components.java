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
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.JTextField;

import vavi.beans.BeanUtil;
import vavi.util.Debug;
import vavi.util.StringUtil;


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

        private static Logger logger = Logger.getLogger(Util.class.getName());

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
         * TODO annotation for method
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

        // TODO not thread safe
        private static boolean enabled = true;

        private static void setEnabled(boolean enabled) {
            Util.enabled = enabled;
        }

        public static <T> void rebind(T bean, Object swings) {
            setEnabled(false);
            bind(bean, swings);
            setEnabled(true);
        }

        public static <T> void bind(T bean, Object swings) {
            //
            Components propsEntity = bean.getClass().getAnnotation(Components.class);
            if (propsEntity == null) {
                throw new IllegalArgumentException("bean is not annotated with @Components");
            }

            Updater<T> updater = getUpdater(bean);

            //
            for (Field field : getComponentFields(bean.getClass())) {
                String name = Component.Util.getName(field);
logger.fine("field: " + name);
                for (Field swingField : swings.getClass().getDeclaredFields()) {
                    if (swingField.getName().equals(name)) {
                        Object fieldValue = BeanUtil.getFieldValue(swingField, swings);
                        if (JComponent.class.isInstance(fieldValue)) {
                            if (JCheckBox.class.isInstance(fieldValue)) {
                                JCheckBox checkBox = JCheckBox.class.cast(fieldValue);
                                checkBox.setSelected((boolean) BeanUtil.getFieldValue(field, bean));
                                if (enabled) {
                                    checkBox.addActionListener(e -> {
                                        if (!enabled) {
                                            return;
                                        }
                                        BeanUtil.setFieldValue(field, bean, checkBox.isSelected());
                                        updater.update(bean);
                                    });
                                }
                            } else if (JSlider.class.isInstance(fieldValue)) {
                                JSlider slider = JSlider.class.cast(fieldValue);
                                slider.setValue((int) BeanUtil.getFieldValue(field, bean));
                                if (enabled) {
                                    slider.addChangeListener(e -> {
                                        if (!enabled) {
                                            return;
                                        }
                                        JSlider source = (JSlider) e.getSource();
                                        if (source.getValueIsAdjusting()) {
                                            return;
                                        }
                                        BeanUtil.setFieldValue(field, bean, slider.getValue());
                                        updater.update(bean);
                                    });
                                }
                            } else if (JTextField.class.isInstance(fieldValue)) {
                                JTextField textField = JTextField.class.cast(fieldValue);
                                textField.setText((String) BeanUtil.getFieldValue(field, bean));
                                if (enabled) {
                                    textField.addActionListener(e -> {
                                        if (!enabled) {
                                            return;
                                        }
                                        BeanUtil.setFieldValue(field, bean, textField.getText());
Debug.println(StringUtil.paramString(bean));
                                        updater.update(bean);
                                    });
                                }
                            } else {
                                logger.warning("field: " + name + " (" + fieldValue.getClass().getName() + ") is not implemented.");
                            }
                        } else {
                            logger.warning("field: " + name + " is not a sub class of JComponent.");
                        }
                    }
                }
            }
        }
    }
}

/* */
