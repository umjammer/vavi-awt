/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.beaninfo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.FeatureDescriptor;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import javax.swing.BoxLayout;
import javax.swing.ComboBoxEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import static java.lang.System.getLogger;


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

    private static final Logger logger = getLogger(SwingEnumEditor.class.getName());

    /** */
    private final JComboBox<EnumeratedItem> comboBox;

    public SwingEnumEditor() {
        comboBox = new JComboBox<>();

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(comboBox);
    }

    /** enum plus */
    public void setEditor(ComboBoxEditor editor) {
        if (editor != null) {
            comboBox.setEditable(true);
            comboBox.setEditor(editor);
        } else {
            comboBox.setEditable(false);
        }
    }

//      public Object getEnumValue(String name) {
//          for (int i = 0; i < comboBox.getItemCount(); ++i) {
//              EnumeratedItem item = (EnumeratedItem) comboBox.getItemAt(i);
//              if (item.getName().equals(name)) {
//                  return item.getValue();
//              }
//          }
//          return null;
//      }

    @Override
    public void setValue(Object value) {
        super.setValue(value);

        // Set combo box if it's a new value. We want to reduce number
        // of extraneous events.
        Object selected = comboBox.getSelectedItem();
        if (selected instanceof EnumeratedItem item) {
            if (value != null && !value.equals(item.getValue()))  {
                for (int i = 0; i < comboBox.getItemCount(); ++i) {
                    item = comboBox.getItemAt(i);
                    if (item.getValue().equals(value)) {
                        // XXX - hack! Combo box shouldn't call action event
                        // for setSelectedItem!!
                        comboBox.removeActionListener(al);
                        comboBox.setSelectedItem(item);
                        comboBox.addActionListener(al);
                        return;
                    }
                }
            }
        } else {
            comboBox.removeActionListener(al);
            comboBox.setSelectedItem(selected);
            comboBox.addActionListener(al);
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
     * recreate the comboBox.
     */
    @Override
    public void init(FeatureDescriptor descriptor) {
        Object[] enumeration = (Object[]) descriptor.getValue("enumerationValues");
//          if (enum == null) {
//              enum = (Object[])descriptor.getValue("enumerationValuesPlus");
//          }

        if (enumeration != null) {
            // Remove action listener to reduce extra events.
            comboBox.removeActionListener(al);
            comboBox.removeAllItems();

            for (int i = 0; i < enumeration.length; i += 3) {
              comboBox.addItem(new EnumeratedItem((String) enumeration[i], enumeration[i+1]));
//logger.log(Level.TRACE, enum[i + 2]); // et. "SwingConstants.LEADING"
            }

            comboBox.addActionListener(al);
        }
    }

    /**
     * Event is set when a combo selection changes.
     */
    private final ActionListener al = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent evt)  {
            Object selected = comboBox.getSelectedItem();
            if (selected instanceof EnumeratedItem item) {
                if (item != null && !getValue().equals(item.getValue()))  {
                    setValue(item.getValue());
                }
            } else {
                setValue(selected);
            }
        }
    };

    /**
     * Object which holds an enumerated item plus its label.
     */
    private static class EnumeratedItem  {

        private final String name;
        private final Object value;

        public EnumeratedItem(String name, Object value) {
            this.name = name;
            this.value = value;
logger.log(Level.TRACE, name + ", " + value);
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
}
