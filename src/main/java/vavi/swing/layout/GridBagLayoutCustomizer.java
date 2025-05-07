/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;

import static java.lang.System.getLogger;


/**
 * GridBagLayoutCustomizer.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020528 nsano initial version <br>
 *          0.01 020615 nsano remoce containerEditor <br>
 */
public class GridBagLayoutCustomizer extends BasicLayoutManagerCustomizer {

    private static final Logger logger = getLogger(GridBagLayoutCustomizer.class.getName());

    /** layout for virtual screen */
    private final GridBagLayout gridbag;

    /** constraints editor */
    private final GridBagLayoutConstraintsEditor constraintsEditor;

    /** Creates customizer for GridBagLayout */
    public GridBagLayoutCustomizer() {
        // UI
        Container c1 = (Container) getComponent(1); // right base panel
logger.log(Level.TRACE, c1.getName());
        Component c2 = c1.getComponent(0); // upper titled border panel
logger.log(Level.TRACE, c2.getName());
        c1.remove(c2);
        ((GridLayout) c1.getLayout()).setRows(1);
        c1.doLayout();

        // init
        this.gridbag = new GridBagLayout();
        this.layout = new GridBagLayout();

        layoutPanel.setLayout(gridbag);

        constraintsEditor = new GridBagLayoutConstraintsEditor(gridbag);
        constraintsEditor.setPropertyChangeListener(pcl);

        lcPanel.addMouseListener(ml);
    }

//    /** TODO */
//      public LayoutManager getObject() {
//          layoutPanel.validate();
//          layoutPanel.repaint();
//          return super.getObject();
//      }

    /** sets container that use layout editing */
    @Override
    public void setContainer(Container container) {
        super.setContainer(container);

        constraintsEditor.clear();
        constraintsEditor.setContainer(container);
        constraintsEditor.associateConstraints();

        layoutPanel.removeAll();
        components.clear();

        for (int i = 0; i < container.getComponentCount(); i++) {
            Component component = container.getComponent(i);
logger.log(Level.TRACE, "---- " + i + " ----");
logger.log(Level.TRACE, component.getSize().width + ", " + component.getSize().height);
logger.log(Level.TRACE, component.getMinimumSize().width + ", " + component.getMinimumSize().height);
logger.log(Level.TRACE, component.getPreferredSize().width + ", " + component.getPreferredSize().height);
logger.log(Level.TRACE, component.getMaximumSize().width + ", " + component.getMaximumSize().height);

            JButton controller = new JButton(String.valueOf(i));
            controller.setMargin(new Insets(0, 0, 0, 0));
//controller.setMinimumSize(adjustRatio(component.getMinimumSize()));
//controller.setPreferredSize(adjustRatio(component.getPreferredSize()));
//controller.setMaximumSize(adjustRatio(component.getMaximumSize()));
//controller.setSize(adjustRatio(component.getSize()));
            controller.addActionListener(al);

            LayoutConstraints lc = constraintsEditor.getLayoutConstraints(i);
            GridBagConstraints cs = (GridBagConstraints) lc.getConstraints();

            layoutPanel.add(controller, adjustRatio(cs));
logger.log(Level.TRACE, "----");
logger.log(Level.TRACE, controller.getSize().width + ", " + controller.getSize().height);
logger.log(Level.TRACE, controller.getMinimumSize().width + ", " + controller.getMinimumSize().height);
logger.log(Level.TRACE, controller.getPreferredSize().width + ", " + controller.getPreferredSize().height);
logger.log(Level.TRACE, controller.getMaximumSize().width + ", " + controller.getMaximumSize().height);

            components.put(controller, component);
        }

//        layoutPanel.validate();
//        layoutPanel.repaint();

        constraintsEditor.setContainer(layoutPanel);
    }

//    /** TODO */
//    private Dimension adjustRatio(Dimension size) {
//        Dimension newSize = new Dimension();
//        int w = Math.round(size.width  * ratio);
//        int h = Math.round(size.height * ratio);
//        newSize.width  = w > 32767 ? 32767 : w;
//        newSize.height = h > 32767 ? 32767 : h;
//        return newSize;
//    }

    /** called last, to set layout to your container */
    @Override
    public void layoutContainer() {
        for (Object o : components.keySet()) {
            JButton controller = (JButton) o;
            int i = Integer.parseInt(controller.getText());
            LayoutConstraints lc = constraintsEditor.getLayoutConstraints(i);
            if (lc != null) {
                Object constraints = lc.getConstraints();
                Component component = components.get(controller);
                layout.removeLayoutComponent(component);
                ((GridBagLayout) layout).addLayoutComponent(component, constraints);
            }
        }
    }

    // constraints editor ----

    /** mouse click on constraint panel means to count up component no. */
    private final MouseListener ml = new MouseAdapter() {
        @Override
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
    private final ActionListener al = ev -> {
        int index = Integer.parseInt(((JButton) ev.getSource()).getText());
        setCurrentConstraints(index);
    };

    /** */
    private void setCurrentConstraints(int index) {
        constraintsEditor.setCurrentIndex(index);

        TitledBorder b = (TitledBorder) lcPanel.getBorder();
        b.setTitle("Component Constraints: " + index);
        lcPanel.repaint();

        lcTableModel.setObject(constraintsEditor.getLayoutConstraints(index));
    }

    // ----

    /** when constraints updated */
    private final PropertyChangeListener pcl = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent ev) {
            int index = constraintsEditor.getCurrentIndex();
            Component component = container.getComponent(index);
            LayoutConstraints lc = (LayoutConstraints) ev.getSource();
            GridBagConstraints cs = (GridBagConstraints) lc.getConstraints();

            gridbag.removeLayoutComponent(component);
            gridbag.addLayoutComponent(component, adjustRatio(cs));

            gridbag.layoutContainer(layoutPanel);
logger.log(Level.DEBUG, index);
        }
    };

    /** */
    private GridBagConstraints adjustRatio(GridBagConstraints gc) {
        GridBagConstraints newGc = new GridBagConstraints();
        newGc.anchor        =            gc.anchor;
        newGc.fill          =            gc.fill;
        newGc.gridheight    =            gc.gridheight;
        newGc.gridwidth     =            gc.gridwidth;
        newGc.gridx         =            gc.gridx;
        newGc.gridy         =            gc.gridy;
        newGc.insets.top    = Math.round(gc.insets.top    * ratio);
        newGc.insets.left   = Math.round(gc.insets.left   * ratio);
        newGc.insets.bottom = Math.round(gc.insets.bottom * ratio);
        newGc.insets.right  = Math.round(gc.insets.right  * ratio);
        newGc.ipadx         = Math.round(gc.ipadx         * ratio);
        newGc.ipady         = Math.round(gc.ipady         * ratio);
        newGc.weightx       =            gc.weightx;
        newGc.weighty       =            gc.weighty;

logger.log(Level.TRACE, ratio);
        return newGc;
    }
}
