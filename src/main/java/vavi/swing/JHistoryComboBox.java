/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import vavi.awt.dnd.Droppable;
import vavi.util.Debug;

import static java.lang.System.getLogger;


/**
 * A combo box with history.
 * Supports drag and drop of files from Explorer etc.
 *
 * TODO ~~History class?~~
 *      ~~Retrieving History~~
 *      Delete History
 *      ~~Is DnD something specified in the UI?~~
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020503 nsano initial version <br>
 *          0.01 021222 nsano try native DnD <br>
 *          0.02 021223 nsano fix native DnD <br>
 */
public class JHistoryComboBox extends JComboBox<String> {

    private static final Logger logger = getLogger(JHistoryComboBox.class.getName());

    private final JTextField editor;

    /** DnD enabled */
    public JHistoryComboBox() {
        this(true);
    }

    /** */
    public JHistoryComboBox(boolean enableDnD) {

        editor = (JTextField) this.getEditor().getEditorComponent();

        @SuppressWarnings("unused")
        JEditorPopupMenu popup = new JEditorPopupMenu(editor);

        this.setEditable(true);
        this.addActionListener(actionListener);

        if (enableDnD) {
            Droppable.makeComponentSinglePathDroppable(editor, path -> {
                setSelectedItem(path.toString());
                return true;
            });
        }
    }

    /** */
    public String getText() {
        return editor.getText();
    }

    /**
     * @param applicationId identity for prefs.
     */
    public void restoreHistory(String applicationId) {
        try {
            Preferences prefs = Preferences.userRoot().node(applicationId);
logger.log(Level.DEBUG, "prefs <<: " + prefs.name());
            for (int i = 0; i < prefs.keys().length; i++) {
                String value = prefs.get("item" + i, null);
logger.log(Level.TRACE, "prefs <<: " + ("item" + i) + ": " + value);
                addItem(value);
            }
            if (prefs.keys().length >0) {
                setSelectedIndex(0);
            }
        } catch (BackingStoreException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * @param applicationId identity for prefs.
     */
    public void saveHistory(String applicationId) {
        Preferences prefs = Preferences.userRoot().node(applicationId);
logger.log(Level.DEBUG, "prefs >>: " + prefs.name());
        try {
            prefs.clear();
        } catch (BackingStoreException e) {
            e.printStackTrace(System.err);
        }
        int c = 0;
        if (getSelectedIndex() >= 0) {
            prefs.put("item0", getItemAt(getSelectedIndex()));
            c++;
        }
        Set<String> set = new HashSet<>();
        for (int i = 0; i < getItemCount(); i++) {
            if (!(getSelectedIndex() >= 0 && i == getSelectedIndex())) {
                if (getItemAt(i) != null) {
                    set.add(getItemAt(i));
                }
            }
        }
        for (String s : set) {
logger.log(Level.DEBUG, "prefs >>: item" + c + ": " + s);
            prefs.put("item" + c, s);
            c++;
        }
    }

    //

    /** */
    private final ActionListener actionListener = ev -> {
        String item = (String) getSelectedItem();
        if (item == null || item.isEmpty()) {
            return;
        }

        for (int i = 0; i < getItemCount(); i++) {
            if (item.equals(getItemAt(i)))
                return;
        }
logger.log(Level.TRACE, Debug.getCallerMethod() + ": " + item);
        insertItemAt(item, 0);
    };

    /** backup for this combo */
    private final List<MouseListener> myMouseListeners = new ArrayList<>();
    /** backup for editor */
    private final List<MouseListener> editorMouseListeners = new ArrayList<>();
    /** backup for drop-down button */
    private final List<MouseListener> buttonMouseListeners = new ArrayList<>();

    /**
     * TODO still drop-down works...
     * @see "https://stackoverflow.com/a/62161500"
     */
    @Override
    public void setEnabled(boolean isEnabled) {
        super.setEnabled(isEnabled);
        setEnabledMouseListeners(this, isEnabled, myMouseListeners);

        editor.setEnabled(isEnabled);
        setEnabledMouseListeners(editor, isEnabled, editorMouseListeners);

        for (Component c : getComponents()) {
            if (c instanceof AbstractButton ab) {
                ab.setEnabled(isEnabled);
                setEnabledMouseListeners(ab, isEnabled, buttonMouseListeners);
            }
        }
    }

    /** */
    private static void setEnabledMouseListeners(JComponent component, boolean isEnabled, List<MouseListener> backup) {
        if (isEnabled) {
            for (MouseListener listener : backup) {
                component.addMouseListener(listener);
            }
            backup.clear();
        } else {
            backup.clear();
            for (MouseListener listener : component.getMouseListeners()) {
                backup.add(listener);
                component.removeMouseListener(listener);
            }
        }
    }
}
