/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.beaninfo;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;


/**
 * An editor which represents a boolean value. This editor is implemented
 * as a checkbox with the text of the check box reflecting the state of the
 * checkbox.
 *
 * @author Mark Davidson
 * @version 1.10 990923 original version <br>
 */
public class SwingBooleanEditor extends SwingEditorSupport {

    private JCheckBox checkbox;

    /** */
    public SwingBooleanEditor() {
        checkbox = new JCheckBox();
        checkbox.addItemListener(il);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(checkbox);
    }

    /** */
    private ItemListener il = ev -> {
        if (ev.getStateChange() == ItemEvent.SELECTED) {
            setValue(true);
        } else {
            setValue(false);
        }
    };

    /** */
    public void setValue(Object value) {
        super.setValue(value);
        if (value != null) {
            try {
                checkbox.setText(value.toString());
                if (checkbox.isSelected() != (Boolean) value) {
                    // Don't call setSelected unless the state actually changes
                    // to avoid a loop.
                    checkbox.setSelected((Boolean) value);
                }
            } catch (Exception e) {
e.printStackTrace();
            }
        }
    }

    /** */
    public Object getValue() {
        return checkbox.isSelected();
    }
}

/* */
