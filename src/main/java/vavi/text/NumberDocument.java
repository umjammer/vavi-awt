/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.text;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;


/**
 * A text document which will reject any characters that are not digits.
 *
 * @author Mark Davidson
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 1.10 990923 original version <br>
 *          1.11 020516 nsano repackage <br>
 */
public class NumberDocument extends PlainDocument {
    /** */
    public void insertString(int offs, String str, AttributeSet atts)
        throws BadLocationException {
        if (!Character.isDigit(str.charAt(0))) {
            return;
        }
        super.insertString(offs, str, atts);
    }
}

/* */
