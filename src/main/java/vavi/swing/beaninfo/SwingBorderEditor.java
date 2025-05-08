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
import java.awt.event.ActionEvent;
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
import javax.swing.border.Border;

import vavi.swing.border.BorderChooser;
import vavi.swing.border.BorderInfoFactory;
import vavi.swing.border.SampleBorderInfo;

import static java.lang.System.getLogger;


/**
 * Swing version of a Border property editor.
 *
 * @author Tom Santos
 * @author Mark Davidson
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 1.20 000620 original version <br>
 *          1.21 020516 nsano i18n <br>
 *          1.30 020518 nsano refine <br>
 */
public class SwingBorderEditor extends SwingEditorSupport {

    private static final Logger logger = getLogger(SwingBorderEditor.class.getName());

    private final JComboBox<?> borderCombo;
    private final JButton borderButton;
//  private JLabel borderLabel;
    private BorderDialog borderDialog;

    private final List<?> bis = BorderInfoFactory.getSampleBorderInfos();

    /** */
    public SwingBorderEditor() {

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        borderCombo = createComboBox();

        // need rigid area match up
        Icon buttonIcon = UIManager.getIcon("beaninfo.BorderIcon");
        borderButton = new JButton(buttonIcon);
//      Dimension d = new Dimension(buttonIcon.getIconWidth(), buttonIcon.getIconHeight());
        borderButton.setMargin(SwingEditorSupport.BUTTON_MARGIN);

//      borderLabel = new JLabel();

        setAlignment(borderCombo);
        setAlignment(borderButton);
//      setAlignment(borderLabel);

        panel.add(borderCombo);
        panel.add(Box.createRigidArea(new Dimension(5, 0)));
        panel.add(borderButton);
//      panel.add(Box.createRigidArea(new Dimension(5, 0)));
//      panel.add(borderLabel);
        panel.add(Box.createHorizontalGlue());

        plug();
    }

    @Override
    public void setValue(Object value) {

        super.setValue(value);

        unplug();
        if (value == null) {
            borderCombo.setSelectedIndex(0);
        } else {
            int i = 1;
            for (; i < borderCombo.getItemCount() - 1; i++) {
                SampleBorderInfo bi =
                    (SampleBorderInfo) borderCombo.getItemAt(i);
                Border border = bi.border;
logger.log(Level.TRACE, bi.desc);
                if (value == border) {
                    borderCombo.setSelectedIndex(i);
                    plug();
                    return;
                }
            }
            SampleBorderInfo bi = (SampleBorderInfo) borderCombo.getItemAt(i);
            bi.border = (Border) value;
            borderCombo.setSelectedIndex(i);
        }
        plug();
    }

   /** */
   private void plug() {
        borderButton.addActionListener(borderButtonListener);
        borderCombo.addActionListener(borderComboListener);
    }

    /** */
    private void unplug() {
        borderButton.removeActionListener(borderButtonListener);
        borderCombo.removeActionListener(borderComboListener);
    }

    /** Pushing button will show the dialog. */
    private final ActionListener borderButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ev) {
            if (borderDialog == null) {
                borderDialog = new BorderDialog(panel, "Border Chooser");
            }
            borderDialog.setSelectedBorder((Border) getValue());
            if (borderDialog.showDialog() == BorderDialog.APPROVE_OPTION) {
                Border border = borderDialog.getSelectedBorder();
                setValue(border);
            }
        }
    };

    /** */
    private final ActionListener borderComboListener = ev -> {
        @SuppressWarnings("unchecked")
        JComboBox<SampleBorderInfo> cb = (JComboBox<SampleBorderInfo>) ev.getSource();
        SampleBorderInfo bi = (SampleBorderInfo) cb.getSelectedItem();
        Border border = bi.border;
        setValue(border);
    };

    /** */
    private JComboBox<?> createComboBox() {
        JComboBox<?> c = new JComboBox<>(bis.toArray());
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
            if (!(value instanceof SampleBorderInfo bi)) {
                setText("");
                setIcon(null);
                return this;
            }

            setText(bi.desc);
            setIcon(bi.icon);
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
    private class BorderDialog extends JDialog {
        public static final int APPROVE_OPTION = 0;
        public static final int CANCEL_OPTION = 1;

        private int returnValue = CANCEL_OPTION;

        private final BorderChooser borderChooser;

        public BorderDialog(Component c, String title) {
            super(JOptionPane.getFrameForComponent(c), title, true);

            Container contentPane = getContentPane();
            contentPane.setLayout(new BorderLayout());

            JPanel pane = new JPanel();
            JButton okButton = new JButton("OK");
            okButton.addActionListener(okListener);
            getRootPane().setDefaultButton(okButton);
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(cancelListener);
            pane.add(okButton);
            pane.add(cancelButton);
            contentPane.add(pane, BorderLayout.SOUTH);

            // borderlayout
            borderChooser = new BorderChooser();
            contentPane.add(borderChooser);

            pack();

            setLocationRelativeTo(SwingUtilities.getRoot(panel));
        }

        private final ActionListener okListener = ev -> {
            returnValue = APPROVE_OPTION;
            setVisible(false);
//                dispose();
        };

        private final ActionListener cancelListener = ev -> {
            returnValue = CANCEL_OPTION;
            setVisible(false);
//                dispose();
        };

        public void setSelectedBorder(Border border) {
            borderChooser.setSelectedBorder(border);
        }

        public Border getSelectedBorder() {
            return borderChooser.getSelectedBorder();
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
        Class<?> clazz = SwingBorderEditor.class;
        UIDefaults table = UIManager.getDefaults();
        table.put("beaninfo.BorderIcon", LookAndFeel.makeIcon(clazz, "resources/BorderIcon.gif"));
    }
}
