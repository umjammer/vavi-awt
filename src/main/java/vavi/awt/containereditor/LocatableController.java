/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.containereditor;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import vavi.awt.Controller;


/**
 * ロケータブルなコントローラです。
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020605 nsano initial version <br>
 *          0.01 020611 nsano change package <br>
 */
public interface LocatableController extends Controller {

    /** */
    void setLocation(Point location);

    /** */
    Point getLocation();

    /** */
    void setSize(Dimension size);

    /** */
    Dimension getSize();

    /** */
    void setBounds(Rectangle bounds);

    /** */
    Rectangle getBounds();
}

/* */
