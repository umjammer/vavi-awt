/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.event;

import java.util.EventListener;


/**
 * The editor listener interface.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010820 nsano initial version <br>
 *          0.10 010906 nsano be generic <br>
 *          0.11 020503 nsano repackage <br>
 *          0.12 020510 nsano repackage <br>
 *          0.20 020510 nsano deprecate open/close <br>
 */
public interface EditorListener extends EventListener {

//    /**
//     * Called when the editor is opened.
//     * @param ev Editor Events
//     */
//    void editorOpened(EditorEvent ev);

    /**
     * Called when the editor is updated.
     * @param ev Editor Events
     */
    void editorUpdated(EditorEvent ev);

//    /**
//     * Called when the editor has finished.
//     * @param ev Editor Events
//     */
//    void editorClosed(EditorEvent ev);
}
