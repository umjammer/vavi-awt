/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.binding.binder;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.lang.reflect.Field;
import javax.swing.JSlider;

import vavi.beans.BeanUtil;

import static java.lang.System.getLogger;


/**
 * JSliderBinder.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-14 nsano initial version <br>
 */
public class JSliderBinder<T> extends BaseBinder<T> {

    private static final Logger logger = getLogger(JSliderBinder.class.getName());

    @Override
    public Class<?> acceptable() {
        return JSlider.class;
    }

    @Override
    public void bind(T bean, Field field, Object swing) {
        JSlider slider = (JSlider) swing;
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
logger.log(Level.DEBUG, "fired from: " + field.getName() + ", listener: " + this.hashCode() + ", source: " + e.getSource().hashCode());
                updater.update(bean);
            });
logger.log(Level.DEBUG, "swing listener added to: " + field.getName() + ", " + slider.hashCode());
        }
    }
}
