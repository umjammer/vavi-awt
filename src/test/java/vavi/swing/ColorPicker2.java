/*
 * Copyright (c) 2023 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.Toolkit;
import javax.swing.SwingUtilities;


/**
 * ColorPicker2.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2023/05 umjammer initial version <br>
 */
public class ColorPicker2 implements Runnable {

    public void run() {

        try {
            Robot robot = new Robot();

            while (true) {
                Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();

                PointerInfo mouseInfo = MouseInfo.getPointerInfo();
                Color color = robot.getPixelColor(mouseInfo.getLocation().x, mouseInfo.getLocation().y);
                System.out.printf("#%02X%02X%02X\n", color.getRed(), color.getGreen(), color.getBlue());
                System.out.printf(" = new Color(0x%02X, 0x%02X, 0x%02X);\n", color.getRed(), color.getGreen(), color.getBlue());
            }
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ColorPicker2());
    }
}
