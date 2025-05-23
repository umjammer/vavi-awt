/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

import static java.lang.System.getLogger;


/**
 * BoxLayoutCustomizer.
 *
 * <pre>
 *  { X_AXIS, Y_AXIS }
 * </pre>
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020518 nsano initial version <br>
 */
public class BoxLayoutCustomizer extends BasicLayoutManagerCustomizer {

    private static final Logger logger = getLogger(BoxLayoutCustomizer.class.getName());

    /** */
    private final BoxLayout box;

    /** */
    private final JPanel buttons;

    /** */
    private final DefaultListModel<String> history;

    private final JCheckBox xAxis;
    private final JCheckBox yAxis;

    private final JButton glue;
    private final JButton vGlue;
    private final JButton hGlue;
    private final JButton rigid;
    private final JButton filler;

    /** */
    public BoxLayoutCustomizer() {
        // UI
        Container c1 = (Container) getComponent(1);    // right base panel
        Component c2 = c1.getComponent(0);    // upper titled border panel
        c1.remove(c2);
        c1.remove(lcPanel);

        history = new DefaultListModel<>();

        xAxis = new JCheckBox("X_AXIS");
        yAxis = new JCheckBox("Y_AXIS");
        ButtonGroup axis = new ButtonGroup();
        axis.add(xAxis);
        axis.add(yAxis);

        glue   = new JButton("createGlue");
        vGlue  = new JButton("createVerticalGlue");
        hGlue  = new JButton("createHorizontalGlue");
        rigid  = new JButton("createRigidArea");
        filler = new JButton("createFiller");

        // panel
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Box Properties"));

        // operation
        JPanel p1 = new JPanel(new BorderLayout());

        Box p2 = new Box(BoxLayout.X_AXIS);
        p2.add(xAxis);
        p2.add(Box.createRigidArea(new Dimension(5, 0)));
        p2.add(yAxis);
        p1.add(p2, BorderLayout.NORTH);

        JPanel p4 = new JPanel(new GridBagLayout());

        JPanel p3 = new JPanel(new GridLayout(5, 1));
        p3.add(glue  );
        p3.add(vGlue );
        p3.add(hGlue );
        p3.add(rigid );
        p3.add(filler);
//      p.add(new JButton("createVerticalStrut"));
//      p.add(new JButton("createHorizontalStrut"));
        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 1.0;
//      gc.gridwidth = GridBagConstraints.RELATIVE;
        gc.fill = GridBagConstraints.BOTH;
        p4.add(p3, gc);

        buttons = new JPanel();
        JScrollPane sp = new JScrollPane(buttons);
//      gc = new GridBagConstraints();
        gc.gridx = 2;
//      gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.fill = GridBagConstraints.BOTH;
        p4.add(sp, gc);

        p1.add(p4, BorderLayout.CENTER);

        p.add(p1);

        // history
        JList<?> list = new JList<>(history);
        sp = new JScrollPane(list);
        p.add(sp);

        c1.setLayout(new BorderLayout());
        c1.add(p);

        // init
        box = new BoxLayout(layoutPanel, BoxLayout.Y_AXIS);
        layoutPanel.setLayout(box);
    }

//    /** TODO Create a BoxLayout from a list */
//  public LayoutManager getObject() {
//      return new BoxLayout(container, BoxLayout.Y_AXIS);
//  }

    @Override
    public void setContainer(Container container) {
        super.setContainer(container);

        yAxis.setSelected(true);
        this.layout = new BoxLayout(container, BoxLayout.Y_AXIS);

        buttons.removeAll();
        history.clear();

        layoutPanel.removeAll();
        components.clear();

        buttons.setLayout(new GridLayout(container.getComponentCount(), 1));

        for (int i = 0; i < container.getComponentCount(); i++) {
            Component c = container.getComponent(i);
logger.log(Level.TRACE, c.getMinimumSize().width + ", " + c.getMinimumSize().height);
logger.log(Level.TRACE, c.getPreferredSize().width + ", " + c.getPreferredSize().height);
logger.log(Level.TRACE, c.getMaximumSize().width + ", " + c.getMaximumSize().height);
            // controller
            JLabel controller = new JLabel(String.valueOf(i));
            controller.setHorizontalAlignment(JLabel.CENTER);
            controller.setOpaque(true);
            controller.setBorder(BorderFactory.createRaisedBevelBorder());
            controller.setMinimumSize(ajustRatio(c.getMinimumSize()));
            controller.setPreferredSize(ajustRatio(c.getPreferredSize()));
            controller.setMaximumSize(ajustRatio(c.getMaximumSize()));
logger.log(Level.TRACE, controller.getMinimumSize().width + ", " + controller.getMinimumSize().height);
logger.log(Level.TRACE, controller.getPreferredSize().width + ", " + controller.getPreferredSize().height);
logger.log(Level.TRACE, controller.getMaximumSize().width + ", " + controller.getMaximumSize().height);

            components.put(String.valueOf(i), controller);

            // operation
            JToggleButton button = new JToggleButton(String.valueOf(i));
            buttons.add(button);

            button.addActionListener(al);
        }
    }

    /** */
    private Dimension ajustRatio(Dimension size) {
        int w = Math.round(size.width  * ratio);
        int h = Math.round(size.height * ratio);
        size.width  = Math.min(w, 32767);
        size.height = Math.min(h, 32767);
        return size;
    }

    @Override
    public void layoutContainer() {
    }

    // ----

    /** controller button pushed */
    private final ActionListener al = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ev) {
            JToggleButton button = (JToggleButton) ev.getSource();
            if (button.isSelected()) {
                int index = Integer.parseInt(button.getText());
                JLabel controller = (JLabel) components.get(String.valueOf(index));

                layoutPanel.add(controller);
                layoutPanel.validate();
                layoutPanel.repaint();

                history.addElement("Component " + index);
            } else {
                button.setSelected(true);
            }
        }
    };
}
