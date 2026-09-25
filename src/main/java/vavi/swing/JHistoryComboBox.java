/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.plaf.basic.ComboPopup;

import vavi.awt.dnd.Droppable;
import vavi.util.Debug;

import static java.lang.System.getLogger;


/**
 * A combo box with history.
 * Supports drag and drop of files from Explorer etc.
 * <p>
 * Typing {@code ctrl + DELETE} (on mac {@code ctrl + fn + delete}) while the drop-down list is shown
 * removes the highlighted item from the history.
 *
 * TODO ~~History class?~~
 *      ~~Retrieving History~~
 *      ~~Delete History~~
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
        editor.addKeyListener(keyListener);

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
     * Restores the history saved by {@link #saveHistory(String)}.
     * The first item (the last selected one) becomes selected.
     * Items already in this combo box are not duplicated.
     *
     * @param applicationId identity for prefs.
     */
    public void restoreHistory(String applicationId) {
        Preferences prefs = Preferences.userRoot().node(applicationId);
logger.log(Level.DEBUG, "prefs <<: " + prefs.name());
        Set<String> items = new LinkedHashSet<>();
        for (int i = 0; ; i++) {
            String value = prefs.get("item" + i, null);
            if (value == null) {
                break;
            }
logger.log(Level.TRACE, "prefs <<: " + ("item" + i) + ": " + value);
            if (!value.isEmpty()) {
                items.add(value);
            }
        }
        for (String item : items) {
            if (indexOf(item) < 0) {
                addItem(item);
            }
        }
        if (!items.isEmpty()) {
            setSelectedItem(items.iterator().next());
        }
    }

    /**
     * Saves the history, the selected item is saved as the first one, the others keep their order.
     *
     * @param applicationId identity for prefs.
     */
    public void saveHistory(String applicationId) {
        Preferences prefs = Preferences.userRoot().node(applicationId);
logger.log(Level.DEBUG, "prefs >>: " + prefs.name());
        Set<String> items = new LinkedHashSet<>();
        Object selected = getSelectedItem();
        if (selected instanceof String s && !s.isEmpty()) {
            items.add(s);
        }
        for (int i = 0; i < getItemCount(); i++) {
            String item = getItemAt(i);
            if (item != null && !item.isEmpty()) {
                items.add(item);
            }
        }
        try {
            prefs.clear();
            int c = 0;
            for (String item : items) {
logger.log(Level.DEBUG, "prefs >>: item" + c + ": " + item);
                prefs.put("item" + c, item);
                c++;
            }
            prefs.flush();
        } catch (BackingStoreException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    /**
     * Removes the item from the history.
     *
     * @return true when the item was removed
     */
    public boolean removeHistory(String item) {
        int index = indexOf(item);
        if (index < 0) {
            return false;
        }
        removeItemAt(index);
        return true;
    }

    /** @return -1 when not found */
    private int indexOf(String item) {
        for (int i = 0; i < getItemCount(); i++) {
            if (item.equals(getItemAt(i))) {
                return i;
            }
        }
        return -1;
    }

    /** @return the list in the drop-down popup, null when the ui doesn't provide it */
    private JList<?> getPopupList() {
        if (getUI().getAccessibleChild(this, 0) instanceof ComboPopup popup) {
            return popup.getList();
        }
        return null;
    }

    /** ctrl + DELETE (mac: ctrl + fn + delete) on the drop-down list removes the highlighted item */
    private final KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent ev) {
            if (!isPopupVisible() ||
                    ev.getKeyCode() != KeyEvent.VK_DELETE ||
                    (ev.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == 0) {
                return;
            }
            JList<?> list = getPopupList();
            int index = list != null ? list.getSelectedIndex() : getSelectedIndex();
            if (index < 0 || index >= getItemCount()) {
                return;
            }
logger.log(Level.DEBUG, "remove history: " + getItemAt(index));
            removeItemAt(index);
            ev.consume();
            // popup size is not updated automatically
            hidePopup();
            if (getItemCount() > 0) {
                showPopup();
            }
        }
    };

    //

    /** */
    private final ActionListener actionListener = ev -> {
        String item = (String) getSelectedItem();
        if (item == null || item.isEmpty()) {
            return;
        }

        if (indexOf(item) >= 0) {
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
