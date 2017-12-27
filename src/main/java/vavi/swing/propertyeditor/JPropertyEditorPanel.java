/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.propertyeditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.Customizer;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.logging.Level;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import vavi.util.Debug;


/**
 * The UI for listing, sorting, and setting component properties.
 *
 * @author John J. Walker
 * @author Mark Davidson
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 1.16 991111       original version <br>
 *          1.17 020512 nsano refine <br>
 *          1.18 020515 nsano rename <br>
 */
public class JPropertyEditorPanel extends JPanel {

    /** リソースバンドル */
    private static final ResourceBundle rb =
    ResourceBundle.getBundle("vavi.swing.resource", Locale.getDefault());

    /** Current Bean. */
    private Object bean;
    /** */
    private BeanDescriptor beanDescriptor;
    /** Stack of beans for walking bean hierarchy. */
    private Stack<Object> beanStack;

    /** */
    private JPropertyEditorTable table;
    /** */
    private PropertyDescriptorTableModel tableModel;

    // UI for the property control panel.
    private JLabel nameLabel;
    private JComboBox<?> viewCombo;

    // View options.
    private static final String[] VIEW_CHOICES = {
        "All",
        "Standard",
        "Expert",
        "Read Only",
        "Bound",
        "Constrained",
        "Hidden",
        "Preferred"
    };

    /**
     * Creates a property editor panel.
     */
    public JPropertyEditorPanel()  {
        this.setLayout(new BorderLayout());

        tableModel = new PropertyDescriptorTableModel();

        table = new JPropertyEditorTable(tableModel);
        table.addMouseListener(ml);

        add(createControlPanel(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(createNamePanel(), BorderLayout.SOUTH);
    }

    /**
     * Double clicking on the first column will call the down
     * action on the current object.
     */
    private MouseListener ml = new MouseAdapter()  {
        public void mouseClicked(MouseEvent ev)  {
            if (ev.getClickCount() == 2 && table.getSelectedColumn() == 0) {
                downAction.actionPerformed(null);
            }
        }
    };

    /**
     * Creates the panel for selecting the view and sorting order.
     */
    private JToolBar createControlPanel()  {
        viewCombo = new JComboBox<>(VIEW_CHOICES);
        viewCombo.addActionListener(viewActionListener);
        viewCombo.setSelectedItem(
            VIEW_CHOICES[PropertyDescriptorTableModel.VIEW_STANDARD]);

        JToolBar toolbar = new JToolBar();
    toolbar.setFloatable(false);

        JButton button = toolbar.add(upAction);
    button.setToolTipText((String) upAction.getValue("Name"));
        button = toolbar.add(downAction);
    button.setToolTipText((String) downAction.getValue("Name"));
        toolbar.addSeparator();
        button = toolbar.add(addAction);
    button.setToolTipText((String) addAction.getValue("Name"));
        button = toolbar.add(custAction);
    button.setToolTipText((String) custAction.getValue("Name"));
        toolbar.addSeparator();
        toolbar.add(viewCombo);

        setButtonState();

        return toolbar;
    }

    /**
     * The name panel shows the current selected item
     */
    private JPanel createNamePanel()  {
        JLabel label = new JLabel();
        label.setText(rb.getString("jPropertyEditorPanel.label.0.text"));
        nameLabel = new JLabel();
        nameLabel.setText(rb.getString("jPropertyEditorPanel.label.1.text"));

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panel.add(label);
        panel.add(nameLabel);

        return panel;
    }

    /**
     * Handler for UI interactions.
     */
    private ActionListener viewActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent evt)  {
            tableModel.filterTable(viewCombo.getSelectedIndex());
        }
    };

    /**
     * View lower level properties of the selected property.
     */
    private Action downAction = new AbstractAction(
        rb.getString("common.button.down.text"),
        (ImageIcon) UIManager.get("jPropertyEditorPanel.upIcon")) {
        public void actionPerformed(ActionEvent ev)  {
            final int col = JPropertyEditorTable.COL_VALUE;
            int row = table.getSelectedRow();
            if (row != -1)  {
                Object obj = table.getValueAt(row, col);

                if (beanStack == null) {
                    beanStack = new Stack<>();
                }
                beanStack.push(bean);
                setBean(obj);
            }
            setButtonState();
        }
    };

    /**
     * View upper level properties.
     */
    private Action upAction = new AbstractAction(
        rb.getString("common.button.up.text"),
        (ImageIcon) UIManager.get("jPropertyEditorPanel.downIcon")) {
        public void actionPerformed(ActionEvent ev)  {
            if (beanStack != null && !beanStack.empty())  {
                setBean(beanStack.pop());
            }
            setButtonState();
        }
    };

    /**
     * Handle the add gesture. Informs prop change listener to add the selected
     * current property sheet component.
     */
    private Action addAction = new AbstractAction(
        rb.getString("common.button.add.text"),
        (ImageIcon) UIManager.get("jPropertyEditorPanel.addIcon")) {
        public void actionPerformed(ActionEvent ev)  {
            final int col = JPropertyEditorTable.COL_VALUE;
            int row = table.getSelectedRow();
            if (row != -1)  {
                Object obj = table.getValueAt(row, col);

                if (obj != null && !(obj instanceof Component)) {
                    String message = obj.getClass().getName();
                    message += " sent to design panel";
                    firePropertyChange("message", "", message);
                    firePropertyChange("add", null, obj);
                }
            }
        }
    };

