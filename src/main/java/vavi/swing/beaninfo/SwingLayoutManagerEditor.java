/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.beaninfo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import vavi.swing.layout.LayoutManagerChooser;
import vavi.swing.layout.LayoutManagerInfoFactory;
import vavi.swing.layout.SampleLayoutManagerInfo;

import static java.lang.System.getLogger;


/**
 * Swing version of a Layout property editor.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020516 nsano initial version <br>
 */
public class SwingLayoutManagerEditor extends SwingEditorSupport {

    private static final Logger logger = getLogger(SwingLayoutManagerEditor.class.getName());

    private final JComboBox<?> layoutCombo;
    private final JButton layoutButton;
//      private JLabel layoutLabel;
    private LayoutDialog layoutDialog;

    private final List<?> lmis =
        LayoutManagerInfoFactory.getSampleLayoutManagerInfos();

    private Container bean;

    /** */
    public SwingLayoutManagerEditor() {

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        layoutCombo = createComboBox();

        // need rigid area match up
        Icon buttonIcon = UIManager.getIcon("beaninfo.LayoutIcon");
        layoutButton = new JButton(buttonIcon);
//      Dimension d = new Dimension(buttonIcon.getIconWidth(), buttonIcon.getIconHeight());
        layoutButton.setMargin(SwingEditorSupport.BUTTON_MARGIN);

//      layoutLabel = new JLabel();

        setAlignment(layoutCombo);
        setAlignment(layoutButton);
//      setAlignment(layoutLabel);

        panel.add(layoutCombo);
        panel.add(Box.createRigidArea(new Dimension(5,0)));
        panel.add(layoutButton);
//      panel.add(Box.createRigidArea(new Dimension(5,0)));
//      panel.add(layoutLabel);
        panel.add(Box.createHorizontalGlue());

        plug();
    }

    /** If you don't use super.setValue, the firePropertyChange probably won't work. */
    @Override
    public void setValue(Object value) {
logger.log(Level.TRACE, value == null ? String.valueOf((Object) null) : String.valueOf(value.hashCode()));
        super.setValue(value);

        if (value != null && bean != null) {
logger.log(Level.TRACE, value);
            ((LayoutManager) value).layoutContainer(bean);
            bean.validate();
            bean.repaint();
        }

        // ui

        unplug();
        if (value == null) {
            layoutCombo.setSelectedIndex(0);
        } else {
            int i = 1;
            for (; i < layoutCombo.getItemCount() - 1; i++) {
                SampleLayoutManagerInfo li =
                    (SampleLayoutManagerInfo) layoutCombo.getItemAt(i);
                LayoutManager layout = li.layout;
logger.log(Level.TRACE, li.desc);
                if (layout.getClass().isInstance(value)) {
                    layoutCombo.setSelectedIndex(i);
                    plug();
                    return;
                }
            }
            SampleLayoutManagerInfo li =
                (SampleLayoutManagerInfo) layoutCombo.getItemAt(i);
            li.layout = (LayoutManager) value;
            layoutCombo.setSelectedIndex(i);
        }
        plug();
    }

    /** */
    public void setBean(Object bean) {
        if (bean instanceof Container) {
            this.bean = (Container) bean;
logger.log(Level.TRACE, "bean is Container: " + bean.getClass().getName());
        } else {
logger.log(Level.DEBUG, "bean is not Container: " + bean);
            this.bean = null;
        }
    }

    /** */
    private void plug() {
        layoutButton.addActionListener(layoutButtonListener);
        layoutCombo.addActionListener(layoutComboListener);
    }

    /** */
    private void unplug() {
        layoutButton.removeActionListener(layoutButtonListener);
        layoutCombo.removeActionListener(layoutComboListener);
    }

    /** Will show dialog. */
    private final ActionListener layoutButtonListener = ev -> {
        if (layoutDialog == null) {
            layoutDialog = new LayoutDialog(panel, "Layout Chooser");
        }
        layoutDialog.setSelectedContainer(bean);
        layoutDialog.setSelectedLayoutManager((LayoutManager) getValue());
        if (layoutDialog.showDialog() == LayoutDialog.APPROVE_OPTION) {
            LayoutManager layout = layoutDialog.getSelectedLayoutManager();
            setValue(layout);
        }
    };

