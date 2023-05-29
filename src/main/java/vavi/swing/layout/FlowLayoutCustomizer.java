/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.event.TableModelListener;

import vavi.util.Debug;


/**
 * FlowLayoutCustomizer.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020527 nsano initial version <br>
 *          0.01 020618 nsano refine <br>
 */
public class FlowLayoutCustomizer extends BasicLayoutManagerCustomizer {

    /** layout for layoutPanel */
    private FlowLayout flow;

    /** */
    public FlowLayoutCustomizer() {
        // UI
        lcPanel.setVisible(false);

        // init
        this.flow = new FlowLayout();
        layoutPanel.setLayout(flow);

        this.layout = new FlowLayout();
        tableModel.setObject(layout);
        tableModel.fireTableDataChanged();

        tableModel.addTableModelListener(tml);
    }

    /** */
    private TableModelListener tml = ev -> updateLayout();

    /** */
    private void updateLayout() {
        flow.setHgap(Math.round(((FlowLayout) layout).getHgap() * ratio));
        flow.setVgap(Math.round(((FlowLayout) layout).getVgap() * ratio));
        flow.setAlignment(((FlowLayout) layout).getAlignment());

        flow.layoutContainer(layoutPanel);
    }

    /** */
    public void setObject(LayoutManager layout) {
        updateLayout();

Debug.println(Level.FINER, "flow  : " + flow.hashCode());
Debug.println(Level.FINER, "layout: " + layout.hashCode());
        super.setObject(layout);

        tableModel.setObject(layout);
        tableModel.fireTableDataChanged();
    }

    /** TODO */
    public LayoutManager getObject() {
        layoutPanel.validate();
        layoutPanel.repaint();
        return super.getObject();
    }

    /** */
    public void setContainer(Container container) {
        super.setContainer(container);

        layoutPanel.removeAll();
        components.clear();

        for (int i = 0; i < container.getComponentCount(); i++) {
            Component component = container.getComponent(i);
Debug.println(Level.FINER, "---- " + i + " ----");
Debug.println(Level.FINER, component.getSize().width + ", " + component.getSize().height);
Debug.println(Level.FINER, component.getMinimumSize().width + ", " + component.getMinimumSize().height);
Debug.println(Level.FINER, component.getPreferredSize().width + ", " + component.getPreferredSize().height);
Debug.println(Level.FINER, component.getMaximumSize().width + ", " + component.getMaximumSize().height);

            JLabel controller = new JLabel(String.valueOf(i));
            controller.setHorizontalAlignment(JLabel.CENTER);
            controller.setOpaque(true);
            controller.setBorder(BorderFactory.createRaisedBevelBorder());
            Dimension size = component.getPreferredSize();
            controller.setPreferredSize(adjustRatio(size));
Debug.println(Level.FINER, "----");
Debug.println(Level.FINER, controller.getSize().width + ", " + controller.getSize().height);
Debug.println(Level.FINER, controller.getMinimumSize().width + ", " + controller.getMinimumSize().height);
Debug.println(Level.FINER, controller.getPreferredSize().width + ", " + controller.getPreferredSize().height);
Debug.println(Level.FINER, controller.getMaximumSize().width + ", " + controller.getMaximumSize().height);

            layoutPanel.add(controller);

            components.put(controller, component);
        }

        layoutPanel.validate();
        layoutPanel.repaint();
    }

    /** */
    private Dimension adjustRatio(Dimension size) {
        Dimension newSize = new Dimension();
        int w = Math.round(size.width  * ratio);
        int h = Math.round(size.height * ratio);
        newSize.width  = Math.min(w, 32767);
        newSize.height = Math.min(h, 32767);
        return newSize;
    }
}

/* */
