/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

import vavi.awt.containereditor.ContainerEditor;
import vavi.awt.containereditor.basic.BasicContainerEditor;
import vavi.swing.event.EditorEvent;
import vavi.swing.event.EditorListener;

import static java.lang.System.getLogger;


/**
 * NullLayoutCustomizer.
 * <p>
 * TODO overlaid
 *      ~~GridBagLayout conversion button~~
 * </p>
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020527 nsano initial version <br>
 */
public class NullLayoutCustomizer extends BasicLayoutManagerCustomizer {

    private static final Logger logger = getLogger(NullLayoutCustomizer.class.getName());

    /** */
    private final ContainerEditor containerEditor;

    /** */
    private final NullLayoutConverter converter;

    /** */
    public NullLayoutCustomizer() {
        // UI
        Container c1 = (Container) getComponent(1); // right base panel
        Component c2 = c1.getComponent(0); // upper titled border panel
        c1.remove(c2);
        ((GridLayout) c1.getLayout()).setRows(1);
        c1.doLayout();

        TitledBorder b = (TitledBorder) lcPanel.getBorder();
        b.setTitle("Converter Properties");

        // init
        this.layout = null;
        layoutPanel.setLayout(null);

        converter = new NullLayoutConverter();
        converter.addPropertyChangeListener(pcl);
        lcTableModel.setObject(converter);
        containerEditor = new BasicContainerEditor(layoutPanel);
        containerEditor.addEditorListener(el);
    }

    /** */
    private final EditorListener el = new EditorListener() {
        @Override
        public void editorUpdated(EditorEvent ev) {
            String name = ev.getName();
logger.log(Level.TRACE, name);
            if ("location".equals(name) || "bounds".equals(name)) {
                if (converter.isJustifyGrid()) {
                    Component component = (Component) ev.getArguments()[0];
                    ajustComponent(component);
                }
            }
        }
    };

    /** */
    private final PropertyChangeListener pcl = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent ev) {
            String name = ev.getPropertyName();
logger.log(Level.TRACE, name);
            if ("overlaid".equals(name)) {
            }
            else if ("justifyGrid".equals(name)) {
                Dimension size = converter.getGridSize();
                Dimension dispSize = new Dimension();
                dispSize.width  = Math.round(size.width  * ratio);
                dispSize.height = Math.round(size.height * ratio);
                ((BasicContainerEditor) containerEditor).setGridSize(dispSize);

                boolean b = (Boolean) ev.getNewValue();
                ((BasicContainerEditor) containerEditor).setGridEnabled(b);
logger.log(Level.TRACE, b);
                if (b) {
                    for (int i = 0; i < layoutPanel.getComponentCount(); i++) {
                        Component component = layoutPanel.getComponent(i);
                        ajustComponent(component);
                    }
                }
            } else if ("gridSize".equals(name)) {
                Dimension size = (Dimension) ev.getNewValue();
                Dimension dispSize = new Dimension();
                dispSize.width  = Math.round(size.width  * ratio);
                dispSize.height = Math.round(size.height * ratio);
                ((BasicContainerEditor) containerEditor).setGridSize(dispSize);

                if (converter.isJustifyGrid()) {
                    for (int i = 0; i < layoutPanel.getComponentCount(); i++) {
                        Component component = layoutPanel.getComponent(i);
                        ajustComponent(component);
                    }
                }
            }
        }
    };

    /** */
    private void ajustComponent(Component component) {
        Dimension size = converter.getGridSize();
        int W = Math.round(size.width  * ratio);
        int H = Math.round(size.height * ratio);

        Rectangle r = component.getBounds();
        int x1 = r.x;
        int y1 = r.y;
        int x2 = r.x + r.width;
        int y2 = r.y + r.height;

        int dx = x1 % W;
        int tx1 = dx < (W - dx) ? x1 - dx : x1 + (W - dx);
        int dy = y1 % H;
        int ty1 = dy < (H - dy) ? y1 - dy : y1 + (H - dy);

        dx = x2 % W;
        int tx2 = dx < (W - dx) ? x2 - dx : x2 + (W - dx);
        dy = y2 % H;
        int ty2 = dy < (H - dy) ? y2 - dy : y2 + (H - dy);

        r.x = tx1;
        r.y = ty1;
        r.width  = tx1 == tx2 ? W : tx2 - tx1;
        r.height = ty1 == ty2 ? H : ty2 - ty1;
logger.log(Level.TRACE, r.x + ", " + r.y + ", " + r.width + ", " + r.height);
        ((BasicContainerEditor) containerEditor).setComponentBounds(component, r);
    }

    @Override
    public void setContainer(Container container) {
        super.setContainer(container);

        converter.setOverlaid(true);
        converter.setJustifyGrid(false);
        converter.setGridSize(new Dimension(20, 20));

        layoutPanel.removeAll();
        components.clear();

        containerEditor.setEditable(true);

        for (int i = 0; i < container.getComponentCount(); i++) {
            Component component = container.getComponent(i);

            JLabel controller = new JLabel(String.valueOf(i));
            controller.setHorizontalAlignment(JLabel.CENTER);
            controller.setOpaque(true);
            controller.setBorder(BorderFactory.createRaisedBevelBorder());

            Rectangle r = component.getBounds();
            int x = Math.round(r.x * ratio);
            int y = Math.round(r.y * ratio);
            int w = Math.round(r.width * ratio);
            int h = Math.round(r.height * ratio);
logger.log(Level.TRACE, x + ", " + y);
            controller.setBounds(new Rectangle(x, y, w, h));

            layoutPanel.add(controller);

            components.put(controller, component);
        }
    }

    /** called last, to set layout to your container */
    @Override
    public void layoutContainer() {
        for (Object o : components.keySet()) {
            Component controller = (Component) o;
            Component component = components.get(controller);
            Rectangle r = controller.getBounds();
            int x = Math.round(r.x / ratio);
            int y = Math.round(r.y / ratio);
            int w = Math.round(r.width / ratio);
            int h = Math.round(r.height / ratio);
            component.setBounds(new Rectangle(x, y, w, h));
//          if (component instanceof JComponent) {
//              ((JComponent) component).setPreferredSize(new Dimension(w, h));
//          }
        }
    }
}
