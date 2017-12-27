/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.plaf.basic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.IllegalComponentStateException;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ActionMap;
import javax.swing.BoundedRangeModel;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;

import vavi.swing.JVolume;
import vavi.swing.plaf.VolumeUI;


/**
 * A Basic L&F implementation of VolumeUI.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020507 nsano initial version <br>
 */
public class BasicVolumeUI extends VolumeUI {
    public static final int POSITIVE_SCROLL = +1;
    public static final int NEGATIVE_SCROLL = -1;
    public static final int MIN_SCROLL = -2;
    public static final int MAX_SCROLL = +2;

    protected Timer scrollTimer;
    protected JVolume volume;

    protected Insets focusInsets = null;
    protected Insets insetCache = null;
    protected Rectangle focusRect = null;
    protected Rectangle contentRect = null;

    private static final Dimension PREFERRED_SIZE = new Dimension(36, 36);
    private static final Dimension MINIMUM_SIZE = new Dimension(21, 21);

    private transient boolean isDragging;

    protected ChangeListener changeListener;
    protected ComponentListener componentListener;
    protected FocusListener focusListener;
    protected RollListener scrollListener;
    protected PropertyChangeListener propertyChangeListener;

    // Colors
    private Color shadowColor;
    private Color highlightColor;
    private Color focusColor;

    protected Color getShadowColor() {
        return shadowColor;
    }

    protected Color getHighlightColor() {
        return highlightColor;
    }

    protected Color getFocusColor() {
        return focusColor;
    }

    ///////////////////////////////////////////////////////////////////////////
    // ComponentUI Interface Implementation methods
    ///////////////////////////////////////////////////////////////////////////

    public static ComponentUI createUI(JComponent b) {
        return new BasicVolumeUI((JVolume) b);
    }

    public BasicVolumeUI(JVolume b) {
    }

    public void installUI(JComponent c) {
        volume = (JVolume) c;

        volume.setEnabled(volume.isEnabled());
        volume.setOpaque(true);

        isDragging = false;
        changeListener = createChangeListener(volume);
        componentListener = createComponentListener(volume);
        focusListener = createFocusListener(volume);
        scrollListener = createRollListener(volume);
        propertyChangeListener = createPropertyChangeListener(volume);

        installDefaults(volume);
        installListeners(volume);
        installKeyboardActions(volume);

        scrollTimer = new Timer(100, scrollListener);
        scrollTimer.setInitialDelay(300);

        insetCache = volume.getInsets();
        focusRect = new Rectangle();
        contentRect = new Rectangle();

        calculateGeometry(); // This figures out where the labels, ticks, track, and thumb are.
    }

    public void uninstallUI(JComponent c) {
        if (c != volume) {
            throw new IllegalComponentStateException(
                this + " was asked to deinstall() "
                + c + " when it only knows about "
                + volume + ".");
        }

        LookAndFeel.uninstallBorder(volume);

        scrollTimer.stop();
        scrollTimer = null;

        uninstallListeners(volume);
        uninstallKeyboardActions(volume);

        focusInsets = null;
        insetCache = null;
        focusRect = null;
        contentRect = null;
        changeListener = null;
        componentListener = null;
        focusListener = null;
        scrollListener = null;
        propertyChangeListener = null;
        volume = null;
    }

    protected void installDefaults(JVolume volume) {
        LookAndFeel.installBorder(volume, "Volume.border");
        LookAndFeel.installColors(volume, "Volume.background", "Volume.foreground");
        highlightColor = UIManager.getColor("Volume.highlight");

        shadowColor = UIManager.getColor("Volume.shadow");
        focusColor = UIManager.getColor("Volume.focus");

        focusInsets = (Insets) UIManager.get("Volume.focusInsets");
    }

    protected ChangeListener createChangeListener(JVolume volume) {
        return new ChangeHandler();
    }

    protected ComponentListener createComponentListener(JVolume volume) {
        return new ComponentHandler();
    }

    protected FocusListener createFocusListener(JVolume volume) {
        return new FocusHandler();
    }

    protected RollListener createRollListener(JVolume volume) {
        return new RollListener();
    }

    protected PropertyChangeListener createPropertyChangeListener(JVolume volume) {
        return new PropertyChangeHandler();
    }