    /** */
    private final ActionListener layoutComboListener = ev -> {
        @SuppressWarnings("unchecked")
        JComboBox<SampleLayoutManagerInfo> cb = (JComboBox<SampleLayoutManagerInfo>) ev.getSource();
        SampleLayoutManagerInfo lmi = (SampleLayoutManagerInfo) cb.getSelectedItem();
        LayoutManager layout = lmi.layout;
        setValue(layout);
    };

    /** */
    private JComboBox<?> createComboBox() {
        JComboBox<?> c = new JComboBox<>(lmis.toArray());
        c.setRenderer(lcr);
        c.setPreferredSize(SwingEditorSupport.MEDIUM_DIMENSION);
        c.setMinimumSize(SwingEditorSupport.MEDIUM_DIMENSION);
        c.setMaximumSize(SwingEditorSupport.MEDIUM_DIMENSION);
        c.setSelectedIndex(-1);
        return c;
    }

    /** */
    private final ListCellRenderer<Object> lcr = new DefaultListCellRenderer() {
        {
            setOpaque(true);
        }

        final Color selectedFG = UIManager.getColor("ComboBox.selectionBackground");
        final Color selectedBG = UIManager.getColor("ComboBox.selectionForeground");
        final Color FG = UIManager.getColor("ComboBox.background");
        final Color BG = UIManager.getColor("ComboBox.foreground");

        @Override
        public Component getListCellRendererComponent(JList<?> list,
                                                      Object value,
                                                      int modelIndex,
                                                      boolean isSelected,
                                                      boolean cellHasFocus) {

            if (!(value instanceof SampleLayoutManagerInfo lmi)) {
                setText("");
                setIcon(null);
                return this;
            }

            setIcon(lmi.icon);
            setText(lmi.desc);
            if (isSelected) {
                setBackground(selectedFG);
                setForeground(selectedBG);
            } else {
                setBackground(FG);
                setForeground(BG);
            }
            return this;
        }
    };

    /** */
    private static class LayoutDialog extends JDialog {
        public static final int APPROVE_OPTION = 0;
        public static final int CANCEL_OPTION = 1;

        private int returnValue = CANCEL_OPTION;

        private final LayoutManagerChooser layoutChooser;

        public LayoutDialog(Component c, String title) {
            super(JOptionPane.getFrameForComponent(c), title, true);

            Container contentPane = getContentPane();
            contentPane.setLayout(new BorderLayout());

            JPanel panel = new JPanel();
            JButton okButton = new JButton("OK");
            okButton.addActionListener(okListener);
            getRootPane().setDefaultButton(okButton);
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(cancelListener);
            panel.add(okButton);
            panel.add(cancelButton);
            contentPane.add(panel, BorderLayout.SOUTH);

            // layoutlayout
            layoutChooser = new LayoutManagerChooser();
            contentPane.add(layoutChooser);

            pack();

            setLocationRelativeTo(SwingUtilities.getRoot(c));
        }

        /** */
        private final ActionListener okListener = ev -> {
            returnValue = APPROVE_OPTION;
            // get the Layout from the pane
            setVisible(false);
//              dispose();
        };

        /** */
        private final ActionListener cancelListener = ev -> {
            returnValue = CANCEL_OPTION;
            setVisible(false);
//              dispose();
        };

        /** @param container nullable */
        public void setSelectedContainer(Container container) {
            layoutChooser.setSelectedContainer(container);
        }

        public void setSelectedLayoutManager(LayoutManager layout) {
            layoutChooser.setSelectedLayoutManager(layout);
        }

        public LayoutManager getSelectedLayoutManager() {
            return layoutChooser.getSelectedLayoutManager();
        }

        public int showDialog() {
            returnValue = CANCEL_OPTION;
            this.setVisible(true);
            return returnValue;
        }
    }

    // ----

    /* */
    static {
        Class<?> clazz = SwingLayoutManagerEditor.class;
        UIDefaults table = UIManager.getDefaults();
        table.put("beaninfo.LayoutIcon", LookAndFeel.makeIcon(clazz, "resources/LayoutIcon.gif"));
    }
}
