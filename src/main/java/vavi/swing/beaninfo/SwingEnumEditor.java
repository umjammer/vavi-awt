/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.beaninfo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.FeatureDescriptor;
import java.util.logging.Level;

import javax.swing.BoxLayout;
import javax.swing.ComboBoxEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import vavi.util.Debug;


/**
 * A property editor for a swing enumerated type. Handles the case in which the
 * PropertyDescriptor has a value for "enumerationValues".
 * Note: the init() method must be called before the set/get methods can be
 * called.
 *
 * @author Mark Davidson
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version                   original version <br>
 *          0.01 020524 nsano be generic #init <br>
 *          0.01 020527 nsano be generic EnumeratedItem <br>
 */
public class SwingEnumEditor extends SwingEditorSupport {

    /** */
    private JComboBox<EnumeratedItem> combobox;

    public SwingEnumEditor() {
        combobox = new JComboBox<>();

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(combobox);
    }

    /** enum plus */
    public void setEditor(ComboBoxEditor editor) {
        if (editor != null) {
            combobox.setEditable(true);
            combobox.setEditor(editor);
        } else {
            combobox.setEditable(false);
        }
    }

//      public Object getEnumValue(String name) {
//          for (int i = 0; i < combobox.getItemCount(); ++i) {
//              EnumeratedItem item = (EnumeratedItem) combobox.getItemAt(i);
//              if (item.getName().equals(name)) {
//                  return item.getValue();
//              }
//          }
//          return null;
//      }

    /** */
    public void setValue(Object value) {
        super.setValue(value);

        // Set combo box if it's a new value. We want to reduce number
        // of extraneous events.
        Object selected = combobox.getSelectedItem();
        if (selected instanceof EnumeratedItem) {
            EnumeratedItem item = (EnumeratedItem) selected;
            if (value != null && !value.equals(item.getValue()))  {
                for (int i = 0; i < combobox.getItemCount(); ++i) {
                    item = combobox.getItemAt(i);
                    if (item.getValue().equals(value)) {
                        // XXX - hack! Combo box shouldn't call action event
                        // for setSelectedItem!!
                        combobox.removeActionListener(al);
                        combobox.setSelectedItem(item);
                        combobox.addActionListener(al);
                        return;
                    }
                }
            }
        } else {
            combobox.removeActionListener(al);
            combobox.setSelectedItem(selected);
            combobox.addActionListener(al);
        }
    }

    /**
     * Initializes this property editor with the enumerated items. Instances
     * can be shared but there are issues.
     * <p>
     * This method does a lot of jiggery pokery since enumerated
     * types are unlike any other homogenous types. Enumerated types may not
     * represent the same set of values.
     * <p>
     * One method would be to empty the list of values which would have the
     * side effect of firing notification events. Another method would be to
     * recreate the combobox.
     */
    public void init(FeatureDescriptor descriptor) {
        Object[] enumeration = (Object[]) descriptor.getValue("enumerationValues");
//          if (enum == null) {
//              enum = (Object[])descriptor.getValue("enumerationValuesPlus");
//          }

        if (enumeration != null) {
            // Remove action listener to reduce extra events.
            combobox.removeActionListener(al);
            combobox.removeAllItems();

            for (int i = 0; i < enumeration.length; i += 3) {
              combobox.addItem(new EnumeratedItem((String) enumeration[i], enumeration[i+1]));
//Debug.println(Level.FINER, enum[i + 2]); // et. "SwingConstants.LEADING"
            }

            combobox.addActionListener(al);
        }
    }

    /**
     * Event is set when a combo selection changes.
     */
    private ActionListener al = new ActionListener() {
        public void actionPerformed(ActionEvent evt)  {
            Object selected = combobox.getSelectedItem();
            if (selected instanceof EnumeratedItem) {
                EnumeratedItem item = (EnumeratedItem) selected;
                if (item != null && !getValue().equals(item.getValue()))  {
                    setValue(item.getValue());
                }
            } else {
                setValue(selected);
            }
        }
    };
}

/**
 * Object which holds an enumerated item plus its label.
 */
class EnumeratedItem  {
    private String name;
    private Object value;

    public EnumeratedItem(String name, Object value) {
        this.name = name;
        this.value = value;
Debug.println(Level.FINER, name + ", " + value);
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public String toString() {
        return name;
    }
}

/* */
