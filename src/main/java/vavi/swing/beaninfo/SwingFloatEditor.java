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
 * An PropertyEditor for editing numbers.
 *
 * @author Mark Davidson
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version                   original version <br>
 *          0.01 020529 nsano rename <br>
 */
public class SwingFloatEditor extends SwingEditorSupport {

    private static final Logger logger = getLogger(SwingFloatEditor.class.getName());

    private final JTextField textField;

    public SwingFloatEditor() {
        textField = new JTextField();
        textField.setDocument(new NumberDocument());
        // XXX - testing
        textField.addKeyListener(new KeyAdapter()  {
            @Override
            public void keyPressed(KeyEvent evt)  {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER)  {
logger.log(Level.TRACE, textField.getText());
                    setValue(Float.parseFloat(textField.getText()));
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
