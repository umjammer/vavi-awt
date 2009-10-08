/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;


/**
 * {@link vavi.swing.JFileChooserHistoryComboBox} のプロパティエディタです．
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 010823 nsano initial version <br>
 *          1.00 010830 nsano let it beans <br>
 *          1.01 020515 nsano fix <br>
 */
public class JFileChooserHistoryComboBoxEditor extends PropertyEditorSupport implements PropertyEditor {

    /**
     * カスタムエディタをサポートします．
     */
    public boolean supportsCustomEditor() {
        return false;
    }
}

/* */