    protected void installListeners(JVolume volume) {
        volume.addFocusListener(focusListener);
        volume.addComponentListener(componentListener);
        volume.addPropertyChangeListener(propertyChangeListener);
        volume.getModel().addChangeListener(changeListener);
    }

    protected void uninstallListeners(JVolume volume) {
        volume.removeFocusListener(focusListener);
        volume.removeComponentListener(componentListener);
        volume.removePropertyChangeListener(propertyChangeListener);
        volume.getModel().removeChangeListener(changeListener);
    }

    protected void installKeyboardActions(JVolume volume) {
        InputMap km = getInputMap(JComponent.WHEN_FOCUSED);

        SwingUtilities.replaceUIInputMap(volume, JComponent.WHEN_FOCUSED, km);
        ActionMap am = getActionMap();

        SwingUtilities.replaceUIActionMap(volume, am);
    }

    InputMap getInputMap(int condition) {
        if (condition == JComponent.WHEN_FOCUSED) {
            InputMap keyMap = (InputMap) UIManager.get("Volume.focusInputMap");
            return keyMap;
        }
        return null;
    }

    ActionMap getActionMap() {
        ActionMap map = (ActionMap) UIManager.get("Volume.actionMap");
        return map;
    }

    protected void uninstallKeyboardActions(JVolume volume) {
        SwingUtilities.replaceUIActionMap(volume, null);
        SwingUtilities.replaceUIInputMap(volume, JComponent.WHEN_FOCUSED,
                                         null);
    }

    public Dimension getPreferredSize(JComponent c) {
        recalculateIfInsetsChanged();
        Dimension d = PREFERRED_SIZE;
        d.width = insetCache.left + insetCache.right;
        d.width += focusInsets.left + focusInsets.right;
        d.height = insetCache.top + insetCache.bottom;
        d.height += focusInsets.top + focusInsets.bottom;

        return d;
    }

    public Dimension getMinimumSize(JComponent c) {
        recalculateIfInsetsChanged();
        Dimension d = MINIMUM_SIZE;
        d.width = insetCache.left + insetCache.right;
        d.width += focusInsets.left + focusInsets.right;
        d.height = insetCache.top + insetCache.bottom;
        d.height += focusInsets.top + focusInsets.bottom;

        return d;
    }

    public Dimension getMaximumSize(JComponent c) {
        Dimension d = getPreferredSize(c);
        d.width = Short.MAX_VALUE;
        d.height = Short.MAX_VALUE;

        return d;
    }

    protected void calculateGeometry() {
        calculateFocusRect();
        calculateContentRect();
    }

    protected void calculateFocusRect() {
        focusRect.x = insetCache.left;
        focusRect.y = insetCache.top;
        focusRect.width =
            volume.getWidth() - (insetCache.left + insetCache.right);
        focusRect.height =
            volume.getHeight() - (insetCache.top + insetCache.bottom);
    }

    protected void calculateContentRect() {
        contentRect.x = focusRect.x + focusInsets.left;
        contentRect.y = focusRect.y + focusInsets.top;
        contentRect.width =
            focusRect.width - (focusInsets.left + focusInsets.right);
        contentRect.height =
            focusRect.height - (focusInsets.top + focusInsets.bottom);
    }

