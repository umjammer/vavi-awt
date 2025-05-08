/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.beaninfo;

import java.awt.Component;
import java.beans.FeatureDescriptor;
import java.beans.PropertyChangeListener;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import static java.lang.System.getLogger;


/**
 * A property editor for editing integers. This editor also supports enumerated
 * type properties which are identified if the "enumerationValues" key returns a
 * non-null value. Note: the init() method must be called before the set/get
 * methods can be called.
 *
 * @author Mark Davidson
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 1.90 991111 original version <br>
 *          1.91 020524 nsano be generic <br>
 */
public class SwingIntegerEditor extends SwingEditorSupport {

    private static final Logger logger = getLogger(SwingIntegerEditor.class.getName());

    // Property editor to use if the Integer represents an Enumerated type.
    private final SwingEnumEditor enumEditor = new SwingEnumEditor();

    private final JSpinner intSpinner;

    private boolean isEnumeration = false;

    /** */
    public SwingIntegerEditor() {
        intSpinner = new JSpinner();

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(intSpinner);
    }

    @Override
    public void setValue(Object value) {
        if (isEnumeration) {
            enumEditor.setValue(value);
        } else {
            super.setValue(value);

            if (value != null) {
                intSpinner.setValue(value);
            }
        }
    }

    @Override
    public Object getValue() {
        if (isEnumeration) {
            return enumEditor.getValue();
        } else {
            return intSpinner.getValue();
        }
    }

    /**
     * Must overload the PropertyChangeListener registration because this class
     * is the only interface to the SwingEnumEditor.
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        enumEditor.addPropertyChangeListener(l);
        super.addPropertyChangeListener(l);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        enumEditor.removePropertyChangeListener(l);
        super.removePropertyChangeListener(l);
    }

    /**
     * Initializes this property editor with the enumerated items.
     */
    @Override
    public void init(FeatureDescriptor descriptor) {
        Object[] enumeration = (Object[]) descriptor.getValue("enumerationValues");
//        Object[] enumplus = (Object[])descriptor.getValue("enumerationValuesPlus");
        if (enumeration != null) {
            // The property descriptor describes an enumerated item.
            isEnumeration = true;

            enumEditor.init(descriptor);
            enumEditor.setEditor(null);
logger.log(Level.TRACE, "enum");
//        } else if (enumplus != null) {
            // enhanced enum

//            isEnumeration = true;

//            enumEditor.init(descriptor);
//            enumEditor.setEditor(ce);
//Debug.println("plus");
        } else {
            // This is an integer item
            isEnumeration = false;
            enumEditor.setEditor(null);
logger.log(Level.TRACE, "normal");
        }
    }

//    /** */
//    private ComboBoxEditor ce = new BasicComboBoxEditor() {
//        public Component getEditorComponent() {
//            return intSpinner;
//        }
//
//        public void setItem(Object item) {
//            if (item instanceof EnumeratedItem) {
//                intSpinner.setValue(((EnumeratedItem) item).getValue());
//            } else if (item instanceof Integer) {
//                intSpinner.setValue(item);
//            }
//        }
//
//        public Object getItem() {
//            return intSpinner.getValue();
//        }
//    };

//    /** */
//    private ChangeListener cl = new ChangeListener() {
//        public void stateChanged(ChangeEvent ev) {
//            SwingIntegerEditor.super.setValue(intSpinner.getValue());
//        }
//    };

//    /** */
//    private PropertyChangeListener al = new PropertyChangeListener() {
//        public void propertyChange(PropertyChangeEvent ev) {
//            SwingIntegerEditor.super.setValue(intSpinner.getValue());
//        }
//    };

    /**
     * Return the custom editor for the enumeration or the integer.
     */
    @Override
    public Component getCustomEditor() {
        if (isEnumeration) {
            return enumEditor.getCustomEditor();
        } else {
            return super.getCustomEditor();
        }
    }
}
