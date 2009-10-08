/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;


/**
 * GridLayoutCustomizer.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020518 nsano initial version <br>
 *          0.01 020618 nsano refine <br>
 */
public class GridLayoutCustomizer extends BasicLayoutManagerCustomizer {

    /** */
    private GridLayout grid;

    /** */
    public GridLayoutCustomizer() {
        // UI
        lcPanel.setVisible(false);

        // init
        this.grid = new GridLayout(2, 2);
        layoutPanel.setLayout(grid);

        this.layout = new GridLayout(2, 2);
        tableModel.setObject(layout);
        tableModel.fireTableDataChanged();

        tableModel.addTableModelListener(tml);
    }

    /** */
    private TableModelListener tml = new TableModelListener() {
        public void tableChanged(TableModelEvent ev) {
            updateLayout();
        }
    };

    /** */
    private void updateLayout() {
        grid.setHgap(Math.round(((GridLayout) layout).getHgap() * ratio));
        grid.setVgap(Math.round(((GridLayout) layout).getVgap() * ratio));
        grid.setColumns(((GridLayout) layout).getColumns());
        grid.setRows(((GridLayout) layout).getRows());

        grid.layoutContainer(layoutPanel);
    }

    /** */
    public void setObject(LayoutManager layout) {
        updateLayout();

        super.setObject(layout);

        tableModel.setObject(layout);
        tableModel.fireTableDataChanged();
    }

    /** */
    public void setContainer(Container container) {
        super.setContainer(container);

        layoutPanel.removeAll();
        components.clear();

        for (int i = 0; i < container.getComponentCount(); i++) {
            Component component = container.getComponent(i);

            JLabel controller = new JLabel(String.valueOf(i));
            controller.setHorizontalAlignment(JLabel.CENTER);
            controller.setOpaque(true);
            controller.setBorder(BorderFactory.createRaisedBevelBorder());
            layoutPanel.add(controller);

            components.put(controller, component);
        }
    }
}

/* */
