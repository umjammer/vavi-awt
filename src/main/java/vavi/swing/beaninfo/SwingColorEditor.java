/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.beaninfo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalComboBoxIcon;

import vavi.swing.colorchooser.SmallColorChooserPopupMenu;
import vavi.util.Debug;


/**
 * Swing version of a Color property editor.
 * <p>
 * [TextField ComboBox Button]
 * </p>
 * <pre>
 * TextField               [r, g, b]
 * SmallColorChooserButton SmallColorChooserPopupMenu
 * Button                  ColorChooser
 * </pre>
 *
 * @author Tom Santos
 * @author Mark Davidson
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 1.20 000620 original version <br>
 *          1.30 020518 nsano refine <br>
 */
public class SwingColorEditor extends SwingEditorSupport {

    private JTextField rgbValue;
    private JButton colorChooserButton;
    private SmallColorChooserButton colorChooserCombo;

    /** */
    public SwingColorEditor() {

        UIDefaults table = UIManager.getDefaults();
        table.put("beaninfo.ColorIcon",
          LookAndFeel.makeIcon(getClass(), "resources/ColorIcon.gif"));
        table.put("beaninfo.ColorPressedIcon",
          LookAndFeel.makeIcon(getClass(), "resources/ColorPressedIcon.gif"));
        Icon colorIcon = UIManager.getIcon("beaninfo.ColorIcon");
        Icon colorPressedIcon = UIManager.getIcon("beaninfo.ColorPressedIcon");

        rgbValue = new JTextField();
        colorChooserCombo = new SmallColorChooserButton();
        colorChooserButton = new JButton(colorIcon);

//      Dimension d = new Dimension(colorIcon.getIconWidth(), colorIcon.getIconHeight());
        rgbValue.setPreferredSize(SwingEditorSupport.MEDIUM_DIMENSION);
        rgbValue.setMaximumSize(SwingEditorSupport.MEDIUM_DIMENSION);
        rgbValue.setMinimumSize(SwingEditorSupport.MEDIUM_DIMENSION);
        colorChooserCombo.setPreferredSize(SwingEditorSupport.SMALL_DIMENSION);
        colorChooserCombo.setMaximumSize(SwingEditorSupport.SMALL_DIMENSION);
        colorChooserCombo.setMinimumSize(SwingEditorSupport.SMALL_DIMENSION);

        colorChooserButton.setPressedIcon(colorPressedIcon);
        colorChooserButton.setToolTipText("press to bring up color chooser");
        colorChooserButton.setMargin(SwingEditorSupport.BUTTON_MARGIN);
        colorChooserButton.setBorderPainted(false);
        colorChooserButton.setContentAreaFilled(false);

        setAlignment(rgbValue);
        setAlignment(colorChooserCombo);
        setAlignment(colorChooserButton);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
        panel.add(rgbValue);
        panel.add(Box.createRigidArea(new Dimension(5,0)));
        panel.add(colorChooserCombo);
        panel.add(Box.createRigidArea(new Dimension(5,0)));
        panel.add(colorChooserButton);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        plug();
    }

    /** */
    private void plug() {
        rgbValue.addActionListener(rgbListener);
        colorChooserCombo.addPropertyChangeListener(pcl);
        colorChooserButton.addActionListener(buttonListener);
    }

    /** for TextField */
    private ActionListener rgbListener = ev -> {
        try {
            setAsText(getAsText());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(panel.getParent(), e.toString());
            setValue(null);
        }
    };

    /** for SmallColorChooserButton */
    private PropertyChangeListener pcl = ev -> {
        if ("color".equals(ev.getPropertyName())) {
            Color color = (Color) ev.getNewValue();
            setValue(color);
        }
    };

    /** for Button */
    private ActionListener buttonListener = ev -> {
        Color oldColor = (Color) getValue();
        Color color = JColorChooser.showDialog(panel.getParent(),
                                               "Color Chooser",
                                               oldColor);
        if (oldColor != color) {
            setValue(color);
            colorChooserCombo.setMostRecentColor(color);
        }
    };

