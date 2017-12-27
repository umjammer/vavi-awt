/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.colorchooser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;


/**
 * SmallColorChooserPopupMenu.
 *
 * @event PropertyChangeEvent("color", null, Color)
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version original version <br>
 *          1.00 020518 nsano refine <br>
 *          1.01 020618 nsano fix firePropertyChange <br>
 */
public class SmallColorChooserPopupMenu extends JPopupMenu {

    /** */
    private SwatchChooserPanel swatchPanel;

    /** */
    public SmallColorChooserPopupMenu() {

        JPanel p = new JPanel(new BorderLayout());

        swatchPanel = new SwatchChooserPanel();
        swatchPanel.buildChooser();
        swatchPanel.addPropertyChangeListener(pcl);
        p.add(swatchPanel, BorderLayout.NORTH);

        JButton b = new JButton("Other ...");
        b.addActionListener(al);
        p.add(b, BorderLayout.SOUTH); // , BorderLayout.SOUTH);

        this.add(p);
        this.addMouseListener(popupListener);
    }

    /** TODO */
    public void setMostRecentColor(Color color) {
        swatchPanel.setMostRecentColor(color);
    }

    /** for Button */
    private ActionListener al = new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            Color oldColor = swatchPanel.getMostRecentColor();
            Color color = JColorChooser.showDialog(getParent(), "Color Chooser", oldColor);
            firePropertyChange("color", oldColor, color);
            // TODO update the recentSwatch
            setVisible(false);
        }
    };

    /** for PopupMenu */
    private MouseListener popupListener = new MouseAdapter() {
        public void mouseReleased(MouseEvent ev) {
            setVisible(false);
        }
    };

    /** for SwatchChooserPanel */
    private PropertyChangeListener pcl = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent ev) {
            if ("color".equals(ev.getPropertyName())) {
                Color oldColor = (Color) ev.getOldValue();
                Color color = (Color) ev.getNewValue();
                firePropertyChange("color", oldColor, color);
                setVisible(false);
            }
        }
    };
}

/* */
