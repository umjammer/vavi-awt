/*
 * http://www.javainthebox.net/laboratory/GUI/TransFrame/TransFrame.html
 */

package vavi.swing;

import java.awt.AWTEvent;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JRootPane;


public class JTransFrame extends JFrame implements Translucentable {
    private BufferedImage backImage;
    private ScreenCapture capture;
    private final static Insets NO_INSETS = new Insets(0, 0, 0, 0);

    public JTransFrame() {
        init();
    }

    public JTransFrame(String title) {
        super(title);
        init();
    }

    public JTransFrame(GraphicsConfiguration gc) {
        super(gc);
        init();
    }

    public JTransFrame(String title, GraphicsConfiguration gc) {
        super(title, gc);
        init();
    }

    public synchronized void init() {
        setRootPane(new TransGlassPane());
        if (System.getProperty("os.name").startsWith("Mac OS X")) {
            getRootPane().putClientProperty("Window.shadow", false); // Mac specific
        }

        JComponent content = (JComponent)getContentPane();
        content.setOpaque(false);

        capture = new ScreenCapture(this);
    }

    public void setCapturing(boolean flag) {
        if (flag && !capture.isCapturing()) {
            capture.startCapturing();
            repaint();
        } else if (!flag && capture.isCapturing()) {
            capture.stopCapturing();
            repaint();
        }
    }

    private synchronized void setBackImage(BufferedImage image) {
        backImage = image;
    }

    private synchronized BufferedImage getBackImage() {
        return backImage;
    }

    public synchronized boolean getIgnoreRepaint() {
        capture.notifyPaint();

        return super.getIgnoreRepaint();
    }

    public void addNotify() {
        if (!capture.isCapturing()) {
            capture.startCapturing();
        }

        enableEvents(AWTEvent.COMPONENT_EVENT_MASK);
        super.addNotify();
    }

    public void copyScreen() {
        Rectangle bounds = getBounds();
        Insets insets = getInsets();
        if (!isUndecorated() && insets.equals(NO_INSETS)
            || bounds.width <= 0 || bounds.height <= 0) {
            capture.notifyPaint();
            return;
        }

        bounds = new Rectangle(bounds.x + insets.left,
                               bounds.y + insets.top,
                               bounds.width - insets.left - insets.right,
                               bounds.height - insets.top - insets.bottom);
        if (bounds.width > 0 && bounds.height > 0) {
            setVisible(false);
            setBackImage(capture.captureScreen(bounds));
            capture.setShow(true);
            setVisible(true);
        }
    }

    protected void processComponentEvent(ComponentEvent e) {
        if (capture.isCapturing()) {
            if (e.getID() == ComponentEvent.COMPONENT_MOVED
                || e.getID() == ComponentEvent.COMPONENT_RESIZED) {
                capture.notifyMove();
            }
        }

        super.processComponentEvent(e);
    }

    class TransGlassPane extends JRootPane {
        public final void paintComponent(Graphics g) {
            if (capture.isCapturing()) {
                if (getBackImage() != null) {
                    g.drawImage(getBackImage(), 0, 0, this);
                    capture.setShow(false);
                }
            }
        }
    }
}