    // PropertyEditorSupport --------------------------------------------------

    /** */
    public boolean isPaintable() {
        return true;
    }

    /**
     * Paints a representation of the value into a given area of screen.
     */
    public void paintValue(Graphics g, Rectangle rect) {
        Color oldColor = g.getColor();
        g.setColor(Color.black);
        g.drawRect(rect.x, rect.y, rect.width - 3, rect.height - 3);
        g.setColor((Color) getValue());
        g.fillRect(rect.x + 1, rect.y + 1, rect.width - 4, rect.height - 4);
        g.setColor(oldColor);
    }

    /** */
    public String getJavaInitializationString() {
        return "new java.awt.Color(" + getAsText() + ")";
    }

    /** */
    public String getAsText() {
        return rgbValue.getText();
    }

    /** */
    public void setAsText(String s) throws IllegalArgumentException {
        int c1 = s.indexOf(',');
        int c2 = s.indexOf(',', c1 + 1);
        if (c1 < 0 || c2 < 0) {
            // Invalid string.
            throw new IllegalArgumentException(s);
        }
        try {
            int r = Integer.parseInt(s.substring(0, c1));
            int g = Integer.parseInt(s.substring(c1 + 1, c2));
            int b = Integer.parseInt(s.substring(c2 + 1));
            setValue(new Color(r, g, b));
        } catch (Exception e) {
            throw new IllegalArgumentException(s);
        }
    }

    /** */
    public void setValue(Object value) {
        super.setValue(value);
Debug.println(Level.FINER, value);
Debug.printStackTrace(Level.FINER, new Exception());
        Color color = (Color) value;
        if (color == null) {
            rgbValue.setText("                  ");
            colorChooserCombo.setBackground(panel.getBackground());
            return;
        }
        // set the combo rect foreground color
        // and the textfield to the rgb value
        rgbValue.setText("" + color.getRed  () + "," +
                              color.getGreen() + "," +
                              color.getBlue ());
        colorChooserCombo.setBackground(color);
    }

    //-------------------------------------------------------------------------

    // for testing

    /** custom combolike rect button */
    private static class SmallColorChooserButton extends JButton {
        private SmallColorChooserPopupMenu popup;
        private Icon comboIcon = new MetalComboBoxIcon();

        /** */
        public SmallColorChooserButton() {
            super("");

            popup = new SmallColorChooserPopupMenu();
            popup.addPropertyChangeListener(pcl);

            this.addMouseListener(popupListener);
        }

        /** */
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Insets insets = getInsets();

            int width = getWidth() - (insets.left + insets.right);
            int height = getHeight() - (insets.top + insets.bottom);

            if (height <= 0 || width <= 0) {
                return;
            }

            int left = insets.left;
            int top = insets.top;
            int right = left + (width - 1);
            int bottom = top + (height - 1);

//          int iconWidth = 0;
            int iconLeft = right;

            // Paint the icon
            if (comboIcon != null) {
//              iconWidth = comboIcon.getIconWidth();
                int iconHeight = comboIcon.getIconHeight();
                int iconTop = 0;
                iconTop = (top + ((bottom - top) / 2)) - (iconHeight / 2);
                comboIcon.paintIcon(this, g, iconLeft, iconTop);
            }
        }

        /** TODO */
        public void setMostRecentColor(Color color) {
            popup.setMostRecentColor(color);
        }

        /** invoke easy color chooser */
        private MouseListener popupListener = new MouseAdapter() {
            public void mouseReleased(MouseEvent ev) {
                // bring up ChooserComboPopup
                // bring it up at the component height location!
                JComponent c = (JComponent) ev.getComponent();
                popup.show(c, 0, 0);
            }
        };

        /** */
        private PropertyChangeListener pcl = ev -> {
            if ("color".equals(ev.getPropertyName())) {
                Color oldColor = (Color) ev.getOldValue();
                Color color = (Color) ev.getNewValue();
                firePropertyChange("color", oldColor, color);
            }
        };
    }
}

/* */
