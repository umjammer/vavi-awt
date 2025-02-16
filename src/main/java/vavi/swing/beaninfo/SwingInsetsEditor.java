/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.beaninfo;

import java.awt.Insets;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import vavi.text.NumberDocument;


/**
 * An editor for editing Insets.
 *
 * @author Mark Davidson
 * @version original version <br>
 */
public class SwingInsetsEditor extends SwingEditorSupport {

    private final JTextField topTF;
    private final JTextField leftTF;
    private final JTextField bottomTF;
    private final JTextField rightTF;

    public SwingInsetsEditor() {
        topTF = new JTextField();
        topTF.setDocument(new NumberDocument());
        leftTF = new JTextField();
        leftTF.setDocument(new NumberDocument());
        bottomTF = new JTextField();
        bottomTF.setDocument(new NumberDocument());
        rightTF = new JTextField();
        rightTF.setDocument(new NumberDocument());

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new JLabel("Top: "));
        panel.add(topTF);
        panel.add(new JLabel("Left: "));
        panel.add(leftTF);
        panel.add(new JLabel("Bottom: "));
        panel.add(bottomTF);
        panel.add(new JLabel("Right: "));
        panel.add(rightTF);
    }

    @Override
    public void setValue(Object value)  {
        super.setValue(value);

        Insets insets = (Insets) value;

        topTF.setText(Integer.toString(insets.top));
        leftTF.setText(Integer.toString(insets.left));
        bottomTF.setText(Integer.toString(insets.bottom));
        rightTF.setText(Integer.toString(insets.right));
    }

    @Override
    public Object getValue()  {
        int top = Integer.parseInt(topTF.getText());
        int left = Integer.parseInt(leftTF.getText());
        int bottom = Integer.parseInt(bottomTF.getText());
        int right = Integer.parseInt(rightTF.getText());

        return new Insets(top, left, bottom, right);
    }
}
