/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.util.Properties;
import java.util.logging.Level;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import vavi.awt.event.ComponentSelectionEvent;
import vavi.awt.event.ComponentSelectionListener;
import vavi.awt.event.ComponentSelectionSupport;
import vavi.util.Debug;


/**
 * bean を選択する TabbedPane です．
 *
 * TODO button management
 * DONE load beans
 *
 * @event ComponentSelectionEvent("select", JToggleButton)
 *
 * @depends ${JDK_HOME}/lib/dt.jar
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010823 nsano initial version <br>
 *          0.10 020509 nsano be independent <br>
 *          0.20 020510 nsano be component <br>
 *          0.21 020512 nsano use ComponentSelectionEvent <br>
 */
public class JBeansTabbedPane extends JTabbedPane {

    /** @see    BeanInfo#getIcon param iconKind */
    private int iconKind = BeanInfo.ICON_COLOR_16x16;

    /** Preferred size for 16 x 16 icon */
    private static final Dimension dimension16x16 = new Dimension(28, 28);
    /** Preferred size for 32 x 32 icon */
    private static final Dimension dimension32x32 = new Dimension(40, 40);

    /** TODO depend on BeanInfo.ICON_X_NxN constants */
    private static final Dimension[] preferredSizes = {
        dimension16x16, // ICON_COLOR_16x16
        dimension32x32, // ICON_COLOR_32x32
        dimension16x16, // ICON_MONO_16x16
        dimension32x32  // ICON_MONO_32x32
    };

    /**
     * bean を選択する TabbedPane を構築します．
     */
    public JBeansTabbedPane() {

        int i = 0;
    while (true) {

            // one tab
            JPanel tab = new JPanel(new FlowLayout(FlowLayout.LEFT));

            String key = "tab." + i + ".title";
            String value = props.getProperty(key);
            if (value == null) {
Debug.println("no property for: " + key);
                break;
            }

            this.addTab(value, tab);

            // button group
            ButtonGroup group = new ButtonGroup();

            int j = 0;
            while (true) {
                key = "tab." + i + "." + j;
                value = props.getProperty(key);
                if (value == null) {
Debug.println("no property for: " + key);
                    break;
                }

                try {
                    Class<?> beanClass = Class.forName(value);
                    BeanInfo info = Introspector.getBeanInfo(beanClass);

                    JToggleButton button = new JToggleButton();
                    Image image = info.getIcon(iconKind);
                    if (image == null) {
                        button.setIcon(UIManager.getIcon("jBeansTabbedPane.defaultIcon"));
                    }
                    else {
                        button.setIcon(new ImageIcon(image));
                    }
                    button.setToolTipText(value);
                    button.setActionCommand(value);    // 仕様
                    button.setPreferredSize(preferredSizes[iconKind-1]);
                    button.addActionListener(selectAction);

                    group.add(button);
                    tab.add(button);
                }
                catch (Exception e) {
Debug.println(value);
Debug.printStackTrace(e);
                }
                j++;
            }
            i++;
        }
    }

    /** */
    public void setIconKind(int iconKind) {
        this.iconKind = iconKind;
    }

    /** */
    public int getIconKind() {
        return iconKind;
    }

    /** */
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (int i = 0; i < this.getTabCount(); i++) {
            Container container = (Container) this.getComponentAt(i); // tab
            for (int j = 0; j < container.getComponents().length; j++) {
                Component component = container.getComponent(j); // button
                component.setEnabled(enabled);
            }
        }
    }

    /** 選択を解除します． TODO 効いてない，なんで？？？ */
    public void deselectAll() {
        for (int i = 0; i < this.getTabCount(); i++) {
            Container container = (Container) this.getComponentAt(i); // tab
            for (int j = 0; j < container.getComponents().length; j++) {
                JToggleButton b = (JToggleButton) container.getComponent(j);
                b.setSelected(false);
            }
        }
    }

    /** ボタンが選択されたとき． */
    private ActionListener selectAction = new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            fireValueChanged(
                new ComponentSelectionEvent(this, ev.getSource()));
        }
    };

    //-------------------------------------------------------------------------

    /** ComponentSelectionEvent 機構のユーティリティ */
    private ComponentSelectionSupport css = new ComponentSelectionSupport();

    /** ComponentSelection リスナーをアタッチします． */
    public void addComponentSelectionListener(ComponentSelectionListener l) {
        css.addComponentSelectionListener(l);
    }

    /** ComponentSelection リスナーをリムーブします． */
    public void removeComponentSelectionListener(ComponentSelectionListener l){
        css.removeComponentSelectionListener(l);
    }

    /** */
    protected void fireValueChanged(ComponentSelectionEvent ev) {
        css.fireValueChanged(ev);
    }

    //-------------------------------------------------------------------------

    /** プロパティ */
    private static Properties props = new Properties();

    /**
     * 初期化します．
     */
    static {
        Toolkit t = Toolkit.getDefaultToolkit();
        final Class<?> clazz = JBeansTabbedPane.class;
        final String base = "/toolbarButtonGraphics/";

        UIDefaults table = UIManager.getDefaults();
        table.put("jBeansTabbedPane.defaultIcon", new ImageIcon(t.getImage(
            clazz.getResource(base + "development/Bean16.gif"))));

        final String path = "JBeansTabbedPane.properties";
        try {
            props.load(clazz.getResourceAsStream(path));
        } catch (Exception e) {
Debug.println(Level.SEVERE, "no properties file");
            throw new IllegalStateException(e);
        }
    }

    //-------------------------------------------------------------------------

    /** このクラスをテストします． */
    public static void main(String[] args) {
        JFrame frame = new JFrame("JBeansTabbedPane Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JBeansTabbedPane t = new JBeansTabbedPane();
        t.setPreferredSize(new Dimension(640, 80));
        frame.getContentPane().add(t);
        frame.pack();
        frame.setVisible(true);
    }
}

/* */
