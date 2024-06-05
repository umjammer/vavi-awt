/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.beaninfo;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.FeatureDescriptor;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * A property editor for editing strings.
 *
 * @author Mark Davidson
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 1.20 991105       original version <br>
 *          1.21 020527 nsano enum'able <br>
 */
public class SwingStringEditor extends SwingEditorSupport {

    // Property editor to use if the Integer represents an Enumerated type.
    private SwingEnumEditor enumEditor = new SwingEnumEditor();

    private JTextField textfield;

    private boolean isEnumeration = false;

    public SwingStringEditor() {
        textfield = new JTextField();
        textfield.addKeyListener(new KeyAdapter()  {
            // XXX - JTextfield should send an actionPerformed event.
            // this was broken for 1.3 beta but fixed in 1.3. This
            // is the workaround.
            public void keyPressed(KeyEvent evt)  {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER)  {
                    setValue(textfield.getText());
                }
            }
        });

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(textfield);
    }

    public void setValue(Object value)  {
        if (isEnumeration)  {
            enumEditor.setValue(value);
        }
        else {
            super.setValue(value);
            if (value != null)  {
                textfield.setText(value.toString());
            } else {
                textfield.setText("");
            }
        }
    }

    public Object getValue() {
        if (isEnumeration)  {
            return enumEditor.getValue();
        } else {
            return super.getValue();
        }
    }

    /**
     * Must overload the PropertyChangeListener registration because
     * this class is the only interface to the SwingEnumEditor.
     */
    public void addPropertyChangeListener(PropertyChangeListener l)  {
        enumEditor.addPropertyChangeListener(l);
        super.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l)  {
        enumEditor.removePropertyChangeListener(l);
        super.removePropertyChangeListener(l);
    }

    /**
     * Initializes this property editor with the enumerated items.
     */
    public void init(FeatureDescriptor descriptor) {
        Object[] enumeration = (Object[])descriptor.getValue("enumerationValues");
        if (enumeration != null) {
            // The property descriptor describes an enumerated item.
            isEnumeration = true;

            enumEditor.init(descriptor);
        } else {
            // This is a string item
            isEnumeration = false;
        }
    }

    /**
     * Return the custom editor for the enumeration or the integer.
     */
    public Component getCustomEditor()  {
        if (isEnumeration)  {
            return enumEditor.getCustomEditor();
        } else {
            return super.getCustomEditor();
        }
    }
}
