/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.io.Serializable;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;


/**
 * ボリューム
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010919 nsano initial version <br>
 *          0.01 020516 nsano use paintComponent <br>
 */
public class JVolume extends JComponent {

    /**
     * The data model that handles the numeric maximum value,
     * minimum value, and current-position value for the volume.
     */
    protected BoundedRangeModel volumeModel;

    /*
     * @see #addChangeListener
     * @see #createChangeListener
     */
    protected ChangeListener changeListener = createChangeListener();

    /**
     * @see #fireStateChanged
     */
    protected transient ChangeEvent changeEvent = null;

    /**
     * 範囲が 0 〜 100、初期値が 50 のボリュームを作成します。
     */
    public JVolume() {
        this(0, 100, 50);
    }

    /**
     * 最小値と最大値を指定して、初期値が 50 のボリュームを作成します。
     */
    public JVolume(int min, int max) {
        this(min, max, (min + max) / 2);
    }

    /**
     * 最小値、最大値、および初期値を指定して、ボリュームを作成します。
     * @see #setMinimum
     * @see #setMaximum
     * @see #setValue
     */
    public JVolume(int min, int max, int value) {
        volumeModel = new DefaultBoundedRangeModel(value, 0, min, max);
        volumeModel.addChangeListener(changeListener);

        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
    }

    /**
     * Creates a volume using the specified
     * BoundedRangeModel.
     */
    public JVolume(BoundedRangeModel brm) {
        setModel(brm);
        volumeModel.addChangeListener(changeListener);

        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
    }

    /**
     * We pass Change events along to the listeners with the
     * the volume (instead of the model itself) as the event source.
     */
    private class ModelListener implements ChangeListener, Serializable {
        public void stateChanged(ChangeEvent e) {
            fireStateChanged();
        }
    }

    /**
     * Subclasses that want to handle model ChangeEvents differently
     * can override this method to return their own ChangeListener
     * implementation.  The default ChangeListener just forwards
     * ChangeEvents to the ChangeListeners added directly to the volume.
     *
     * @see #fireStateChanged
     */
    protected ChangeListener createChangeListener() {
        return new ModelListener();
    }

    /**
     * Adds a ChangeListener to the volume.
     *
     * @param l the ChangeListener to add
     * @see #fireStateChanged
     * @see #removeChangeListener
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    /**
     * Removes a ChangeListener from the volume.
     *
     * @param l the ChangeListener to remove
     * @see #fireStateChanged
     * @see #addChangeListener
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    /**
     * Returns an array of all the <code>ChangeListener</code>s added
     * to this JVolume with addChangeListener().
     *
     * @return all of the <code>ChangeListener</code>s added or an empty
     *         array if no listeners have been added
     * @since 1.4
     */
    public ChangeListener[] getChangeListeners() {
        return listenerList.getListeners(ChangeListener.class);
    }

