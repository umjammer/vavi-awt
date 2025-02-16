/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.colorchooser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;

import vavi.util.Debug;

import static vavi.swing.colorchooser.MainSwatchPanel.colors;


/**
 * Modified from the standard color swatch chooser.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. A future release of Swing will provide support for
 * long term persistence.
 *
 * @author Steve Wilson
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 1.14 980828 original version <br>
 *          1.20 020518 nsano refine <br>
 *          1.21 020618 nsano fix firePropertyChange <br>
 */
public class SwatchChooserPanel extends AbstractColorChooserPanel implements Serializable {

    private SwatchPanel swatchPanel;

    private RecentSwatchPanel recentSwatchPanel;

    private static final String recentStr = UIManager.getString("ColorChooser.swatchesRecentText");

    @Override
    public String getDisplayName() {
        return UIManager.getString("ColorChooser.swatchesNameText");
    }

    @Override
    public Icon getSmallDisplayIcon() {
        return null;
    }

    @Override
    public Icon getLargeDisplayIcon() {
        return null;
    }

    /**
     * The background color, foreground color, and font are already set to the
     * defaults from the defaults table before this method is called.
     */
    @Override
    public void installChooserPanel(JColorChooser enclosingChooser) {
        super.installChooserPanel(enclosingChooser);
    }

    @Override
    protected void buildChooser() {

        JPanel superHolder = new JPanel();
        superHolder.setLayout(new BoxLayout(superHolder, BoxLayout.Y_AXIS));

        swatchPanel = new MainSwatchPanel();
        swatchPanel.getAccessibleContext().setAccessibleName(getDisplayName());

        recentSwatchPanel = new RecentSwatchPanel();
        recentSwatchPanel.getAccessibleContext().setAccessibleName(recentStr);

        swatchPanel.addMouseListener(mainSwatchListener);
        recentSwatchPanel.addMouseListener(recentSwatchListener);

        JPanel mainHolder = new JPanel(new BorderLayout());
        Border border = new CompoundBorder(new LineBorder(Color.black), new LineBorder(Color.white));
        mainHolder.setBorder(border);
        mainHolder.add(swatchPanel, BorderLayout.CENTER);

        JPanel recentHolder = new JPanel(new BorderLayout());
        recentHolder.setBorder(border);
        recentHolder.add(recentSwatchPanel, BorderLayout.CENTER);

        superHolder.add(recentHolder);
        superHolder.add(Box.createRigidArea(new Dimension(0, 3)));
        superHolder.add(mainHolder);

        add(superHolder);
    }

    @Override
    public void uninstallChooserPanel(JColorChooser enclosingChooser) {
        super.uninstallChooserPanel(enclosingChooser);

        swatchPanel.removeMouseListener(mainSwatchListener);
        swatchPanel = null;
        mainSwatchListener = null;

        removeAll(); // strip out all the sub-components
    }

    @Override
    public void updateChooser() {
    }

    /** TODO */
    public void setMostRecentColor(Color color) {
        recentSwatchPanel.setMostRecentColor(color);
    }

    /** TODO */
    public Color getMostRecentColor() {
        return recentSwatchPanel.getMostRecentColor();
    }

    /** */
    private final MouseListener recentSwatchListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent ev) {
            // Color oldColor = recentSwatchPanel.getMostRecentColor();
            Color color = recentSwatchPanel.getColorForLocation(ev.getX(), ev.getY());
            firePropertyChange("color", null /* oldColor */, color);
        }
    };

    /** */
    private MouseListener mainSwatchListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent ev) {
            // Color oldColor = recentSwatchPanel.getMostRecentColor();
            Color color = swatchPanel.getColorForLocation(ev.getX(), ev.getY());
            firePropertyChange("color", null /* oldColor */, color);
            recentSwatchPanel.setMostRecentColor(color);
        }
    };
}

/**
 * base class
 */
class SwatchPanel extends JPanel {
    protected Dimension swatchSize = new Dimension(13, 13);

    protected Dimension numSwatches;

    protected Dimension gap;

    /** */
    public SwatchPanel() {
        initValues();
        initColors();
        setToolTipText(""); // register for events
        setOpaque(true);
        setBackground(Color.gray);
        setRequestFocusEnabled(false);
    }

    @Override
    public boolean isFocusable() {
        return false;
    }

