/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.beaninfo;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;


/**
 * A PropertyEditor for editing a Dimension object.
 * 
 * @author Mark Davidson
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version original version <br>
 *          0.01 020516 nsano use JSpinner <br>
 */
public class SwingDimensionEditor extends SwingEditorSupport {

    /** リソースバンドル */
//    private static final ResourceBundle rb =
//        ResourceBundle.getBundle("vavi.swing.resource", Locale.getDefault());

    private JSpinner widthSpinner;
    private JSpinner heightSpinner;
    
    public SwingDimensionEditor() {
        widthSpinner  = new JSpinner();
        heightSpinner = new JSpinner();
        
        JLabel wlabel = new JLabel("width"  + ": ");
        JLabel hlabel = new JLabel("height" + ": ");
        
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(wlabel);
        panel.add(widthSpinner);
        panel.add(Box.createRigidArea(new Dimension(5,0)));
        panel.add(hlabel);
        panel.add(heightSpinner);
    }
    
    public void setValue(Object value)  {
        super.setValue(value);
        
        Dimension dim = (Dimension) value;
        
        widthSpinner .setValue(new Integer(dim.width));
        heightSpinner.setValue(new Integer(dim.height));
    }
    
    public Object getValue()  {
        int width  = ((Integer) widthSpinner .getValue()).intValue();
        int height = ((Integer) heightSpinner.getValue()).intValue();
        
        return new Dimension(width, height);
    }
}

/* */
