/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import static java.lang.System.getLogger;


/**
 * Base class for components that input file names.
 * The input field has a "Browse" button.
 * Currently I think of it as an extension of the file chooser and text field.
 *
 * @event PropertyChangeEvent("text",,String)
 * @event PropertyChangeEvent("selectedFile",,File)
 *
 * @depends /vavi/swing/resource${I18N}.properties
 *
 * @done Revisiting Listener
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020503 nsano initial version <br>
 *          0.10 020504 nsano deprecate Text setter, getter <br>
 *          0.20 020515 nsano revive Text setter, getter <br>
 *          0.21 020603 nsano refine <br>
 *          0.22 020618 nsano fix firePropertyChange <br>
 */
public abstract class JFileChooserField extends JComponent {

    private static final Logger logger = getLogger(JFileChooserField.class.getName());

    /** */
    private static final ResourceBundle rb = ResourceBundle.getBundle("vavi.swing.resource", Locale.getDefault());

    /** File Chooser */
    private JFileChooser chooser;

    /** filechooser's approve button title */
    private String title;

    /** File Chooser Launch Button */
    private final JButton selectButton;

    /** Input Field */
    protected JComponent pathField;

    /**
     * Constructs a file chooser component with an input field.
     */
    public JFileChooserField(File file) {
        this();
        setSelectedFile(file);
    }

    /**
     * Constructs a file chooser component with an input field.
     */
    public JFileChooserField() {

        this.setLayout(new BorderLayout());

        // filechooser
        chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        title = rb.getString("jFileChooser.dialog.title");

        // field
        setPathFieldImpl();
        addActionListenerImpl();
        this.add(pathField, BorderLayout.CENTER);

        // button
        selectButton = new JButton();
        selectButton.setText(rb.getString("jFileChooser.button.text"));
        selectButton.addActionListener(selectButtonActionListener);
        this.add(selectButton, BorderLayout.EAST);
    }

    /**
     * Set an instance of the component to #pathField.
     */
    protected abstract void setPathFieldImpl();

    /**
     * Add #pathFieldActionListener to #pathField. JComponent does not have addActionListener.
     */
    protected abstract void addActionListenerImpl();

    /** Processing the field TODO Is text only okay? */
    protected final ActionListener pathFieldActionListener = ev -> {
logger.log(Level.TRACE, ev.getSource().getClass().getName());
        setText(getText());
    };

    /** Select button handling */
    private final ActionListener selectButtonActionListener = ev -> {
logger.log(Level.TRACE, ev.getSource().getClass().getName());
        int returnValue = chooser.showDialog(getParent(), title);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            setSelectedFile(chooser.getSelectedFile());
        }
    };

    /**
     * Please set a string to #pathField.
     *
     * @param text String
     */
    protected abstract void setTextImpl(String text);

    /**
     * Please return the string obtained from #pathField.
     *
     * @return Displayed string
     */
    protected abstract String getTextImpl();

    /**
     * Please set the file in #pathField.
     *
     * @param file file
     */
    protected abstract void setSelectedFileImpl(File file);

    /**
     * #Return the file obtained from pathField.
     *
     * @return The displayed file
     */
    protected abstract File getSelectedFileImpl();

    // Function as a Field ----

    /**
     * Sets the string.
     */
    public void setText(String text) {
        String oldString = getTextImpl();
        setTextImpl(text);
        firePropertyChange("text", oldString, getText());
    }

    /**
     * Gets the string.
     */
    public String getText() {
        return getTextImpl();
    }

    // Functions as a FileChooser ----

    /**
     * Sets the selected file. The current directory of the file chooser
     * will be the directory of the specified file.
     *
     * @param file file
     */
    public void setSelectedFile(File file) {
        File oldFile = getSelectedFileImpl();
        setSelectedFileImpl(file);
        firePropertyChange("selectedFile", oldFile, getSelectedFile());
    }

    /**
     * Returns the selected file.
     *
     * @return The displayed file
     */
    public File getSelectedFile() {
        return getSelectedFileImpl();
    }

    /** Sets the current directory for the file chooser. */
    public void setCurrentDirectory(File file) {
        File defaultPath = null;
        if (file.exists()) {
            if (file.isFile())
                defaultPath = file.getParentFile();
            else
                defaultPath = file;
        } else {
            defaultPath = new File(System.getProperty("user.home"));
        }
logger.log(Level.TRACE, defaultPath);
        chooser.setCurrentDirectory(defaultPath);
    }

//    /** Gets the current directory of the file chooser. */
//    private File getCurrentDirectory() {
//        return chooser.getCurrentDirectory();
//    }

    /**
     * Sets the file selection mode of the file chooser.
     *
     * @param selectionMode File Selection Type
     */
    public void setFileSelectionMode(int selectionMode) {
        chooser.setFileSelectionMode(selectionMode);
    }

    /** */
    public void setDialogTitle(String title) {
        this.title = title;
    }

    /** */
    public void setFileFilter(FileFilter filter) {
        chooser.setFileFilter(filter);
    }

    // ----

    /**
     * Override setEnabled for all components.
     */
    @Override
    public void setBackground(Color background) {
        super.setBackground(background);
        pathField.setBackground(background);
        selectButton.setBackground(background);
    }

    /**
     * Override setEnabled for all components.
     */
    @Override
    public void setForeground(Color foreground) {
        super.setForeground(foreground);
        pathField.setForeground(foreground);
        selectButton.setForeground(foreground);
    }

    /**
     * Override setEnabled for all components.
     */
    @Override
    public void setFont(Font font) {
        super.setFont(font);
        pathField.setFont(font);
        selectButton.setFont(font);
    }

    /**
     * Override setEnabled for all components.
     */
    @Override
    public void setEnabled(boolean isEnabled) {
        super.setEnabled(isEnabled);
        pathField.setEnabled(isEnabled);
        selectButton.setEnabled(isEnabled);
    }

    /**
     * Override it to add to all components.
     *
     * @param l Mouse Listener
     */
    @Override
    public synchronized void addMouseListener(MouseListener l) {
        super.addMouseListener(l);
        pathField.addMouseListener(l);
        selectButton.addMouseListener(l);
    }

    /**
     * Override it to add to all components.
     *
     * @param l Mouse Motion Listener
     */
    @Override
    public synchronized void addMouseMotionListener(MouseMotionListener l) {
        super.addMouseMotionListener(l);
        pathField.addMouseMotionListener(l);
        selectButton.addMouseMotionListener(l);
    }

    /**
     * Override remove on all components.
     *
     * @param l Mouse Listener
     */
    @Override
    public synchronized void removeMouseListener(MouseListener l) {
        super.removeMouseListener(l);
        pathField.removeMouseListener(l);
        selectButton.removeMouseListener(l);
    }

    /**
     * Override remove on all components.
     *
     * @param l Mouse Motion Listener
     */
    @Override
    public synchronized void removeMouseMotionListener(MouseMotionListener l) {
        super.removeMouseMotionListener(l);
        pathField.removeMouseMotionListener(l);
        selectButton.removeMouseMotionListener(l);
    }
}
