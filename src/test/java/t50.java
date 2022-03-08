/*
 * Copyright (c) 2010 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * t50. transparent window
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2010/01/22 nsano initial version <br>
 */
public class t50 {

    /**
     * @jvmarg -Dimage.reader.class=com.sixlegs.png.iio.PngImageReader
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        final Toolkit t = Toolkit.getDefaultToolkit();

        String className = System.getProperty("image.reader.class", "com.sixlegs.png.iio.PngImageReader");
        Class<?> imageReaderClass;
        try {
            imageReaderClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("no such ImageReader: " + className);
        }

        ImageReader ir = null;
        Iterator<ImageReader> irs = ImageIO.getImageReadersByFormatName("PNG");
        while (irs.hasNext()) {
            ImageReader tmpIw = irs.next();
            if (imageReaderClass.isInstance(tmpIw)) {
                ir = tmpIw;
System.err.println("imageIOSupport:: imageReader: " + ir.getClass());
                break;
            }
        }

        ImageInputStream iis = ImageIO.createImageInputStream(new File("/Users/nsano/Pictures/Cyberduck.png"));
        ir.setInput(iis);
        final BufferedImage image = ir.read(0);

        // TODO move frame instead of image
        JPanel panel = new JPanel() {
            public void paint(Graphics g) {
                g.clearRect(0, 0, getWidth(), getHeight());
                g.drawImage(image, x, y, this);
            }
            int x, y;
            int dx = 10;
            int dy = 10;
            {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            x += dx;
                            y += dy;
                            if (x < 0 || t.getScreenSize().width - image.getWidth() < x) dx *= -1;
                            if (y < 0 || t.getScreenSize().height - image.getHeight() < y) dy *= -1;
                            repaint();
                        } catch (Exception e) {
e.printStackTrace(System.err);
                        }
                    }
                }, 0, 50);
            }
        };
        panel.setPreferredSize(t.getScreenSize());

        JFrame frame = new JFrame();
        frame.setTitle("Niko Niko Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(new Color(0x00000000, true)); // this program's essential
        frame.setUndecorated(true);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}

/* */
