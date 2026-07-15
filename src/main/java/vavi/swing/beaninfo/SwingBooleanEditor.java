/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.beaninfo;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;


/**
 * An editor which represents a boolean value. This editor is implemented
 * as a checkbox with the text of the checkbox reflecting the state of the
 * checkbox.
 *
 * @author Mark Davidson
 * @version 1.10 990923 original version <br>
 */
public class SwingBooleanEditor extends SwingEditorSupport {

    private static final Logger logger = System.getLogger(SwingBooleanEditor.class.getName());

    private final JCheckBox checkbox;

    /** */
    public SwingBooleanEditor() {
        checkbox = new JCheckBox();
        ItemListener il = ev -> setValue(ev.getStateChange() == ItemEvent.SELECTED);
        checkbox.addItemListener(il);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(checkbox);
    }

    @Override
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
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
    }

    @Override
    public Object getValue() {
        return checkbox.isSelected();
    }
}