    /**
     * Will display a customizer in a dialog
     */
    private Action custAction = new AbstractAction(
        rb.getString("common.button.customizer.text"),
        (ImageIcon) UIManager.get("jPropertyEditorPanel.custIcon")) {
        public void actionPerformed(ActionEvent ev)  {
            Component comp = getCustomizer();

            if (comp != null)  {
                CustomizerDialog dialog = new CustomizerDialog(comp);
                dialog.setVisible(true);
            }
        }
    };

    /**
     * A customizer dialog which takes a Component which implements the
     * customizer interface.
     */
    private class CustomizerDialog extends JDialog {

        public CustomizerDialog(Component c)  {
            super(JOptionPane.getFrameForComponent(c), "Customizer Dialog");

            ((Customizer) c).setObject(bean);

            JPanel p = new JPanel();
            JButton b = new JButton(rb.getString("common.button.ok.text"));
            getRootPane().setDefaultButton(b);
            b.setMnemonic('O'); // TODO
            b.addActionListener(okActionListener);
            p.add(b);

            getContentPane().add(c, BorderLayout.CENTER);
            getContentPane().add(p, BorderLayout.SOUTH);
            pack();

            p = JPropertyEditorPanel.this;
            setLocationRelativeTo(SwingUtilities.getRoot(p));
        }

        /** OK means only closing the dialog. */
        private ActionListener okActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent ev)  {
                dispose();
            }
        };
    }

    /**
     * Sets the bean that this PropertyPane represents.
     */
    public void setSelectedItem(Object bean)  {
        beanStack = null;   // Reset the bean stack.

        setBean(bean);
        setButtonState();
    }

    /**
     * Sets the state of the up and down buttons based on the contents of
     * the stack.
     */
    private void setButtonState()  {

        if (bean == null || beanStack == null || beanStack.isEmpty())  {
            upAction.setEnabled(false);
        } else {
            upAction.setEnabled(true);
        }

        downAction.setEnabled(bean != null);

        addAction.setEnabled(bean != null);
        // Enable customizer button if the model has a customizer.
        custAction.setEnabled(bean != null && hasCustomizer());

        viewCombo.setEnabled(bean != null);
    }

    /**
     * Sets the PropertyPane to show the properties of the named bean.
     */
    protected void setBean(Object bean)  {
        this.bean = bean;

        if (bean != null)  {
            try {
                BeanInfo info = Introspector.getBeanInfo(bean.getClass());
                beanDescriptor = info.getBeanDescriptor();

                nameLabel.setText(bean.getClass().getName());

                tableModel.setObject(bean);
                tableModel.filterTable(viewCombo.getSelectedIndex());
                tableModel.fireTableDataChanged();

                table.setVisible(true);
            } catch (IntrospectionException e) {
Debug.println(Level.SEVERE, e);
                nameLabel.setText(rb.getString("jPropertyEditorPanel.label.1.text"));
                table.setVisible(false);
            }
        } else {
            nameLabel.setText(rb.getString("jPropertyEditorPanel.label.1.text"));
            table.setVisible(false);
        }
    }

    /**
     * Returns a flag indicating if the encapsulated object has a customizer.
     */
    private boolean hasCustomizer() {
        if (beanDescriptor != null) {
            Class<?> clazz = beanDescriptor.getCustomizerClass();
            return clazz != null;
        }

        return false;
    }

    /**
     * Gets the customizer for the current object.
     * @return    New instance of the customizer or null
     *        if there isn't a customizer.
     */
    private Component getCustomizer() {
        Component customizer = null;

        if (beanDescriptor != null) {
            Class<?> clazz = beanDescriptor.getCustomizerClass();

            if (clazz != null) {
                try {
                    customizer = (Component) clazz.newInstance();
                } catch (Exception e) {
Debug.println("Instantiation exception creating Customizer: " + e);
                }
            }
        }

        return customizer;
    }

    //-------------------------------------------------------------------------

    static {
        Toolkit t = Toolkit.getDefaultToolkit();
        Class<?> clazz = JPropertyEditorPanel.class;
        String base = "/toolbarButtonGraphics/";
        UIDefaults table = UIManager.getDefaults();
        table.put("jPropertyEditorPanel.upIcon", new ImageIcon(t.getImage(
                clazz.getResource(base + "navigation/Down16.gif"))));
        table.put("jPropertyEditorPanel.downIcon", new ImageIcon(t.getImage(
                clazz.getResource(base + "navigation/Up16.gif"))));
        table.put("jPropertyEditorPanel.addIcon", new ImageIcon(t.getImage(
                clazz.getResource(base + "general/Add16.gif"))));
        table.put("jPropertyEditorPanel.custIcon", new ImageIcon(t.getImage(
                clazz.getResource(base + "general/Preferences16.gif"))));
    }
}

/* */
