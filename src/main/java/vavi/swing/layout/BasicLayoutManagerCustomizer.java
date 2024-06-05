/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;

import vavi.swing.propertyeditor.JPropertyEditorTable;
import vavi.util.Debug;


/**
 * BasicLayoutManagerCustomizer.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020527 nsano initial version <br>
 */
public class BasicLayoutManagerCustomizer extends JComponent
    implements LayoutManagerCustomizer {

    /** The target layout */
    protected LayoutManager layout = null;

    /** The target container */
    protected Container container;

    /** The virtual target panel */
    protected final JPanel layoutPanel;

    /** The virtual screen */
    private final JPanel screenPanel;

    /** Layout properties */
    protected final PropertyDescriptorTableModel tableModel;

    /** Layout constraint properties */
    protected final PropertyDescriptorTableModel lcTableModel;

    /** panel for layout constraint properties */
    protected final JPanel lcPanel;

    /** component/controller pair */
    protected final Map<Object, Component> components = new HashMap<>();

    /** */
    public BasicLayoutManagerCustomizer() {

        this.setLayout(new GridLayout(1, 2));

        layoutPanel = new JPanel();
        layoutPanel.setBackground(Color.white);
        screenPanel = new JPanel();
        screenPanel.setLayout(null);
        screenPanel.setBackground(UIManager.getColor("Desktop.background"));
//      screenPanel.setPreferredSize(new Dimension());
        screenPanel.add(layoutPanel);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(screenPanel);

        this.add(panel);

        panel = new JPanel(new GridLayout(2, 1));
//      panel.setName("right base");
        tableModel = new PropertyDescriptorTableModel();
        JTable table = new JPropertyEditorTable(tableModel);
        JScrollPane sp = new JScrollPane(table);
        JPanel p = new JPanel(new BorderLayout());
//      p.setName("upper titled");
        p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Layout Properties"));
        p.add(sp);
        panel.add(p);
        lcTableModel = new PropertyDescriptorTableModel();
        JPropertyEditorTable lcTable = new JPropertyEditorTable(lcTableModel);
        sp = new JScrollPane(lcTable);
        lcPanel = new JPanel(new BorderLayout());
//      lcPanel.setName("lcPanel");
        lcPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Component Constraints"));
        lcPanel.add(sp);
        panel.add(lcPanel);

        this.add(panel);
    }

    @Override
    public void setObject(LayoutManager layout) {
        LayoutManager oldLayout = this.layout;
        this.layout = layout;

Debug.println(Level.FINER, "old: " + (oldLayout == null ? null : oldLayout.getClass().getSimpleName()));
Debug.println(Level.FINER, "new: " + (layout == null ? null : layout.getClass().getSimpleName()));
        firePropertyChange("layout", oldLayout, layout);
    }

    /** TODO */
    @Override
    public LayoutManager getObject() {
        return layout;
    }

    /** Size ratio against the target container */
    protected float ratio;

    /**
     * <ol>
     * <li> set ratio
     * <li> set layoutPanel bounds
     * </ol>
     * In your sub class {@link #setContainer(Container)},
     * must be called <code>super.</code>{@link #setContainer(Container)} at first
     * for setting {@link #ratio}
     */
    @Override
    public void setContainer(Container container) {
        this.container = container;

        Dimension ss = screenPanel.getSize();
        Dimension cs = container.getSize();
Debug.println(Level.FINER, cs.width + ", " + cs.height);
Debug.println(Level.FINER, ss.width + ", " + ss.height);
//screenPanel.setPreferredSize(ss);
        if (ss.width < ss.height) {
            ratio = (float) ss.width / cs.width * 0.9f;
        } else {
            ratio = (float) ss.height / cs.height * 0.9f;
        }
Debug.println(Level.FINER, ratio);
        int w = Math.round(cs.width * ratio);
        int h = Math.round(cs.height * ratio);
        layoutPanel.setSize(new Dimension(w, h));
        layoutPanel.setLocation(new Point((ss.width - w) / 2, (ss.height - h) / 2));
    }

    @Override
    public void layoutContainer() {
    }
}
