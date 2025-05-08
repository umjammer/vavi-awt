/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing;

import java.io.File;


/**
 * A component for inputting a file name.
 * The combo box with history has a "Browse" button.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020503 nsano initial version <br>
 *          0.10 020504 nsano deprecate Text setter, getter <br>
 *          0.11 020515 nsano fix getSelectedFile <br>
 *          0.12 020515 nsano revive Text setter, getter <br>
 */
public class JFileChooserHistoryComboBox extends JFileChooserField {

    /**
     * Constructs a file chooser with a combo box and history.
     */
    public JFileChooserHistoryComboBox() {
        super();
    }

    /**
     * Constructs a file chooser with a combo box and history.
     */
    public JFileChooserHistoryComboBox(File file) {
        super(file);
    }

    /** Combo Box with History */
    @Override
    protected void setPathFieldImpl() {
        pathField = new JHistoryComboBox();
    }

    /** Combobox Action Listener */
    @Override
    protected void addActionListenerImpl() {
        ((JHistoryComboBox) pathField).addActionListener(pathFieldActionListener);
    }

    /**
     * Sets the string in the combo box.
     *
     * @param text String
     */
    @Override
    protected void setTextImpl(String text) {
        ((JHistoryComboBox) pathField).setSelectedItem(text);
    }

    /**
     * Returns the string selected in the combo box.
     *
     * @return The selected string
     */
    @Override
    protected String getTextImpl() {
        return (String) ((JHistoryComboBox) pathField).getSelectedItem();
    }

    /**
     * Set the file in the combo box.
     *
     * @param file file
     */
    @Override
    protected void setSelectedFileImpl(File file) {
        setTextImpl(file.toString());
    }

    /**
     * Returns the file selected in the combo box.
     *
     * @return Selected file
     */
    @Override
    protected File getSelectedFileImpl() {
        String path = getTextImpl();
        return path == null ? null : new File(path);
    }
}
