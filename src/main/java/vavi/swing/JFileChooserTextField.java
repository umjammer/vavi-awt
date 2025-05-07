/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing;

import java.io.File;

import javax.swing.JTextField;


/**
 * This is a component for entering a file name. It has a text field and a "Browse" button.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010823 nsano initial version <br>
 *          0.10 020427 nsano repackage <br>
 *          0.11 020427 nsano refine <br>
 *          0.12 020428 nsano refine ResourceBundle <br>
 *          0.20 020503 nsano separate base class <br>
 *          0.30 020504 nsano deprecate Text setter, getter <br>
 *          0.31 020515 nsano fix getSelectedFile <br>
 *          0.32 020515 nsano revive Text setter, getter <br>
 */
public class JFileChooserTextField extends JFileChooserField {

    /**
     * Constructs a file chooser with a text field.
     */
    public JFileChooserTextField() {
        super();
    }

    /**
     * Constructs a file chooser with a text field.
     */
    public JFileChooserTextField(File file) {
        super(file);
    }

    /** Text Field */
    @Override
    protected void setPathFieldImpl() {
        pathField = new JTextField(20);
    }

    /** Text Field Action Listener */
    @Override
    protected void addActionListenerImpl() {
        ((JTextField) pathField).addActionListener(pathFieldActionListener);
    }

    /**
     * Sets the string in the text field.
     *
     * @param text String
     */
    @Override
    protected void setTextImpl(String text) {
        ((JTextField) pathField).setText(text);
    }

    /**
     * Returns the string displayed in the text field.
     *
     * @return Displayed string
     */
    @Override
    protected String getTextImpl() {
        return ((JTextField) pathField).getText();
    }

    /**
     * Set the file in the text field.
     *
     * @param file file
     */
    @Override
    protected void setSelectedFileImpl(File file) {
        setTextImpl(file.toString());
    }

    /**
     * Returns the file displayed in the text field.
     *
     * @return The displayed file
     */
    @Override
    protected File getSelectedFileImpl() {
        String path = getTextImpl();
        return path == null ? null : new File(path);
    }
}
