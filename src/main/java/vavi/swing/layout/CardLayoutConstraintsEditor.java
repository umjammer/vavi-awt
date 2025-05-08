/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.CardLayout;
import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import static java.lang.System.getLogger;


/**
 * CardLayoutConstraintEditor.
 *
 * TODO get constraint when initializing components in the container.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020528 nsano initial version <br>
 */
public class CardLayoutConstraintsEditor
    extends AbstractLayoutConstraintsEditor {

    private static final Logger logger = getLogger(CardLayoutConstraintsEditor.class.getName());

    /** Creates card constraints editor */
    public CardLayoutConstraintsEditor(CardLayout layout) {
        super(layout);
    }

    // ----

    /** for table model setter */
    private final PropertyChangeListener pcl = ev -> {
        Component component = container.getComponent(index);
logger.log(Level.DEBUG, index);
        layout.removeLayoutComponent(component);
        layout.addLayoutComponent(component, ev.getNewValue());

        layout.layoutContainer(container);
    };

    /** sets target constraint */
    @Override
    public void setLayoutConstraints(int index, LayoutConstraints constraints) {
        super.setLayoutConstraints(index, constraints);
        constraints.addPropertyChangeListener(pcl);
    }

    // constraints supports ----

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
