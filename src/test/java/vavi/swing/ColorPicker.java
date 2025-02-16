/*
 * "https://github.com/wiztools/screen-color-picker"
 */

package vavi.swing;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;


/**
 * ColorPicker.
 *
 * TODO なんかぼやけてる
 *  - "https://bugs.openjdk.java.net/browse/JDK-8207386"
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2019/04/30 umjammer initial version <br>
 * @see "https://github.com/wiztools/screen-color-picker"
 */
public class ColorPicker implements Runnable {

    @Override
    public void run() {

        try {
            Robot robot = new Robot();
            Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
            BufferedImage screenImage;
            synchronized (this) {
                screenImage = robot.createScreenCapture(new Rectangle(screenDimension));
            }

            JWindow window = new JWindow();
            window.getContentPane().setLayout(new BorderLayout());
            JLabel label = new JLabel(new ImageIcon(screenImage));
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent event) {
                    Color color = robot.getPixelColor(event.getX(), event.getY());
                    System.out.printf("#%02X%02X%02X\n", color.getRed(), color.getGreen(), color.getBlue());
                    System.out.printf(" = new Color(0x%02X, 0x%02X, 0x%02X);\n", color.getRed(), color.getGreen(), color.getBlue());
                    window.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    window.setVisible(false);
                    System.exit(0);
                }
            });
            window.getContentPane().add(label);
            window.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            window.pack();
            window.setSize(screenDimension);
            window.setVisible(true);

            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
            graphicsDevice.setFullScreenWindow(window);

        } catch (AWTException e) {
            e.printStackTrace();
            System.exit(2);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ColorPicker());
    }
}
