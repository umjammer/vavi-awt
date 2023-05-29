/*
 * Copyright (c) 1997 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

import vavi.awt.dnd.Droppable;
import vavi.util.Debug;

import static javax.swing.SwingConstants.BOTTOM;
import static javax.swing.SwingConstants.CENTER;
import static javax.swing.SwingConstants.LEFT;
import static javax.swing.SwingConstants.RIGHT;
import static javax.swing.SwingConstants.TOP;


/**
 * BaseImageComponent.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 220920 nsano initial version <br>
 */
@SuppressWarnings("unchecked")
public abstract class BaseImageComponent<T extends Image> extends JComponent {

    /** image to render */
    private T image;

    /** image width for convenience */
    protected int iw;
    /** image height for convenience */
    protected int ih;

    /** fitting image or not */
    private boolean fit = true;

    // alignment for image
    private int alignmentX = LEFT;
    private int alignmentY = TOP;

    /** rendering hints */
    private Map<?, ?> hints;

    /** Creates an image component. */
    public BaseImageComponent() {
        this(false);
    }

    /**
     * Creates an image component and make it droppable.
     *
     * @param droppable true: dragging a file into this component is enabled.
     *                  when dropping a file, {@link PropertyChangeEvent} named "droppedImage" is fired.
     *                  {@link PropertyChangeEvent#getNewValue()} will be a dropped image.
     */
    public BaseImageComponent(boolean droppable) {
        if (droppable) {
            Droppable.makeComponentSinglePathDroppable(this, p -> {
                try {
                    @SuppressWarnings("unchecked")
                    T image = (T) ImageIO.read(Files.newInputStream(p));
Debug.println(Level.FINE, p + ", " + image);
                    if (image != null) {
                        firePropertyChange("droppedImage", this.image, image);
                        setImage(image);
                        repaint();
                        return true;
                    }
Debug.println(Level.INFO, "unrecognized image: " + p);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                return false;
            });
        }
    }

    /** Sets an image */
    public void setImage(T image) {
        T old = this.image;
        this.image = image;
        firePropertyChange("image", old, image);
    }

    /** Gets the image */
    public T getImage() {
        return this.image;
    }

    /** Sets fitting image or not */
    public void setFit(boolean fit) {
        this.fit = fit;
    }

    /** Gets scale when fit */
    public float getScale() {
        return fit ? fit() : 1;
    }

    /** Sets horizontal alignment for image */
    public void setImageHorizontalAlignment(int alignment) {
        assert Arrays.asList(CENTER, LEFT, RIGHT).contains(alignment) : "wring alignment value: " + alignment;
        this.alignmentX = alignment;
    }
    /** Sets vertical alignment for image */
    public void setImageVerticalAlignment(int alignment) {
        assert Arrays.asList(CENTER, TOP, BOTTOM).contains(alignment) : "wring alignment value: " + alignment;
        this.alignmentY = alignment;
    }

    /** Sets image rendering hints */
    public void setRenderingHints(Map<?, ?> hints) {
        this.hints = hints;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {

            if (hints != null) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHints(hints);
            }

            synchronized (image) {
                if (fit) {
                    Rectangle r = fitBounds();
                    g.drawImage(image, r.x, r.y, r.width, r.height, null);
                } else {
                    g.drawImage(image, 0, 0, null);
                }
            }
        } else {
            g.drawImage(noImage, 0, 0, null);
        }
    }

    /** Gets image bound after scaled */
    private Rectangle fitBounds() {
        float scale = fit();

        int nw = Math.round(iw * scale);
        int nh = Math.round(ih * scale);

        int w = getWidth();
        int h = getHeight();
        int dx = 0;
        int dy = 0;
        switch (alignmentX) {
        default:
        case LEFT:
            break;
        case CENTER:
            dx = Math.round((w - nw) / 2f);
            break;
        case RIGHT:
            dx = w - nw;
            break;
        }
        switch (alignmentY) {
        default:
        case TOP:
            break;
        case CENTER:
            dy = Math.round((h - nh) / 2f);
            break;
        case BOTTOM:
            dy = w - nh;
            break;
        }

        return new Rectangle(dx, dy, nw, nh);
    }

