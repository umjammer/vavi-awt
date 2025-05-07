/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt;

import java.awt.CheckboxMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.Vector;

import vavi.awt.event.ActionSupport;


/**
 * A group of CheckBoxMenuItems.
 *
 * @target 1.1
 * @caution AWT only
 * @thanks Yuichi Nakajima
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020502 nsano initial version <br>
 *          0.01 020914 nsano add #setSelectedIndex <br>
 */
public class CheckboxMenuItemGroup {

    /** */
    private final Vector<CheckboxMenuItem> group = new Vector<>();

    /** */
    public CheckboxMenuItemGroup() {
        addActionListener(actionListener);
    }

    /** */
    public void add(CheckboxMenuItem menuItem) {
        group.addElement(menuItem);
        menuItem.addItemListener(itemListener);
        if (menuItem.getState())
            selectedIndex = group.indexOf(menuItem);
    }

    /** */
    private int selectedIndex = -1;

    /** */
    public void setSelectedIndex(int index) {
        this.selectedIndex = index;

        for (int i = 0; i < group.size(); i++) {
            group.elementAt(i).setState(i == index);
        }

        CheckboxMenuItem source = group.elementAt(index);
        int id = ActionEvent.ACTION_PERFORMED;
        String command = source.getActionCommand();
        fireActionPerformed(new ActionEvent(source, id, command));
    }

    /** */
    private final ItemListener itemListener = ev -> {
        CheckboxMenuItem source = (CheckboxMenuItem) ev.getSource();
        int id = ev.getID();
        String command = source.getActionCommand();
        fireActionPerformed(new ActionEvent(source, id, command));
    };

    /** */
    private final ActionListener actionListener = ev -> {
        int index = group.indexOf(ev.getSource());
        for (int i = 0; i < group.size(); i++) {
            group.elementAt(i).setState(i == index);
        }
    };

    /** */
    public CheckboxMenuItem getSelected() {
        return group.elementAt(selectedIndex);
    }

    /** */
    private final ActionSupport actionSupport = new ActionSupport();

    /** */
    public void addActionListener(ActionListener l) {
        actionSupport.addActionListener(l);
    }

    /** */
    public void removeActionListener(ActionListener l) {
        actionSupport.removeActionListener(l);
    }

    /** */
    protected void fireActionPerformed(ActionEvent ev) {
        actionSupport.fireActionPerformed(ev);
    }
}