    public class PropertyChangeHandler implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();
            if (propertyName.equals("model")) {
                ((BoundedRangeModel) e.getOldValue()).removeChangeListener(changeListener);
                ((BoundedRangeModel) e.getNewValue()).addChangeListener(changeListener);
                volume.repaint();
            }
        }
    }

    public void paint(Graphics g, JComponent c) {
        recalculateIfInsetsChanged();
        Rectangle clip = g.getClipBounds();

        if (volume.hasFocus() && clip.intersects(focusRect)) {
            paintFocus(g);
        }
    }

    protected void recalculateIfInsetsChanged() {
        Insets newInsets = volume.getInsets();
        if (!newInsets.equals(insetCache)) {
            insetCache = newInsets;
            calculateGeometry();
        }
    }

    public void paintFocus(Graphics g) {
        g.setColor(getFocusColor());

        BasicGraphicsUtils.drawDashedRect(g, focusRect.x, focusRect.y,
                                          focusRect.width, focusRect.height);
    }

    /**
     * Returns a value give a y position.
     */
    public int valueForYPosition(int yPos) {
        int value = 0;
//      final int minValue = volume.getMinimum();
//      final int maxValue = volume.getMaximum();

        return value;
    }

    /**
     * Returns a value give an x position.
     */
    public int valueForXPosition(int xPos) {
        int value = 0;
//      final int minValue = volume.getMinimum();
//      final int maxValue = volume.getMaximum();


        return value;
    }

    /////////////////////////////////////////////////////////////////////////
    /// Model Listener Class
    /////////////////////////////////////////////////////////////////////////

    /**
     * Data model listener.
     *
     * This inner class is marked &quot;public&quot; due to a compiler bug.
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of <Foo>.
     */
    public class ChangeHandler implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            if (!isDragging) {
                volume.repaint();
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////
    /// Track Listener Class
    /////////////////////////////////////////////////////////////////////////

    /**
     * Track mouse movements.
     *
     * This inner class is marked &quot;public&quot; due to a compiler bug.
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of <Foo>.
     */
    public class TrackListener extends MouseInputAdapter {
        protected transient int offset;
        protected transient int currentMouseX, currentMouseY;

        public void mouseReleased(MouseEvent e) {
            if (!volume.isEnabled()) {
                return;
            }

            offset = 0;
            scrollTimer.stop();

            // This is the way we have to determine snap-to-ticks.  It's hard to explain
            // but since ChangeEvents don't give us any idea what has changed we don't
            // have a way to stop the thumb bounds from being recalculated.  Recalculating
            // the thumb bounds moves the thumb over the current value (i.e., snapping
            // to the ticks).
            volume.setValueIsAdjusting(false);
            isDragging = false;

            volume.repaint();
        }

        /**
        * If the mouse is pressed above the "thumb" component
        * then reduce the scrollbars value by one page ("page up"),
        * otherwise increase it by one page.  If there is no
        * thumb then page up if the mouse is in the upper half
        * of the track.
        */
        public void mousePressed(MouseEvent e) {

            if (!volume.isEnabled()) {
                return;
            }

            currentMouseX = e.getX();
            currentMouseY = e.getY();

            if (volume.isRequestFocusEnabled()) {
                volume.requestFocus();
            }

            // Clicked in the Thumb area?
            isDragging = false;
            volume.setValueIsAdjusting(true);

//          Dimension sbSize = volume.getSize();
//          int direction = POSITIVE_SCROLL;
        }

        public boolean shouldScroll(int direction) {
            if (direction > 0 && volume.getValue() + volume.getExtent() >= volume.getMaximum()) {
                return false;
            } else if (direction < 0 && volume.getValue() <= volume.getMinimum()) {
                return false;
            }

            return true;
        }

        /**
        * Set the models value to the position of the top/left
        * of the thumb relative to the origin of the track.
        */
        public void mouseDragged(MouseEvent e) {
//          BasicScrollBarUI ui;

            if (!volume.isEnabled()) {
                return;
            }

            currentMouseX = e.getX();
            currentMouseY = e.getY();

            if (!isDragging)
                return;

            volume.setValueIsAdjusting(true);

            volume.setValue(currentMouseX);
        }

        public void mouseMoved(MouseEvent e) {}
    }

    /**
     * Scroll-event listener.
     *
     * This inner class is marked &quot;public&quot; due to a compiler bug.
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of <Foo>.
     */
    protected class RollListener implements ActionListener {
        // changed this class to public to avoid bogus IllegalAccessException bug i
        // InternetExplorer browser.  It was protected.  Work around for 4109432
        int direction = POSITIVE_SCROLL;

        public RollListener() {
            direction = POSITIVE_SCROLL;
        }

        public RollListener(int dir) {
            direction = dir;
        }

        public void setDirection(int direction) { this.direction = direction; }

        public void actionPerformed(ActionEvent e) {
            ((Timer) e.getSource()).stop();
        }
    };

    /**
     * Listener for resizing events.
     * <p>
     * This inner class is marked &quot;public&quot; due to a compiler bug.
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of <Foo>.
     */
    public class ComponentHandler extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            calculateGeometry();
            volume.repaint();
        }
    };

    /**
     * Focus-change listener.
     * <p>
     * This inner class is marked &quot;public&quot; due to a compiler bug.
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of <Foo>.
     */
    public class FocusHandler implements FocusListener {
        public void focusGained(FocusEvent e) {
            volume.repaint();
        }
        public void focusLost(FocusEvent e) {
            volume.repaint();
        }
    };
}

/* */
