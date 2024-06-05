/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.SwingConstants;

import vavi.awt.ImageComponent;
import vavi.util.Debug;


/**
 * JImageComponentTest
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (vavi)
 * @version 0.00 220915 nsano initial version <br>
 */
public class ImageComponentTest {

    /** */
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame();
        frame.setTitle("ImageComponent");
        frame.setLocation(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageComponent image = new ImageComponent(true);
        image.setPreferredSize(new Dimension(400, 600));
        image.addPropertyChangeListener(e -> {
            if (e.getPropertyName().equals("droppedImage")) {
Debug.println("dropped: " + e.getNewValue());
            } else if (e.getPropertyName().equals("image")) {
                Image sub = image.getSubimage(10, 10, 40, 40);
Debug.println("sub: " + sub);
                assert sub != null : "sub image is null";
            }
        });
        // https://componenthouse.com/2008/02/08/high-quality-image-resize-with-java/
        Map<Object, Object> hints = new HashMap<>();
        hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        image.setRenderingHints(hints);
        image.setImageHorizontalAlignment(SwingConstants.RIGHT);
        image.setImageVerticalAlignment(SwingConstants.CENTER);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(image, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
Debug.println(image.getSize());
    }
}
