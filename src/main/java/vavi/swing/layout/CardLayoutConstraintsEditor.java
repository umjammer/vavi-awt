/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.CardLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;

import vavi.util.Debug;


/**
 * CardLayoutConstraintEditor.
 *
 * @todo get constraint when initializing components in the container.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020528 nsano initial version <br>
 */
public class CardLayoutConstraintsEditor
    extends AbstractLayoutConstraintsEditor {

    /** Creates card constraints editor */
    public CardLayoutConstraintsEditor(CardLayout layout) {
        super(layout);
    }

    //-------------------------------------------------------------------------

    /** for table model setter */
    private PropertyChangeListener pcl = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent ev) {
            Component component = container.getComponent(index);
Debug.println(Level.FINE, index);
            layout.removeLayoutComponent(component);
            layout.addLayoutComponent(component, ev.getNewValue());

            layout.layoutContainer(container);
        }
    };

    /** sets target constraint */
    public void setLayoutConstraints(int index,LayoutConstraints constraints) {
        super.setLayoutConstraints(index, constraints);
        constraints.addPropertyChangeListener(pcl);
    }

    // constraints supports ---------------------------------------------------

    /**
     * Associates constraints'.
     */
    public void associateConstraints() {
        for (int i = 0; i < container.getComponentCount(); i++) {
            LayoutConstraints lc = new CardLayoutConstraints();
            lc.setConstraints(String.valueOf(i));

            cache.put(String.valueOf(i), lc);
        }
    }
}

/* */
