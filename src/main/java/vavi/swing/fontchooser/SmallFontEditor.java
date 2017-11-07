/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.fontchooser;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;


/**
 * SmallFontEditor
 *
 * @depends /vavi/swing/resource${I18N}.properties
 *
 * @event send PropertyChangeEvent("font", Font, Font)
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020517 nsano initial version <br>
 */
public class SmallFontEditor extends JComponent implements FontEditor {

    /** リソースバンドル */
    private static final ResourceBundle rb =
        ResourceBundle.getBundle("vavi.swing.resource", Locale.getDefault());

    private static final int BUTTON_WIDTH = 20;
    private static final int BUTTON_HEIGHT = 30;

    private static final Dimension buttonSize =
        new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);

    private String fonts[];
    private static final int[] pointSizes =
        { 3, 5, 8, 10, 12, 14, 18, 24, 36, 48 };

    private int selectedStyle = Font.PLAIN;

    // Controls
    private JComboBox<String> familyNameCombo;
    private JComboBox<String> fontSizeCombo;

    private JToggleButton pButton, iButton, bButton;
    private FontDisplay iDisplay, pDisplay, bDisplay;

    /** */
    private JLabel labelDisplay;

    /** */
    private Font font;

    /** */
    public SmallFontEditor() {
        fonts = GraphicsEnvironment
            .getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        pDisplay = new FontDisplay(Font.PLAIN);
        pButton = new JToggleButton(pDisplay);
        pButton.setToolTipText(rb.getString("jFontChooser.label.plain.text"));

        iDisplay = new FontDisplay(Font.ITALIC);
        iButton = new JToggleButton(iDisplay);
        iButton.setToolTipText(rb.getString("jFontChooser.label.italic.text"));

        bDisplay = new FontDisplay(Font.BOLD);
        bButton = new JToggleButton(bDisplay);
        bButton.setToolTipText(rb.getString("jFontChooser.label.bold.text"));

        initializeButton(pButton);
        initializeButton(iButton);
        initializeButton(bButton);

        ButtonGroup group = new ButtonGroup();
        group.add(pButton);
        group.add(iButton);
        group.add(bButton);

        labelDisplay = new JLabel(fonts[0]);
        labelDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelDisplay.setPreferredSize(new Dimension(250,30));
        labelDisplay.setMinimumSize(new Dimension(250,30));

        initializeComboBoxes();

        // Assemble the panel.
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p,BoxLayout.X_AXIS));
        p.add(familyNameCombo);
        p.add(Box.createRigidArea(new Dimension(5,0)));
        p.add(fontSizeCombo);
        p.add(Box.createRigidArea(new Dimension(5,0)));

        p.add(pButton);
        p.add(iButton);
        p.add(bButton);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(p);
        this.add(labelDisplay);

        plugActionListener();
    }

    /** */
    private void initializeButton(JToggleButton b) {
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);

        b.setPreferredSize(buttonSize);
        b.setMaximumSize(buttonSize);
        b.setMinimumSize(buttonSize);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setAlignmentY(Component.CENTER_ALIGNMENT);
    }

    /**
     * Creates the ComboBoxes. The fons and point sizes must be initialized.
     */
    private void initializeComboBoxes() {
        final Dimension MEDIUM_DIMENSION = new Dimension(120, 20);
        final Dimension SMALL_DIMENSION = new Dimension(50, 20);
        // ComboBoxes
        familyNameCombo = new JComboBox<>(fonts);
        fontSizeCombo = new JComboBox<>();

        familyNameCombo.setPreferredSize(MEDIUM_DIMENSION);
        familyNameCombo.setMinimumSize(MEDIUM_DIMENSION);
        familyNameCombo.setMaximumSize(MEDIUM_DIMENSION);
        familyNameCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        familyNameCombo.setAlignmentY(Component.CENTER_ALIGNMENT);

        for (int i = 0; i < pointSizes.length; i++) {
            fontSizeCombo.addItem("" + pointSizes[i]);
        }

        fontSizeCombo.setPreferredSize(SMALL_DIMENSION);
        fontSizeCombo.setMaximumSize(SMALL_DIMENSION);
        fontSizeCombo.setMinimumSize(SMALL_DIMENSION);
        fontSizeCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        fontSizeCombo.setAlignmentY(Component.CENTER_ALIGNMENT);
    }

    /** */
    private void plugActionListener() {
        pButton.addActionListener(al);
        iButton.addActionListener(al);
        bButton.addActionListener(al);
        familyNameCombo.addActionListener(al);
        fontSizeCombo.addActionListener(al);
    }

    /** */
