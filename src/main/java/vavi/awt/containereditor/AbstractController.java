/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.containereditor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;


/**
 * This is the controller.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020529 nsano initial version <br>
 *          0.01 020611 nsano change package <br>
 */
public abstract class AbstractController extends JComponent implements LocatableController {

    /** Whether it is selected */
    private boolean isSelected = false;

    /** */
    protected Component view;

    /**
     * Construct the controller.
     */
    public AbstractController(Component view) {
        this.view = view;
//      this.setDoubleBuffered(true);
    }

    /**
     * Set the view.
     *
     * @param view a view component
     */
    @Override
    public void setView(Component view) {
        this.view = view;
    }

    /**
     * Get the view.
     */
    @Override
    public Component getView() {
        return view;
    }

    /**
     * Sets the selection state.
     */
    @Override
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        repaint();
    }

    /**
     * Returns the selection state.
     */
    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setLocation(Point location) {
        super.setLocation(location);
        view.setLocation(location);
    }

    @Override
    public Point getLocation() {
        return view.getLocation();
    }

    @Override
    public void setSize(Dimension size) {
        super.setSize(size);
        view.setSize(size);
    }

    @Override
    public Dimension getSize() {
        return view.getSize();
    }

    @Override
    public void setBounds(Rectangle bounds) {
        super.setBounds(bounds);
        view.setBounds(bounds);
    }

    @Override
    public Rectangle getBounds() {
        return view.getBounds();
    }
}
