/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */
package vavi.swing.beaninfo;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.beans.FeatureDescriptor;
import java.beans.PropertyEditorSupport;
import javax.swing.JComponent;
import javax.swing.JPanel;


/**
 * Base class of all Swing based property editors.
 *
 * @author Tom Santos
 * @author Mark Davidson
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 1.30 000620       original version <br>
 *          1.31 020524 nsano refine <br>
 */
public class SwingEditorSupport extends PropertyEditorSupport {

    /**
     * Component which holds the editor. Subclasses are responsible for
     * instantiating this panel.
     */
    protected JPanel panel;

    protected static final Dimension LARGE_DIMENSION = new Dimension(150, 20);
    protected static final Dimension MEDIUM_DIMENSION = new Dimension(120, 20);
    protected static final Dimension SMALL_DIMENSION = new Dimension(50, 20);
    protected static final Insets BUTTON_MARGIN = new Insets(0, 0, 0, 0);

    /**
     * Returns the panel responsible for rendering the PropertyEditor.
     */
    @Override
    public Component getCustomEditor() {
        return panel;
    }

    @Override
    public boolean supportsCustomEditor() {
        return true;
    }

    // layout stuff
    protected final void setAlignment(JComponent c){
        c.setAlignmentX(Component.CENTER_ALIGNMENT);
        c.setAlignmentY(Component.CENTER_ALIGNMENT);
    }

    /**
     * For property editors that must be initialized with values from
     * the property descriptor.
     */
    public void init(FeatureDescriptor descriptor)  {
    }
}
