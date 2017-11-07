/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.containereditor.basic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import vavi.awt.containereditor.AbstractController;


/**
 * Basic コントローラです．
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 010823 nsano initial version <br>
 *          0.10 020508 nsano deprecate basePanel <br>
 *          0.11 020510 nsano make simple <br>
 *          0.12 020611 nsano fix <br>
 */
public class BasicController extends AbstractController {

    /**
     * Basic コントローラを構築します．
     */
    public BasicController(Component view) {
        super(view);

        this.setBounds(view.getBounds());
//      view.addPropertyChangeListener(pcl);
    }

    /** */
//  private PropertyChangeListener pcl = new PropertyChangeListener() {
//      public void propertyChange(PropertyChangeEvent ev) {
//          String name = ev.getPropertyName();
//Debug.println(name);
//      }
//  };

    /** TODO rubberBandRenderer.drawSelected */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isSelected()) {
            g.setColor(Color.blue);
            final int I = 5;
            int w = getSize().width - 1;
            int h = getSize().height - 1;
            g.drawRect(0, 0, w, h);
            g.fillRect(0, 0, I, I);
            g.fillRect(w - I, 0, I, I);
            g.fillRect(0, h - I, I, I);
            g.fillRect(w - I, h - I, I, I);
        } else {
            g.setColor(Color.white);
            g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
        }
    }
}

/* */
