/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.binding.binder;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Consumer;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import vavi.beans.BeanUtil;
import vavi.swing.binding.Component;

import static java.lang.System.getLogger;


/**
 * JTextFieldBinder.
 * <pre>
 * {@link Component#args()}:
 *   {@link #DETECT_ENTER}: fire event for enter key only
 *   else: fire event for every key
 * </pre>
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-14 nsano initial version <br>
 */
public class JTextFieldBinder<T> extends BaseBinder<T> {

    private static final Logger logger = getLogger(JTextFieldBinder.class.getName());

    public static final String DETECT_ENTER = "JTextFieldBinder_DETECT_ENTER";

    @Override
    public Class<?> acceptable() {
        return JTextField.class;
    }

    @Override
    public void bind(T bean, Field field, Object swing) {
        JTextField textField = (JTextField) swing;
        textField.setText((String) BeanUtil.getFieldValue(field, bean));
logger.log(Level.DEBUG, "text: " + textField.getText());
        Consumer<Object> fireUpdate = e -> {
            if (!enabled) {
                return;
            }
            BeanUtil.setFieldValue(field, bean, textField.getText());
logger.log(Level.DEBUG, "fired from: " + field.getName() + ", listener: " + this.hashCode() + ", " + e);
            updater.update(bean);
        };
        if (enabled) {
            if (Arrays.asList(Component.Util.getArgs(field)).contains(DETECT_ENTER)) {
                textField.addKeyListener(new KeyAdapter() {
                    @Override public void keyReleased(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            fireUpdate.accept(e);
                        }
                    }
                });
logger.log(Level.DEBUG, "swing key listener added to: " + field.getName() + ", " + textField.hashCode());
            } else {
                textField.getDocument().addDocumentListener(new DocumentListener() {
                    @Override public void changedUpdate(DocumentEvent e) {
                        fireUpdate.accept(e);
                    }
                    @Override public void removeUpdate(DocumentEvent e) {
                        fireUpdate.accept(e);
                    }
                    @Override public void insertUpdate(DocumentEvent e) {
                        fireUpdate.accept(e);
                    }
                });
logger.log(Level.DEBUG, "swing document listener added to: " + field.getName() + ", " + textField.hashCode());
            }
        }
    }
}
