/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.beaninfo;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import vavi.swing.JFileChooserTextField;
import vavi.util.Debug;
import vavi.util.RegexFileFilter;


/**
 * A property editor for editing Icon.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020527 nsano initial version <br>
 */
public class SwingIconEditor extends SwingEditorSupport {

    /** */
    private JFileChooserTextField chooser;

    /** */
    public SwingIconEditor() {
        // TODO use imageio
        RegexFileFilter filter = new RegexFileFilter();
        filter.addPattern(".+\\.(gif|GIF)");
        filter.addPattern(".+\\.(jpeg|JPEG|jpg|JPG)");
        filter.addPattern(".+\\.(png|PNG)");
        filter.setDescription("Image File");

        chooser = new JFileChooserTextField();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(filter);
        chooser.addPropertyChangeListener(pcl);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(chooser);
    }

    /** */
    private PropertyChangeListener pcl = ev -> {
        String name = ev.getPropertyName();
        String path = null;
        if ("text".equals(name)) {
            path = chooser.getText();
        } else if ("selectedFile".equals(name)) {
            path = chooser.getSelectedFile().getAbsolutePath();
        }

        if (path == null) {
//Debug.println("null image");
            return;
        }

        try {
            MediaTracker tracker = new MediaTracker(panel);
            Image image = Toolkit.getDefaultToolkit().getImage(path);
            tracker.addImage(image, 0);
            tracker.waitForAll();
            setValue(new ImageIcon(image));
        } catch (Exception e) {
//Debug.printStackTrace(e);
Debug.println(Level.SEVERE, e);
        }
    };
}

/* */
