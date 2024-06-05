/*
 * Copyright (c) 2010 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * NicoPanel. niconico
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2010/01/22 nsano initial version <br>
 */
public class NicoPanel {

    static class Comment {
        long time;
        String comment;
        int y;
        int x;
        boolean on;
        Comment(long time, String comment) {
            this.time = time;
            this.comment = comment;
        }
    }

    static class SmilyPanel extends JPanel {
        private long time;
        List<Comment> comments = new ArrayList<>();
        public void addComment(long time, String comment) {
            comments.add(new Comment(time, comment));
        }
        public void paint(Graphics g) {
            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.white);
            for (Comment comment : comments) {
                if (comment.on) {
                    g.drawString(comment.comment, comment.x, comment.y);
                }
            }
        }
        private Timer timer;
        final long interval = 50;
        void play() {
            time = 0;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        time += interval;
                        for (Comment comment : comments) {
                            if (time == comment.time) {
                                comment.on = true;
                                comment.x = getWidth();
                                comment.y = new Random().nextInt(16) * (getHeight() / 16);
                            }
                            if (comment.on) {
                                comment.x -= 10;
                                if (comment.x < 0) {
                                    comment.on = false;
                                }
                            }
                        }
                        repaint();
                    } catch (Exception e) {
e.printStackTrace(System.err);
                    }
                }
            }, 0, interval);
        }
        public void stop() {
            timer.cancel();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        SmilyPanel panel = new SmilyPanel();
        panel.setPreferredSize(new Dimension(640, 400));
        panel.addComment(50, "aaaaaaaaaaaaaa");
        panel.addComment(150, "うは〜");
        panel.addComment(250, "bbbbbbbbbbbbb");
        panel.addComment(350, "wwwwwwwwwwwwwww");
        panel.addComment(450, "キターーー");
        panel.addComment(2000, "うぇうぇ");
        panel.addComment(2350, "wwwwwwwwwwwwwww");
        panel.addComment(1350, "wwwwwwwwwwwwwww");

        JFrame frame = new JFrame();
        frame.setTitle("Niko Niko Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);

        panel.play();
        Thread.sleep(6000);
        panel.stop();
        panel.play();
    }
}
