/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;


/**
 * CardLayoutCustomizer.
 *
 * TODO CardLayout#show した後，他の setVisible が false になってます．
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020528 nsano initial version <br>
 *          0.01 020618 nsano refine <br>
 */
public class CardLayoutCustomizer extends BasicLayoutManagerCustomizer {

    /** */
    private CardLayout card;

    /** constraints editor */
    private CardLayoutConstraintsEditor constraintsEditor;

    /** */
    public CardLayoutCustomizer() {
        this.card = new CardLayout();
        layoutPanel.setLayout(card);

        this.layout = new CardLayout();
        tableModel.setObject(layout);
        tableModel.fireTableDataChanged();

        tableModel.addTableModelListener(tml);
        constraintsEditor = new CardLayoutConstraintsEditor(card);
        lcPanel.addMouseListener(ml);
    }

    /** */
    private TableModelListener tml = new TableModelListener() {
        public void tableChanged(TableModelEvent ev) {
            updateLayout();
        }
    };

    /** */
    private void updateLayout() {
        card.setHgap(Math.round(((CardLayout) layout).getHgap() * ratio));
        card.setVgap(Math.round(((CardLayout) layout).getVgap() * ratio));

        card.layoutContainer(layoutPanel);
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

            layoutPanel.add(controller, String.valueOf(i));
            LayoutConstraints lc = new CardLayoutConstraints();
            lc.setConstraints(String.valueOf(i));
            constraintsEditor.setLayoutConstraints(i, lc);

            components.put(controller, component);
        }
    }

    /** called last, to set layout to your container */
    public void layoutContainer() {
        Iterator<Object> e = components.keySet().iterator();
        while (e.hasNext()) {
            JLabel controller = (JLabel) e.next();
            String constraints = controller.getText();
            Component component = components.get(controller);
            layout.removeLayoutComponent(component);
            ((CardLayout) layout).addLayoutComponent(component, constraints);
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
            constraintsEditor.setCurrentIndex(index);

            card.show(layoutPanel, "" + index);

            TitledBorder b = (TitledBorder) lcPanel.getBorder();
            b.setTitle("Component Constraints: " + index);
            lcPanel.repaint();

            lcTableModel.
                setObject(constraintsEditor.getLayoutConstraints(index));
        }
    };
}

/* */
