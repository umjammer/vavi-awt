/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.border.Border;

import vavix.util.ClassUtil;
import vavi.util.Debug;


/**
 * BorderInfoFactory.
 *
 * @depends border.properties
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020518 nsano initial version <br>
 *          1.00 020527 nsano refine <br>
 */
public class BorderInfoFactory {

    /** */
    private BorderInfoFactory() {}

    /**
     * TODO vavi.swing.border にあると決め打ち
     */
    public static BorderInfo getBorderInfo(Class<?> borderClass) {
        try {
            String name = borderClass.getName();
            int p = name.lastIndexOf('.');
            if (p != -1) {
                name = name.substring(p + 1);
            }
            name = "vavi.swing.border." + name + "Info";
            @SuppressWarnings("unchecked")
            Class<BorderInfo> clazz = (Class<BorderInfo>) Class.forName(name);
            return clazz.newInstance();
        } catch (Exception e) {
Debug.println(Level.SEVERE, e);
Debug.printStackTrace(e);
            return null;
        }
    }

    //-------------------------------------------------------------------------

    /** */
    private static List<SampleBorderInfo> bis;

    /**
     * TODO
     * 0: null border
     * last: user defined
     */
    public static List<?> getSampleBorderInfos() {

        final String path = "border.properties";
        final Toolkit t = Toolkit.getDefaultToolkit();
        final Class<?> c = BorderInfoFactory.class;

        Properties props = new Properties();

        bis = new ArrayList<>();

        try {
            props.load(c.getResourceAsStream(path));

            // none
            SampleBorderInfo bi = new SampleBorderInfo();
            bi.border = null;
            String key = "icon";
            String val = props.getProperty(0 + "." + key);
            bi.icon = new ImageIcon(t.getImage(c.getResource(val)));
            key = "desc";
            val = props.getProperty(0 + "." + key);
            bi.desc = val;
            bis.add(bi);

            // 1 ...
            int i = 1;
            while (true) {
                bi = new SampleBorderInfo();

                key = "className";
                String className = props.getProperty(i + "." + key);
                if (className == null) {
                    break;
                }

                key = "argTypes";
                String argTypes = props.getProperty(i + "." + key);
                if (argTypes != null) { // 引数なし
                    key = "args";
                    String args = props.getProperty(i + "." + key);

                    bi.border = (Border) ClassUtil.newInstance(className, argTypes, args);
                } else { // 引数あり
                    @SuppressWarnings("unchecked")
                    Class<Border> clazz = (Class<Border>) Class.forName(className);
                    bi.border = clazz.newInstance();
                }

                key = "icon";
                val = props.getProperty(i + "." + key);
                bi.icon = new ImageIcon(t.getImage(c.getResource(val)));

                key = "desc";
                val = props.getProperty(i + "." + key);
                bi.desc = val;

                bis.add(bi);

                i++;
            }

            // last
            bi = new SampleBorderInfo();
            bi.border = null;
            key = "icon";
            val = props.getProperty(i + "." + key);
            bi.icon = new ImageIcon(t.getImage(c.getResource(val)));
            key = "desc";
            val = props.getProperty(i + "." + key);
            bi.desc = val;
            bis.add(bi);
        } catch (Exception e) {
Debug.println(Level.SEVERE, e);
Debug.printStackTrace(e);
            throw new IllegalStateException(e);
        }

        return bis;
    }
}

/* */
