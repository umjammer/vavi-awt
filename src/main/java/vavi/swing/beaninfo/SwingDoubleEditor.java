/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.beaninfo;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.util.logging.Level;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import vavi.text.NumberDocument;
import vavi.util.Debug;


/**
 * An PropertyEdtitor for editing double.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020529 nsano initial version <br>
 */
public class SwingDoubleEditor extends SwingEditorSupport {

    private JTextField textfield;

    public SwingDoubleEditor() {
        textfield = new JTextField();
        textfield.setDocument(new NumberDocument());
        // XXX - testing
        textfield.addKeyListener(new KeyAdapter()  {
            public void keyPressed(KeyEvent evt)  {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER)  {
Debug.println(Level.FINE, textfield.getText());
                    setValue(new Double(textfield.getText()));
                }
            }
        });

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(textfield);
    }

    public void setValue(Object value)  {
        super.setValue(value);

        if (value != null)  {
            textfield.setText(value.toString());
        }
    }
}

/* */
