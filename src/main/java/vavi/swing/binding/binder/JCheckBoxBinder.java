/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.binding.binder;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.lang.reflect.Field;
import javax.swing.JCheckBox;

import vavi.beans.BeanUtil;

import static java.lang.System.getLogger;


/**
 * JCheckBoxBinder.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-14 nsano initial version <br>
 */
public class JCheckBoxBinder<T> extends BaseBinder<T> {

    private static final Logger logger = getLogger(JCheckBoxBinder.class.getName());

    @Override
    public Class<?> acceptable() {
        return JCheckBox.class;
    }

    @Override
    public void bind(T bean, Field field, Object swing) {
        JCheckBox checkBox = (JCheckBox) swing;
        checkBox.setSelected((boolean) BeanUtil.getFieldValue(field, bean));
        if (enabled) {
            checkBox.addItemListener(e -> {
                if (!enabled) {
                    return;
                }
                BeanUtil.setFieldValue(field, bean, checkBox.isSelected());
logger.log(Level.DEBUG, "fired from: " + field.getName() + ", listener: " + this.hashCode() + ", source: " + e.getSource().hashCode());
                updater.update(bean);
            });
logger.log(Level.DEBUG, "swing listener added to: " + field.getName() + ", " + checkBox.hashCode());
        }
    }
}
