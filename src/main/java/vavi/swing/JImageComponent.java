/*
 * Copyright (c) 1997 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.border.Border;


/**
 * JImageComponent.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 021109 nsano initial version <br>
 */
public class JImageComponent extends JComponent {

    /** */
    private Image image;

    /** */
    public void update(Graphics g) {
        Border border = getBorder();
        int l = 0;
        int t = 0;
        if (border == null) {
            l = 0;
            t = 0;
        } else {
            Insets i = border.getBorderInsets(this);
            l = i.left;
            t = i.top;
        }
        if (image != null) {
            g.drawImage(image, l, t, this);
        } else {
            g.drawImage(noimage, l, t, this);
        }
    }

    /** */
    public void paint(Graphics g) {
        super.paint(g);
        // Dimension d = getSize();
        // g.clearRect(0, 0, d.width, d.height);
        update(g);
    }

    /** */
    public void setImage(Image image) {
        synchronized (image) {
            this.image = image;
        }
    }

    /** */
    private static Image noimage;

    /** */
    static {
        Toolkit t = Toolkit.getDefaultToolkit();
        noimage = t.getImage(JImageComponent.class.getResource("/noimage.png"));
    }
}

/* */

