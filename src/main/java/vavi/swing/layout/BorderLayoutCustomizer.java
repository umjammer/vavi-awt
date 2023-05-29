/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import vavi.util.Debug;


/**
 * BorderLayoutCustomizer.
 *
 * @todo get constraint when initializing components in the container.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020527 nsano initial version <br>
 *          0.01 020618 nsano refine <br>
 */
public class BorderLayoutCustomizer extends BasicLayoutManagerCustomizer {

    /** layout for virtual screen */
    private BorderLayout border;

    /** constraints editor */
    private BorderLayoutConstraintsEditor constraintsEditor;

    /** Creates a customizer for Borderlayout. */
    public BorderLayoutCustomizer() {
        this.border = new BorderLayout();
        layoutPanel.setLayout(border);

        this.layout = new BorderLayout();
        tableModel.setObject(layout);
        tableModel.fireTableDataChanged();

        tableModel.addTableModelListener(tml);
        constraintsEditor = new BorderLayoutConstraintsEditor(border);
        lcPanel.addMouseListener(ml);
    }

    /** called when layout property changed */
    private TableModelListener tml = new TableModelListener() {
        public void tableChanged(TableModelEvent ev) {
            updateLayout();
        }
    };

    /** */
    private void updateLayout() {
        border.setHgap(Math.round(((BorderLayout) layout).getHgap() * ratio));
        border.setVgap(Math.round(((BorderLayout) layout).getVgap() * ratio));

        border.layoutContainer(layoutPanel);
    }

    /** called once at first */
    public void setObject(LayoutManager layout) {
        updateLayout();

        super.setObject(layout);

        tableModel.setObject(layout);
        tableModel.fireTableDataChanged();
    }

    /** called first */
    public void setContainer(Container container) {
        super.setContainer(container);

        constraintsEditor.clear();
        constraintsEditor.setContainer(container);

        layoutPanel.removeAll();
        components.clear();

        for (int i = 0; i < container.getComponentCount(); i++) {
            Component component = container.getComponent(i);

            JButton controller = new JButton("" + i);
            controller.addActionListener(al);

            Object constraints = constraintsEditor.associateConstraints(i);
Debug.println(Level.FINER, i + ": " + constraints);
            LayoutConstraints lc = new BorderLayoutConstraints();
            lc.setConstraints(constraints);
            constraintsEditor.setLayoutConstraints(i, lc);

            layoutPanel.add(controller, constraints);

            components.put(controller, component);
        }

        constraintsEditor.setContainer(layoutPanel);
    }

    /** called last, to set layout to your container */
    public void layoutContainer() {
        Iterator<Object> e = components.keySet().iterator();
        while (e.hasNext()) {
            JButton controller = (JButton) e.next();
            int i = Integer.parseInt(controller.getText());
            LayoutConstraints lc = constraintsEditor.getLayoutConstraints(i);
            if (lc != null) {
                Object constraints = lc.getConstraints();
                Component component = components.get(controller);
                layout.removeLayoutComponent(component);
                ((BorderLayout) layout).addLayoutComponent(component, constraints);
            }
        }
    }

    // constraints editor -----------------------------------------------------

    /** mouse click on constraint panel means to count up component no. */
    private MouseListener ml = new MouseAdapter() {
        public void mouseClicked(MouseEvent ev) {
            int index = constraintsEditor.getCurrentIndex();
            if (index != -1) {
                if (index + 1 < layoutPanel.getComponentCount()) {
                    index++;
                } else {
                    index = 0;
                }
            } else {
                if (layoutPanel.getComponentCount() > 1) {
                    index = 0;
                } else {
                    return;
                }
            }
            setCurrentConstraints(index);
        }
    };

    /** button click means to select a component */
    private ActionListener al = new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            int index = Integer.parseInt(((JButton) ev.getSource()).getText());
            setCurrentConstraints(index);
        }
    };

    /** */
    private void setCurrentConstraints(int index) {
        constraintsEditor.setCurrentIndex(index);

        TitledBorder b = (TitledBorder) lcPanel.getBorder();
        b.setTitle("Component Constraints: " + index);
        lcPanel.repaint();

        lcTableModel.setObject(constraintsEditor.getLayoutConstraints(index));
    }
}

/* */
