/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.rubberband;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.EventObject;


/**
 * RubberBand が変更される時発行するイベントのクラスです．
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010904 nsano initial version <br>
 *          0.10 010904 nsano fix specifications??? <br>
 *          0.20 020604 nsano fix specifications??? <br>
 *          0.21 020605 nsano fix specifications <br>
 */
public class RubberBandEvent extends EventObject {

    /** コンポーネントの相対 location */
    private Point location;
//    /** コンポーネントの相対 size */
//  private Dimension size;
    /** 選択された領域 */
    private Rectangle bounds;

    /**
     * RubberBand イベントを構築します．
     * @see RubberBandListener#selecting
     * @see RubberBandListener#selected
     */
    public RubberBandEvent(Object source, Rectangle bounds) {
        super(source);
        this.bounds = bounds;
    }

    /**
     * RubberBand イベントを構築します．
     * @see RubberBandListener#moving
     * @see RubberBandListener#moved
     * @param location コンポーネントの相対 location
     */
    public RubberBandEvent(Object source, Point location) {
        super(source);
        this.location = location;
    }

//    /**
//     * RubberBand イベントを構築します．
//     * @see RubberBandListener#resizing
//     * @see RubberBandListener#resized
//     * @param size コンポーネントの相対 size
//     */
//  public RubberBandEvent(Object source, Dimension size) {
//      super(source);
//      this.size = size;
//  }

    /** 選択された領域を取得します． */
    public Rectangle getBounds() {
        return bounds;
    }

    /** コンポーネントの相対 location を取得します． */
    public Point getLocation() {
        return location;
    }

//    /** コンポーネントの相対 size を取得します． */
//  public Dimension getDimension() {
//      return size;
//  }
}