    /** */
    protected void initValues() {
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        for (int row = 0; row < numSwatches.height; row++) {
            for (int column = 0; column < numSwatches.width; column++) {
                g.setColor(getColorForCell(column, row));
                int x = column * (swatchSize.width + gap.width);
                int y = row * (swatchSize.height + gap.height);
                g.fillRect(x, y, swatchSize.width, swatchSize.height);
                g.setColor(Color.black);
                g.drawLine(x + swatchSize.width - 1, y, x + swatchSize.width - 1, y + swatchSize.height - 1);
                g.drawLine(x, y + swatchSize.height - 1, x + swatchSize.width - 1, y + swatchSize.width - 1);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        int x = numSwatches.width * (swatchSize.width + gap.width) - 1;
        int y = numSwatches.height * (swatchSize.height + gap.height) - 1;
        return new Dimension(x, y);
    }

    /** */
    protected void initColors() {
    }

    @Override
    public String getToolTipText(MouseEvent e) {
        Color color = getColorForLocation(e.getX(), e.getY());
        return color.getRed() + ", " + color.getGreen() + ", " + color.getBlue();
    }

    /** */
    public Color getColorForLocation(int x, int y) {
        int column = x / (swatchSize.width + gap.width);
        int row = y / (swatchSize.height + gap.height);
        return getColorForCell(column, row);
    }

    /** */
    private Color getColorForCell(int column, int row) {
Debug.printf("@@@ OVERFLOW: %d, %d", column, row);
if (colors.length <= (row * numSwatches.width) + column) {
    return Color.black;
}
        return colors[(row * numSwatches.width) + column];
        // (STEVE) - change data orientation here
    }
}

/**
 * show recent colors
 */
class RecentSwatchPanel extends SwatchPanel {
    @Override
    protected void initValues() {
        // swatchSize = UIManager.getDimension("ColorChooser.swatchesRecentSwatchSize");
        swatchSize = new Dimension(13, 13);
        numSwatches = new Dimension(6, 1);
        gap = new Dimension(1, 1);
    }

    @Override
    protected void initColors() {
        Color defaultRecentColor = UIManager.getColor("ColorChooser.swatchesDefaultRecentColor");
        int numColors = numSwatches.width * numSwatches.height;
Debug.println("@@@ numColors: " + numColors);
        colors = new Color[numColors];
        for (int i = 0; i < numColors; i++) {
            colors[i] = defaultRecentColor;
        }
    }

    /** */
    public void setMostRecentColor(Color c) {
        if (!colors[0].equals(c)) { // TODO NPE
            System.arraycopy(colors, 0, colors, 1, colors.length - 1);
            colors[0] = c;
            repaint();
        }
    }

    /** */
    public Color getMostRecentColor() {
        return colors[0];
    }
}

/**
 * show sample colors
 */
class MainSwatchPanel extends SwatchPanel {
    @Override
    protected void initValues() {
        // swatchSize = UIManager.getDimension("ColorChooser.swatchesSwatchSize");
        swatchSize = new Dimension(13, 13);
        // numSwatches = new Dimension(31, 10);
        numSwatches = new Dimension(6, 6);
        gap = new Dimension(1, 1);
    }

    /** */
    protected static Color[] colors;

    /** */
    private static final int[] rawValues = {
        255, 255, 255, 192, 192, 192, 128, 128, 128, 64, 64, 64, 0, 0, 0, 255, 0, 0, 100, 100, 100, 255, 175, 175, 255, 200, 0, 255, 255, 0, 0, 255, 0, 255, 0, 255, 0, 255, 255, 0, 0, 255, // repeat
                                                                                                                                                                                                // here
        255, 255, 255, 192, 192, 192, 128, 128, 128, 64, 64, 64, 0, 0, 0, 255, 0, 0, 100, 100, 100, 255, 175, 175, 255, 200, 0, 255, 255, 0, 0, 255, 0, 255, 0, 255, 0, 255, 255, 0, 0, 255, // repeat
                                                                                                                                                                                                // here
        100, 100, 100, 255, 175, 175, 255, 200, 0, 255, 255, 0, 0, 255, 0, 255, 0, 255, 0, 255, 255, 0, 0, 255,
    };

    /* */
    static {
        int numColors = rawValues.length / 3;

        colors = new Color[numColors];
        for (int i = 0; i < numColors; i++) {
            colors[i] = new Color(rawValues[(i * 3)], rawValues[(i * 3) + 1], rawValues[(i * 3) + 2]);
        }
    }
}