//      private void unplugActionListener() {
//          pButton.removeActionListener(al);
//          iButton.removeActionListener(al);
//          bButton.removeActionListener(al);
//          familyNameCombo.removeActionListener(al);
//          fontSizeCombo.removeActionListener(al);
//      }

    /**
     * ActionListener handler for all component events.
     */
    private ActionListener al = new  ActionListener() {
        public void actionPerformed(ActionEvent ev)  {
            Object obj = ev.getSource();
            if (obj instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) obj;
                if (button == pButton) {
                    selectedStyle = Font.PLAIN;
                } else if (button == iButton) {
                    selectedStyle = Font.ITALIC;
                } else if (button == bButton) {
                    selectedStyle = Font.BOLD;
                }

                String family = (String) familyNameCombo.getSelectedItem();
                int size = pointSizes[fontSizeCombo.getSelectedIndex()];

                Font oldFont = font;
                setSelectedFont(new Font(family, selectedStyle, size));
                firePropertyChange("font", oldFont, font);
            } else if (obj instanceof JComboBox) {
                String family = (String) familyNameCombo.getSelectedItem();
                int size = pointSizes[fontSizeCombo.getSelectedIndex()];

                Font oldFont = font;
                setSelectedFont(new Font(family, selectedStyle, size));
                firePropertyChange("font", oldFont, font);
            }
        }
    };

    //-------------------------------------------------------------------------

    /**
     * Implementation of a Icon button.
     */
    private static class FontDisplay implements Icon {
        private Font font;

        private int style = Font.PLAIN;
        private int size = 24;

        private String label = "A";

        private int iconWidth = 20;
        private int iconHeight = 30;

        public FontDisplay(int style) {
            this.style = style;
            this.font = new Font("Dialog", this.style, this.size);
        }

        @SuppressWarnings("unused")
        public FontDisplay() {
            this.font = new Font("Dialog", this.style, this.size);
        }

        public void setFamily(String family) {
            this.font = new Font(family, this.style, this.size);
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            JComponent component = (JComponent) c;

            Font oldFont = g.getFont();
            g.setFont(this.font);
            if (component instanceof JToggleButton) {
                AbstractButton b= (AbstractButton) component;
                ButtonModel model = b.getModel();
                if (model.isPressed() || model.isSelected()) {
                    g.setColor(Color.black); // xxx: foreground
                } else {
                    g.setColor(Color.gray); // xxx: foreground light
                }
            }
            g.drawString(label, x, (y + iconHeight) - 7);
            g.setFont(oldFont);
        }

        public int getIconWidth() {
            return iconWidth;
        }

        public int getIconHeight() {
            return iconHeight;
        }
    }

    //-------------------------------------------------------------------------

    /** */
    public Component getFontEditorComponent() {
        return this;
    }

    /**
     * Reconfigure the controls to reflect the current font.
     */
    public void setSelectedFont(Font font) {
        this.font = font;

//Debug.println(font);
        if (font == null)
            return;

        for (int i = 0; i < fonts.length; i++) {
            if (fonts[i].equalsIgnoreCase(font.getName())) {
                familyNameCombo.setSelectedIndex(i);
                break;
            }
        }

        for (int i = 0; i < pointSizes.length; i++) {
            if (font.getSize() <= pointSizes[i]) {
                fontSizeCombo.setSelectedIndex(i);
                break;
            }
        }

        selectedStyle = font.getStyle();
        String style = "";
        switch (selectedStyle) {
        case Font.PLAIN:
            pButton.setSelected(true);
            style = rb.getString("jFontChooser.style.plain");
            break;
        case Font.ITALIC:
            iButton.setSelected(true);
            style = rb.getString("jFontChooser.style.italic");
            break;
        case Font.BOLD:
            bButton.setSelected(true);
            style = rb.getString("jFontChooser.style.bold");
            break;
        }
        String family = font.getFamily();

        iDisplay.setFamily(family);
        pDisplay.setFamily(family);
        bDisplay.setFamily(family);

        labelDisplay.setFont(font);
        labelDisplay.setText(family + ", " + style + ", " + font.getSize());

        revalidate();
        repaint();
    }

    /** */
    public Font getSelectedFont() {
        return font;
    }
}

/* */
