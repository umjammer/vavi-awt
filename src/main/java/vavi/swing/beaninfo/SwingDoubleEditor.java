/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.beaninfo;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

import vavi.text.NumberDocument;

import static java.lang.System.getLogger;


/**
 * An PropertyEditor for editing double.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020529 nsano initial version <br>
 */
public class SwingDoubleEditor extends SwingEditorSupport {

    private static final Logger logger = getLogger(SwingDoubleEditor.class.getName());

    private final JTextField textField;

    public SwingDoubleEditor() {
        textField = new JTextField();
        textField.setDocument(new NumberDocument());
        // XXX - testing
        textField.addKeyListener(new KeyAdapter()  {
            @Override
            public void keyPressed(KeyEvent evt)  {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER)  {
logger.log(Level.DEBUG, textField.getText());
                    setValue(Double.parseDouble(textField.getText()));
                }
            }
        });

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(textField);
    }

    @Override
    public void setValue(Object value)  {
        super.setValue(value);

        if (value != null)  {
            textField.setText(value.toString());
        }
    }
}
