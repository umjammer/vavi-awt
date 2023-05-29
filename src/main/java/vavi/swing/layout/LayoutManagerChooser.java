/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Properties;
import java.util.logging.Level;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import vavi.util.Debug;


/**
 * LayoutManagerChooser.
 *
 * @event PropertyChangeEvent("layout", null, LayoutManager)
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020516 nsano initial version <br>
 *          0.01 020618 nsano fix null pcl <br>
 */
public class LayoutManagerChooser extends JTabbedPane {

    private static final String path = "layoutManager.properties";

    /** */
    private LayoutManager layout;

    /** */
//  private Container container;

    /** */
    public LayoutManagerChooser() {
        try {
            Properties props = new Properties();
            props.load(getClass().getResourceAsStream(path));

            LayoutManagerCustomizer c = new NullLayoutCustomizer();
            c.addPropertyChangeListener(pcl);
            addTab("null",
                   UIManager.getIcon("layoutManagerChooser.nullLayoutIcon"),
                   (Component) c);

            int i = 1;
            while (true) {
                String key = "editor." + i;
                String value = props.getProperty(key);
                if (value == null) {
Debug.println(Level.FINE, "no property for: editor." + i);
                    break;
                }

Debug.println(Level.FINER, value);
                Class<?> clazz = Class.forName(value);

                BeanInfo bi = LayoutManagerInfoFactory.getBeanInfo(clazz);
                BeanDescriptor bd = bi.getBeanDescriptor();
                @SuppressWarnings("unchecked")
                Class<LayoutManagerCustomizer> cc = (Class<LayoutManagerCustomizer>) bd.getCustomizerClass();
                if (cc != null) {
                    c = cc.newInstance();
                    c.addPropertyChangeListener(pcl);
                    Icon icon = new ImageIcon(bi.getIcon(BeanInfo.ICON_COLOR_16x16));
                    String desc = bd.getShortDescription();
                    addTab(desc, icon, (Component) c);
                }
                i++;
            }
        } catch (Exception e) {
Debug.println(Level.SEVERE, e);
Debug.printStackTrace(e);
        }

        this.setPreferredSize(new Dimension(640, 400));
        this.addChangeListener(cl);
    }

    /** */
    private PropertyChangeListener pcl = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent ev) {
            String name = ev.getPropertyName();
Debug.println(Level.FINER, name);
            if ("layout".equals(name)) {
                layout = (LayoutManager) ev.getNewValue();
Debug.println(Level.FINER, layout == null ? null : layout.getClass().getSimpleName());
//              firePropertyChange("layout", null, layout);
            }
        }
    };

    /** */
    private ChangeListener cl = new ChangeListener() {
        public void stateChanged(ChangeEvent ev) {
            int i = getSelectedIndex();
            LayoutManagerCustomizer lmc = (LayoutManagerCustomizer) getComponentAt(i);
//          lmc.setContainer(container);
            layout = lmc.getObject();
Debug.println(Level.FINER, layout == null ? null : layout.getClass().getSimpleName());
        }
    };

    /** */
    public LayoutManager getSelectedLayoutManager() {
        LayoutManagerCustomizer lmc = (LayoutManagerCustomizer) getSelectedComponent();
        lmc.layoutContainer();
        return layout;
    }

    /** */
    public void setSelectedLayoutManager(LayoutManager layout) {
        this.layout = layout;

        if (layout == null) {
            LayoutManagerCustomizer lmc = (LayoutManagerCustomizer) this.getComponentAt(0);
            lmc.setObject(null);
            setSelectedIndex(0);
            return;
        }

        String name = layout.getClass().getName();
        int p = name.lastIndexOf('.');
        if (p != -1) {
            name = name.substring(p + 1);
        }
        for (int i = 1; i < this.getTabCount(); i++) {
// Debug.println(name + ", " + this.getTitleAt(i));
            if (name.equals(this.getTitleAt(i))) {
                LayoutManagerCustomizer lmc = (LayoutManagerCustomizer) this.getComponentAt(i);
                lmc.setObject(layout);
                setSelectedIndex(i);
                return;
            }
        }

        // TODO use user defined layoutManager info
        addTab(name,
               UIManager.getIcon("layoutManagerChooser.unknownLayoutIcon"),
               new NullLayoutCustomizer());
    }

    /** TODO must be called before setValue */
    public void setSelectedContainer(Container container) {
//      this.container = container;
        for (int i = 0; i < this.getTabCount(); i++) {
            LayoutManagerCustomizer lmc = (LayoutManagerCustomizer) this.getComponentAt(i);
            lmc.setContainer(container);
        }
    }

    // ----

    /* */
    static {
        Class<?> clazz = LayoutManagerChooser.class;
        UIDefaults table = UIManager.getDefaults();
        table.put("layoutManagerChooser.nullLayoutIcon", LookAndFeel.makeIcon(clazz, "resources/NullLayout.gif"));
        table.put("layoutManagerChooser.unknownLayoutIcon", LookAndFeel.makeIcon(clazz, "resources/AbstractLayout.gif"));
    }
}

/* */