    /**
     * Send a ChangeEvent, whose source is this Volume, to
     * each listener.  This method method is called each time
     * a ChangeEvent is received from the model.
     *
     * @see #addChangeListener
     * @see EventListenerList
     */
    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i+1]).stateChanged(changeEvent);
            }
        }
    }

    /**
     * Returns data model that handles the volumes three
     * fundamental properties: minimum, maximum, value.
     *
     * @see #setModel
     */
    public BoundedRangeModel getModel() {
        return volumeModel;
    }

    /**
     * Sets the model that handles the volumes three
     * fundamental properties: minimum, maximum, value.
     *
     * @see #getModel
     * @beaninfo
     *       bound: true
     * description: The volumes BoundedRangeModel.
     */
    public void setModel(BoundedRangeModel newModel) {
        BoundedRangeModel oldModel = getModel();

        if (oldModel != null) {
            oldModel.removeChangeListener(changeListener);
        }

        volumeModel = newModel;

        if (newModel != null) {
            newModel.addChangeListener(changeListener);
        }

        firePropertyChange("model", oldModel, volumeModel);
    }

    /** 値を取得します． */
    public int getValue() {
        return getModel().getValue();
    }

    /** 値を設定します． */
    public void setValue(int value) {
        BoundedRangeModel m = getModel();
        int oldValue = m.getValue();
        if (oldValue == value) {
            return;
        }
        m.setValue(value);
    }

    /** 最小値を取得します． */
    public int getMinimum() {
        return getModel().getMinimum();
    }

    /** 最小値を設定します． */
    public void setMinimum(int min) {
        int oldMin = getModel().getMinimum();
        getModel().setMinimum(min);
        firePropertyChange("minimum", new Integer(oldMin), new Integer(min));
    }

    /** 最大値を取得します． */
    public int getMaximum() {
        return getModel().getMaximum();
    }

    /** 最大値を設定します． */
    public void setMaximum(int max) {
        int oldMax = getModel().getMaximum();
        getModel().setMaximum(max);
        firePropertyChange("maximum", new Integer(oldMax), new Integer(max));
    }

    /**
     * True if the volume knob is being dragged.
     *
     * @return the value of the models valueIsAdjusting property
     * @see #setValueIsAdjusting
     */
    public boolean getValueIsAdjusting() {
        return getModel().getValueIsAdjusting();
    }

    /**
     * Sets the models valueIsAdjusting property.  Volume look and
     * feel implementations should set this property to true when
     * a knob drag begins, and to false when the drag ends.  The
     * volume model will not generate ChangeEvents while
     * valueIsAdjusting is true.
     *
     * @see #getValueIsAdjusting
     * @see BoundedRangeModel#setValueIsAdjusting
     * @beaninfo
     *      expert: true
     * description: True if the volume knob is being dragged.
     */
    public void setValueIsAdjusting(boolean b) {
        BoundedRangeModel m = getModel();
//      boolean oldValue = m.getValueIsAdjusting();
        m.setValueIsAdjusting(b);
    }

    /**
     * Returns the "extent" -- the range of values "covered" by the knob.
     * @return an int representing the extent
     * @see #setExtent
     * @see BoundedRangeModel#getExtent
     */
    public int getExtent() {
        return getModel().getExtent();
    }

    /**
     * Sets the size of the range "covered" by the knob.  Most look
     * and feel implementations will change the value by this amount
     * if the user clicks on either side of the knob.
     *
     * @see #getExtent
     * @see BoundedRangeModel#setExtent
     * @beaninfo
     *      expert: true
     * description: Size of the range covered by the knob.
     */
    public void setExtent(int extent) {
        getModel().setExtent(extent);
    }

    //---- UI -----------------------------------------------------------------

    /** マウスの x 座標 */
    private transient int x;
    /** マウスの y 座標 */
    private transient int y;

    /** マウスリスナ */
    private MouseInputListener mouseListener = new MouseInputAdapter() {
        /** マウスが押されたときに呼ばれます．*/
        public void mousePressed(MouseEvent ev) {
            setValueIsAdjusting(true);
//Debug.println(ev.getX() + ", " + ev.getY());
        }
        /** マウスがドラッグしたときに呼ばれます． */
        public void mouseDragged(MouseEvent ev) {
            if (getValueIsAdjusting()) {
                x = ev.getX() - getWidth() / 2;
                y = getHeight() / 2 - ev.getY();
                repaint();
//Debug.println(x + ", " + y);
            }
        }
        /** マウスが放されたときに呼ばれます． */
        public void mouseReleased(MouseEvent ev) {
            if (getValueIsAdjusting()) {
                x = ev.getX() - getWidth() / 2;
                y = getHeight() / 2 - ev.getY();
                setValueIsAdjusting(false);
//Debug.println(x + ", " + y);
                double theta = StrictMath.atan2(x, y);
                if (theta < 0) theta = 2 * Math.PI + theta;
                setValue((int) (getMinimum() +
                        theta / (2 * Math.PI) *
                        (getMaximum() - getMinimum())));
//Debug.println(getValue());
                repaint();
            }
        }
    };

    /** ボリュームノブの幅 */
    private double r = 0.4;

    /** ボリュームを描画します． */
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setStroke(new BasicStroke(2.0f));

        RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2.setRenderingHints(qualityHints);

        int w = getWidth();
        int h = getHeight();

