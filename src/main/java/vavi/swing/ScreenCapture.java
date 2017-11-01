/*
 * http://www.javainthebox.net/laboratory/GUI/TransFrame/TransFrame.html
 */

package vavi.swing;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;


class ScreenCapture {
    private Robot robot; 

    private Thread thread;
    private volatile boolean dirtyFlag;
    private volatile boolean showFlag;
    private volatile boolean moveFlag;
    private volatile boolean moveFlagOld;

    private volatile boolean captureFlag = false;

    private Timer timer;

    private Translucentable parent;

    public ScreenCapture(Translucentable parent) {
        this.parent = parent;

        try {
            robot = new Robot();
        } catch (AWTException ex) {
            ex.printStackTrace();
            return;
        }
    }

    public void setShow(boolean showFlag) {
        this.showFlag = showFlag;
    }

    public boolean isCapturing() {
        return captureFlag;
    }

    public void startCapturing() {
        captureFlag = true;

        dirtyFlag = true;
        moveFlagOld = true;

        thread = new Thread(new Runnable() {
                public void run() {
                    watchDirtyRegeon();
                }
            });
        thread.start();

        timer = new Timer();
        TimerTask task = new TimerTask() {
                public void run() {
                    if (moveFlag) {
                        moveFlagOld = moveFlag;
                        moveFlag = false;
                    } else if (moveFlagOld) {
                        dirtyFlag = true;
                        wakeup();
                        moveFlagOld = moveFlag;
                    }
                }
            };
        timer.schedule(task, 0, 1000L);
    }

    public void stopCapturing() {
        captureFlag = false;

        thread.interrupt();
        thread = null;

        timer.cancel();
        timer = null;
    }

    private synchronized void wakeup() {
        notifyAll();
    }

    public void notifyMove() {
        moveFlag = true;
    }

    public void notifyPaint() {
        if (!showFlag) {
            dirtyFlag = true;
            wakeup();
        }
    }

    public BufferedImage captureScreen(Rectangle bounds) {
        try {
            Thread.sleep(200L);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        synchronized (this) {
            return robot.createScreenCapture(bounds);
        }
    }

    private void watchDirtyRegeon() {
        Thread current = Thread.currentThread();
        while(current == thread) {
            if (!dirtyFlag) {
                try {
                    synchronized (this) {
                        wait();
                    }
                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
                    return;
                }
            } else {
                parent.copyScreen();
                dirtyFlag = false;
            }
        }
    }
}
