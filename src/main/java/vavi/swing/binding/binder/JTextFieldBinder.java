/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.binding.binder;

import java.lang.reflect.Field;
import java.util.logging.Level;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import vavi.beans.BeanUtil;
import vavi.util.Debug;


/**
 * JTextFieldBinder.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-14 nsano initial version <br>
 */
public class JTextFieldBinder<T> extends BaseBinder<T> {

    @Override
    public Class<?> acceptable() {
        return JTextField.class;
    }

    @Override
    public void bind(T bean, Field field, Object swing) {
        JTextField textField = (JTextField) swing;
        textField.setText((String) BeanUtil.getFieldValue(field, bean));
        if (enabled) {
            textField.getDocument().addDocumentListener(new DocumentListener() {
                @Override public void changedUpdate(DocumentEvent e) {
                    fireUpdate(e);
                }
                @Override public void removeUpdate(DocumentEvent e) {
                    fireUpdate(e);
                }
                @Override public void insertUpdate(DocumentEvent e) {
                    fireUpdate(e);
                }

                void fireUpdate(DocumentEvent e) {
                    if (!enabled) {
                        return;
                    }
                    BeanUtil.setFieldValue(field, bean, textField.getText());
Debug.println(Level.FINE, "fired from: " + field.getName() + ", listener: " + this.hashCode() + ", " + e);
                    updater.update(bean);
                }
            });
Debug.println(Level.FINE, "swing listener added to: " + field.getName() + ", " + textField.hashCode());
        }
    }
}