//      if (!isOpaque()) {
//          g.setColor(getBackground());
//          g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
//      }

        g.setColor(Color.white);
        g2.draw(new Arc2D.Double(0, 0, w, h, 90, 90, Arc2D.OPEN));

        g.setColor(Color.black);
        g2.draw(new Arc2D.Double(0, 0, w, h, 270, 90, Arc2D.OPEN));

//      g.setColor(UIManager.getColor("control"));
        g.setColor(new Color(0xcc, 0xcc, 0xff));
        g.fillOval(0, 0, w, h);

        double X;
        double Y;

        double W = w / 2d;
        double H = h / 2d;
        // ボリュームノブの位置
        double a = W * (1 - r / 2 * 1.2);
        double b = H * (1 - r / 2 * 1.2);

        if (getValueIsAdjusting()) {
//Debug.println("(x, y) = (" + x + ", " + y + ")");
            // X, Y は正規座標
            X = Math.sqrt((a * a * b * b * x * x) / (b * b * x * x + a * a * y * y));
            Y = Math.sqrt((a * a * b * b * y * y) / (b * b * x * x + a * a * y * y));

            if (x < 0) X *= -1;
            if (y < 0) Y *= -1;

//Debug.println("(X, Y) = (" + X + ", " + Y + ")");
            // Java 座標への変換
            X = W + X;
            Y = H - Y;
        } else {
            // TODO theta の座標は正しくない
            double theta = (double) (getValue() - getMinimum()) / (double) (getMaximum() - getMinimum()) * (Math.PI * 2) - Math.PI / 2;
// Debug.println(getValue());
//Debug.println(theta);
            // X, Y は正規座標
            X = Math.sqrt((a * a * b * b) / (b * b + a * a * Math.pow(Math.tan(theta), 2)));
            Y = Math.sqrt((a * a * b * b) / (b * b / Math.pow(Math.tan(theta), 2) + a * a));

            // TODO 正しくない theta の座標に対しての補正
            if (theta > Math.PI / 2 && theta < Math.PI * 1.5) X *= -1;
            if (theta < Math.PI     && theta > 0)             Y *= -1;

//Debug.println("(X, Y) = (" + X + ", " + Y + ")");
            // Java 座標への変換
            X = W + X;
            Y = H - Y;
        }

        // ノブの半径
        double R = Math.min(W, H) * r / 2;

        int nx = (int) (X - R);
        int ny = (int) (Y - R);
        int nr = (int) (2 * R);

        g.setColor(Color.black);
        g2.draw(new Arc2D.Double(nx, ny, nr, nr, 90, 90, Arc2D.OPEN));

        g.setColor(Color.white);
        g2.draw(new Arc2D.Double(nx, ny, nr, nr, 270, 90, Arc2D.OPEN));

        g.setColor(new Color(0x99, 0x99, 0xcc));
//      g.setColor(Color.pink);
        g.fillOval(nx, ny, nr, nr);
//Debug.println(X + ", " + Y);
    }

    //-------------------------------------------------------------------------

    /** このクラスをテストします． */
    public static void main(String[] args) {
        JFrame frame = new JFrame("JVolume Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JVolume volume = new JVolume();
        volume.setPreferredSize(new Dimension(120, 100));
        frame.getContentPane().add(volume);
        frame.pack();
        frame.setVisible(true);

//        Enumeration e = UIManager.getDefaults().keys();
//        while (e.hasMoreElements()) {
//            Object o = e.nextElement();
//            System.err.println(o + "=" + UIManager.getDefaults().get(o));
//        }
    }
}

/* */
