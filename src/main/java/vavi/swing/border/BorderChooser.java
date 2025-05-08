/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.awt.Component;
import java.awt.Dimension;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.PropertyChangeListener;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Properties;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;

import static java.lang.System.getLogger;


/**
 * BorderChooser.
 *
 * @depends border.properties
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 original version <br>
 *          1.00 020527 nsano complete <br>
 */
public class BorderChooser extends JTabbedPane {

    private static final Logger logger = getLogger(BorderChooser.class.getName());

    /** */
    private static final String path = "border.properties";

    /** the border you choose */
    private Border border = null;

    /** Creates a border chooser. */
    public BorderChooser() {
        try {
            Properties props = new Properties();
            props.load(getClass().getResourceAsStream(path));

            addTab("null", UIManager.getIcon("borderChooser.nullBorderIcon"),
                   new NullBorderCustomizer());

            int i = 1;
            while (true) {
                String key = "editor." + i;
                String value = props.getProperty(key);
                if (value == null) {
logger.log(Level.DEBUG, "no property for: editor." + i);
                    break;
                }

logger.log(Level.TRACE, value);
                Class<?> clazz = Class.forName(value);

                BorderInfo bi = BorderInfoFactory.getBorderInfo(clazz);
                BeanDescriptor bd = bi.getBeanDescriptor();
                @SuppressWarnings("unchecked")
                Class<BorderCustomizer> cc = (Class<BorderCustomizer>) bd.getCustomizerClass();
                if (cc != null) {
                    BorderCustomizer c = cc.getDeclaredConstructor().newInstance();
                    c.addPropertyChangeListener(pcl);
                    Icon icon = new ImageIcon(bi.getIcon(BeanInfo.ICON_COLOR_16x16));
                    String desc = bd.getShortDescription();
                    addTab(desc, icon, (Component) c);
                }
                i++;
            }
        } catch (Exception e) {
logger.log(Level.ERROR, e.getMessage(), e);
        }

        this.setPreferredSize(new Dimension(640, 320));
        this.addChangeListener(cl);
    }

    /** listener for customizers */
    private final PropertyChangeListener pcl = ev -> {
        if ("border".equals(ev.getPropertyName())) {
            border = (Border) ev.getNewValue();
        }
    };

    /** listener for this (tabbed pane) */
    private final ChangeListener cl = ev -> {
        int i = getSelectedIndex();
        BorderCustomizer bc = (BorderCustomizer) getComponentAt(i);
        border = bc.getObject();
logger.log(Level.TRACE, border);
    };

    /** Gets selected border. */
    public Border getSelectedBorder() {
        return border;
    }

    /** Sets selected border. tab will be changed. */
    public void setSelectedBorder(Border border) {
        this.border = border;

        if (border == null) {
            BorderCustomizer bc = (BorderCustomizer) this.getComponentAt(0);
            bc.setObject(null);
            setSelectedIndex(0);
            return;
        }

        String name = border.getClass().getName();
        int p = name.lastIndexOf('.');
        if (p != -1)
            name = name.substring(p + 1);
        for (int i = 1; i < this.getTabCount(); i++) {
//logger.log(Level.INFO, name + ", " + this.getTitleAt(i));
            if (name.equals(this.getTitleAt(i))) {
                BorderCustomizer bc = (BorderCustomizer) this.getComponentAt(i);
                bc.setObject(border);
                setSelectedIndex(i);
                return;
            }
        }

        // TODO use user defined border info
        addTab(name, UIManager.getIcon("borderChooser.unknownBorderIcon"), new NullBorderCustomizer());
    }

    /* get icons */
    static {
        Class<?> clazz = BorderChooser.class;
        UIDefaults table = UIManager.getDefaults();
        table.put("borderChooser.nullBorderIcon", LookAndFeel.makeIcon(clazz, "resources/nullBorder.gif"));
        table.put("borderChooser.unknownBorderIcon", LookAndFeel.makeIcon(clazz, "resources/unknownBorder.gif"));
    }
}
