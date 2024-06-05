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
 * An PropertyEditor for editing numbers.
 *
 * @author Mark Davidson
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version                   original version <br>
 *          0.01 020529 nsano rename <br>
 */
public class SwingFloatEditor extends SwingEditorSupport {

    private final JTextField textfield;

    public SwingFloatEditor() {
        textfield = new JTextField();
        textfield.setDocument(new NumberDocument());
        // XXX - testing
        textfield.addKeyListener(new KeyAdapter()  {
            @Override
            public void keyPressed(KeyEvent evt)  {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER)  {
Debug.println(Level.FINER, textfield.getText());
                    setValue(new Float(textfield.getText()));
                }
            }
        });

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(textfield);
    }

    @Override
    public void setValue(Object value)  {
        super.setValue(value);

        if (value != null)  {
            textfield.setText(value.toString());
        }
    }
}
