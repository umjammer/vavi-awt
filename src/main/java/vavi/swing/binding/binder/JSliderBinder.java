/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.binding.binder;

import java.lang.reflect.Field;
import java.util.logging.Level;
import javax.swing.JSlider;

import vavi.beans.BeanUtil;
import vavi.util.Debug;


/**
 * JSliderBinder.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-14 nsano initial version <br>
 */
public class JSliderBinder<T> extends BaseBinder<T> {

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
Debug.println(Level.FINE, "fired from: " + field.getName() + ", listener: " + this.hashCode() + ", source: " + e.getSource().hashCode());
                updater.update(bean);
            });
Debug.println(Level.FINE, "swing listener added to: " + field.getName() + ", " + slider.hashCode());
        }
    }
}
