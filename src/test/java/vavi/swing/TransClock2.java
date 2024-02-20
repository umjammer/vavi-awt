package vavi.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import vavi.swing.ClockTask.Updatable;


/**
 * TODO 時計自身も薄くなるやんか！
 *
 * @see "http://www.code-life.jp/blog/2009/04/26/java%E3%81%A7%E9%9D%9E%E7%9F%A9%E5%BD%A2%E3%83%BB%E5%8D%8A%E9%80%8F%E6%98%8E%E3%82%A6%E3%82%A3%E3%83%B3%E3%83%89%E3%82%A6swing%E7%89%88/"
 */
public class TransClock2 implements Updatable {
    private Preferences prefs;
    private final static String FAMILY = "family";
    private final static String STYLE = "style";
    private final static String SIZE = "size";
    private final static String COLOR = "color";
    private final static String LOCATION_X = "locationX";
    private final static String LOCATION_Y = "locationY";

    private JFrame frame;
    private JLabel clockLabel;
    private DateFormat formatter = DateFormat.getDateTimeInstance();
    private JPopupMenu popup;
    private JColorChooser colorChooser;

    private Color color;
    private Font font;

    private volatile int locationX;
    private volatile int locationY;

    public TransClock2() {
        prefs = Preferences.userNodeForPackage(this.getClass());

        frame = new JFrame("Translucent Clock 2");
        frame.setUndecorated(true);
        frame.setOpacity(0.2f);

        frame.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                if (event.isPopupTrigger()) {
                    showPopupMenu(event.getComponent(), event.getX(), event.getY());
                } else {
                    if (event.getButton() == MouseEvent.BUTTON1) {
                        Point p = frame.getLocationOnScreen();
                        locationX = p.x - event.getX();
                        locationY = p.y - event.getY();

                        frame.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                    }
                }
            }

            public void mouseReleased(MouseEvent event) {
                int x = locationX + event.getX();
                int y = locationY + event.getY();

                prefs.putInt(LOCATION_X, x);
                prefs.putInt(LOCATION_Y, y);
                try {
                    prefs.flush();
                } catch (BackingStoreException ex) {
                    ex.printStackTrace();
                }

                frame.setLocation(x, y);
                frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });

        clockLabel = initClockLabel();
        popup = initPopupMenu();

        frame.getContentPane().add(clockLabel);

        int x = prefs.getInt(LOCATION_X, 0);
        int y = prefs.getInt(LOCATION_Y, 0);
        frame.setLocation(x, y);

        frame.pack();
        Dimension dim = frame.getSize();
        frame.setSize((int) (dim.width * 1.1), dim.height);
        frame.setVisible(true);

        initTimer();
    }

    private JLabel initClockLabel() {
        JLabel label = new JLabel();
        color = label.getForeground();
        font = label.getFont();
        font = new Font(font.getFontName(), font.getStyle(), font.getSize() + 8);

        String family = prefs.get(FAMILY, font.getFamily());
        int style = prefs.getInt(STYLE, font.getStyle());
        int size = prefs.getInt(SIZE, font.getSize());
        int argb = prefs.getInt(COLOR, color.getRGB());

        font = new Font(family, style, size);
        color = new Color(argb, true);

        label.setFont(font);
        label.setForeground(color);
        label.setOpaque(false);

        label.setText(formatter.format(new Date(System.currentTimeMillis())));

        return label;
    }

    private JPopupMenu initPopupMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem item = new JMenuItem("色の変更...");
        item.addActionListener(event -> changeColor());
        menu.add(item);

        item = new JMenuItem("フォントの変更...");
        item.addActionListener(event -> changeFont());
        menu.add(item);

        item = new JMenuItem("終了");
        item.addActionListener(event -> System.exit(0));
        menu.add(item);

        return menu;
    }

    private void initTimer() {
        Timer timer = new Timer();

        // 2 秒後からスタート
        Date start = new Date((System.currentTimeMillis() / 1000L) * 1000L + 500L);
        timer.scheduleAtFixedRate(new ClockTask(this), start, 1000L);
    }

    private void changeColor() {
        colorChooser = new JColorChooser(color);
        colorChooser.addChooserPanel(new ARGBChooserPanel());

        JDialog dialog = JColorChooser.createDialog(frame, "Font Color", true, colorChooser, e -> {
            color = colorChooser.getColor();

            prefs.putInt(COLOR, color.getRGB());
            try {
                prefs.flush();
            } catch (BackingStoreException ex) {
                ex.printStackTrace();
            }

            clockLabel.setForeground(color);
        }, e -> {
        });
        dialog.setVisible(true);
    }

    private void changeFont() {
        FontChooser chooser = new FontChooser(font);
        int result = chooser.showDialog(frame);
        if (result == FontChooser.APPROVE_OPTION) {
            font = chooser.getFont();

            prefs.put(FAMILY, font.getFamily());
            prefs.putInt(STYLE, font.getStyle());
            prefs.putInt(SIZE, font.getSize());
            try {
                prefs.flush();
            } catch (BackingStoreException ex) {
                ex.printStackTrace();
            }

            clockLabel.setFont(font);
            frame.pack();
            Dimension dim = frame.getSize();
            frame.setSize((int)(dim.width * 1.1), dim.height);
        }
    }

    private void showPopupMenu(Component comp, int x, int y) {
        popup.show(comp, x, y);
    }

    public void update(long time) {
        clockLabel.setText(formatter.format(new Date(time)));
    }

    public static void main(String[] args) {
        new TransClock2();
    }
}