    /** Gets a scale to make image fit this component size keeping aspect ratio */
    private float fit() {
        int w = getWidth();
        int h = getHeight();
        float sw = 1;
        float sh = 1;
        float s;
        if (iw > w || ih > h) {
            // https://stackoverflow.com/a/10245583
            if (iw > w) {
                sw = w / (float) iw;
            }
            if (ih * sw > h) {
                sh = h / (float) ih;
            }
            s = Math.min(sw, sh);
        } else {
            if (w > iw) {
                sw = (float) iw / w;
            }
            if (ih * sw < h) {
                sh = (float) ih / h;
            }
            s = 1 / Math.max(sw, sh);
        }
        return s;
    }

    /**
     * Gets a sub image.
     * @param sx x point of this component (scaled)
     * @param sy y point of this component (scaled)
     * @param sw a sub image width (scaled)
     * @param sh a sub image height (scaled)
     * @return null: when {@link #image} is null
     */
    public T getSubimage(int sx, int sy, int sw, int sh) {
        if (image == null) {
Debug.printf(Level.FINE, "image is null");
            return null;
        }

        Rectangle ir = new Rectangle(0, 0, getWidth(), getHeight());
        Rectangle sr = new Rectangle(sx, sy, sw, sh);
        Rectangle cr = sr.intersection(ir);

Debug.printf(Level.FINE, "total: %d, %d %d, %d", ir.x, ir.y, ir.width, ir.height);
Debug.printf(Level.FINE, "sub: %d, %d %d, %d", sr.x, sr.y, sr.width, sr.height);
Debug.printf(Level.FINE, "crop: %d, %d %d, %d", cr.x, cr.y, cr.width, cr.height);

        int t = image instanceof BufferedImage ? ((BufferedImage) image).getType() : BufferedImage.TYPE_INT_ARGB;
        BufferedImage total = new BufferedImage(getWidth(), getHeight(), t);
        Graphics2D gt = total.createGraphics();
        paintComponent(gt);
        gt.dispose();

        BufferedImage crop = total.getSubimage(cr.x, cr.y, cr.width, cr.height);

        BufferedImage sub = new BufferedImage(sw, sh, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gs = sub.createGraphics();
        gs.setColor(Color.black);
        gs.fillRect(0, 0, sw, sh);

        Point ip = getRelativeIntersectionPoint(ir, sr, cr);
        gs.drawImage(crop, ip.x, ip.y, null);
        gs.dispose();

        return (T) sub;
    }

    /**
     * Returns cr's (x, y) when transform ir coordinate system to sr coordinate system
     * TODO use affine transform?
     */
    private Point getRelativeIntersectionPoint(Rectangle ir, Rectangle sr, Rectangle cr) {
        int ix = 0;
        if (sr.width != cr.width) {
            if (ir.contains(sr.x, sr.y)) { // SE
                ix = 0;
            } else if (ir.contains(sr.x + sr.width, sr.y)) { // SW
                ix = sr.width - cr.width;
            } else if (ir.contains(sr.x, sr.y + sr.height)) { // NE
                ix = 0;
            } else if (ir.contains(sr.x + sr.width, sr.y + sr.height)) { // NW
                ix = sr.width - cr.width;
            }
        }
        int iy = 0;
        if (sr.height != cr.height) {
            if (ir.contains(sr.x, sr.y)) { // SE
                iy = 0;
            } else if (ir.contains(sr.x + sr.width, sr.y)) { // SW
                iy = 0;
            } else if (ir.contains(sr.x, sr.y + sr.height)) { // NE
                iy = sr.height - cr.height;
            } else if (ir.contains(sr.x + sr.width, sr.y + sr.height)) { // NW
                iy = sr.height - cr.height;
            }
        }
        return new Point(ix, iy);
    }

    /** default image */
    private final T noImage;

    /* load default image */
    {
        try {
            noImage = (T) ImageIO.read(BaseImageComponent.class.getResourceAsStream("/noimage.png"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

/* */

