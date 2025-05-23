/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.text.JTextComponent;


/**
 * This is the popup menu for editing the field.
 *
 * TODO Think carefully about the architecture
 *
 * @depends /vavi/swing/resource${I18N}.properties
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020503 nsano initial version <br>
 */
public class JEditorPopupMenu extends JPopupMenu {

    /** */
    private static final ResourceBundle rb =
        ResourceBundle.getBundle("vavi.swing.resource", Locale.getDefault());

    public static final String UNDO =
        rb.getString("common.menuItem.undo.text");
    public static final String REDO =
        rb.getString("common.menuItem.redo.text");
    public static final String CUT =
        rb.getString("common.menuItem.cut.text");
    public static final String COPY =
        rb.getString("common.menuItem.copy.text");
    public static final String PASTE =
        rb.getString("common.menuItem.paste.text");
    public static final String DELETE =
        rb.getString("common.menuItem.delete.text");
    public static final String SELECT_ALL =
        rb.getString("common.menuItem.selectAll.text");

    /** */
    private final Editable editor;

    /** TODO This place is also not so good. */
    static class JEditorComponent implements Editable {
        final JTextComponent tc;
        JEditorComponent(JTextComponent tc) {
            this.tc = tc;
        }
        @Override
        public void cut()       { tc.cut();                  }
        @Override
        public void copy()      { tc.copy();                 }
        @Override
        public void paste()     { tc.paste();                }
        @Override
        public void delete()    { tc.replaceSelection(null); }
        @Override
        public void selectAll() { tc.selectAll();            }
    }

    /** */
    public JEditorPopupMenu(JTextComponent tc) {
        this.editor = new JEditorComponent(tc);
        init();
        tc.addMouseListener(mouseListener);
    }

    /** */
    public JEditorPopupMenu(Editable editor) {
        this.editor = editor;
        init();
        ((JComponent) editor).addMouseListener(mouseListener);
    }

    private void init() {
//      this.add(undoAction);
//      this.addSeparator();
        this.add(cutAction);
        this.add(copyAction);
        this.add(pasteAction);
        this.add(deleteAction);
        this.addSeparator();
        this.add(selectAllAction);
    }

    /** Hmm, not so good... */
    private final MouseInputListener mouseListener = new MouseInputAdapter() {
        @Override
        public void mouseClicked(MouseEvent ev) {
            if (editor instanceof JEditorComponent) {
//                  if (ev.getClickCount() == 2) {
//                      editor.selectAll();
//                  } else
                if (SwingUtilities.isRightMouseButton(ev)) {
                    int x = ev.getX();
                    int y = ev.getY();
                    show(((JEditorComponent) editor).tc, x, y);
                }
            } else {
                if (SwingUtilities.isRightMouseButton(ev)) {
                    int x = ev.getX();
                    int y = ev.getY();
                    show((JComponent) editor, x, y);
                }
            }
        }
    };

    //

//    private Action undoAction = new AbstractAction(UNDO) {
//        public void actionPerformed(ActionEvent ev) {
//        }
//    };
//
//    private Action redoAction = new AbstractAction(REDO) {
//        public void actionPerformed(ActionEvent ev) {
//        }
//    };

    private final Action cutAction = new AbstractAction(CUT) {
        @Override
        public void actionPerformed(ActionEvent ev) {
            editor.cut();
        }
    };

    private final Action copyAction = new AbstractAction(COPY) {
        @Override
        public void actionPerformed(ActionEvent ev) {
            editor.copy();
        }
    };

    private final Action pasteAction = new AbstractAction(PASTE) {
        @Override
        public void actionPerformed(ActionEvent ev) {
            editor.paste();
        }
    };

    private final Action deleteAction = new AbstractAction(DELETE) {
        @Override
        public void actionPerformed(ActionEvent ev) {
            editor.delete();
        }
    };

    private final Action selectAllAction = new AbstractAction(SELECT_ALL) {
        @Override
        public void actionPerformed(ActionEvent ev) {
            editor.selectAll();
        }
    };
}
