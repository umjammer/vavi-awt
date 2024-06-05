/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.beans.BeanInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import javax.swing.ImageIcon;

import vavix.util.ClassUtil;
import vavi.util.Debug;


/**
 * LayoutManager クラスは Bean の仕様では扱えませんので Introspector の
 * 替わりにこのクラスを用いて *LayoutManager の BeanInfo を取得します．
 *
 * @depends layoutManager.properties
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020518 nsano initial version <br>
 *          1.00 020527 nsano refine <br>
 */
public class LayoutManagerInfoFactory {

    /** */
    private LayoutManagerInfoFactory() {}

    /**
     * TODO vavi.swing.layout にあると決め打ち
     */
    public static BeanInfo getBeanInfo(Class<?> lmClass) {
        try {
            String name = lmClass.getName();
            int p = name.lastIndexOf('.');
            if (p != -1) {
                name = name.substring(p + 1);
            }
            name = "vavi.swing.layout." + name + "Info";
            @SuppressWarnings("unchecked")
            Class<BeanInfo> clazz = (Class<BeanInfo>) Class.forName(name);
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
Debug.println(Level.SEVERE, e);
Debug.printStackTrace(e);
            return null;
        }
    }

    //-------------------------------------------------------------------------

    /** */
    private static List<SampleLayoutManagerInfo> lmis;

    /** TODO */
    public static List<?> getSampleLayoutManagerInfos() {

        final String path = "layoutManager.properties";
        final Toolkit t = Toolkit.getDefaultToolkit();
        final Class<?> c = LayoutManagerInfoFactory.class;

        Properties props = new Properties();

        lmis = new ArrayList<>();

        try {
            props.load(c.getResourceAsStream(path));

            // none
            SampleLayoutManagerInfo lmi = new SampleLayoutManagerInfo();
            lmi.layout = null;
            String key = "icon";
            String val = props.getProperty(0 + "." + key);
            lmi.icon = new ImageIcon(t.getImage(c.getResource(val)));
            key = "desc";
            val = props.getProperty(0 + "." + key);
            lmi.desc = val;
            lmis.add(lmi);

            // 1 ...
            int i = 1;
            while (true) {
                lmi = new SampleLayoutManagerInfo();

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

                    lmi.layout = (LayoutManager)
                        ClassUtil.newInstance(className, argTypes, args);
                } else { // 引数あり
                    @SuppressWarnings("unchecked")
                    Class<LayoutManager> clazz = (Class<LayoutManager>) Class.forName(className);
                    lmi.layout = clazz.getDeclaredConstructor().newInstance();
                }

                key = "icon";
                val = props.getProperty(i + "." + key);
                lmi.icon = new ImageIcon(t.getImage(c.getResource(val)));

                key = "desc";
                val = props.getProperty(i + "." + key);
                lmi.desc = val;

                lmis.add(lmi);

                i++;
            }

            // none
            lmi = new SampleLayoutManagerInfo();
            lmi.layout = null;
            key = "icon";
            val = props.getProperty(i + "." + key);
            lmi.icon = new ImageIcon(t.getImage(c.getResource(val)));
            key = "desc";
            val = props.getProperty(i + "." + key);
            lmi.desc = val;
            lmis.add(lmi);
        } catch (Exception e) {
Debug.println(Level.SEVERE, e);
            throw new IllegalStateException(e);
        }

        return lmis;
    }
}
