/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.beaninfo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JPanel;

import vavi.swing.fontchooser.SmallFontEditor;


/**
 * A Font property editor. Mostly designed by Chris Ryan.
 *
 * @depends /vavi/swing/resource${I18N}.properties
 *
 * @author Tom Santos
 * @author Mark Davidson
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 1.40 010103       original version <br>
 *          1.41 020516 nsano i18n <br>
 *          2.00 020517 nsano separate FontEditor <br>
 */
public class SwingFontEditor extends SwingEditorSupport {

    /** リソースバンドル */
    private static final ResourceBundle rb =
    ResourceBundle.getBundle("vavi.swing.resource", Locale.getDefault());

    private final static String sampleText =
        rb.getString("jFontChooser.sample.text");

    /** */
    private final SmallFontEditor fontEditor;

    /** */
    public SwingFontEditor() {
        fontEditor = new SmallFontEditor();

        panel = new JPanel(new BorderLayout());
        panel.add(fontEditor.getFontEditorComponent());

        plug();
    }

    /** */
    private final PropertyChangeListener pcl = ev -> {
        if ("font".equals(ev.getPropertyName())) {
            SwingFontEditor.super.setValue(ev.getNewValue());
        }
    };

    // PropertyEditor interface definitions -----------------------------------

    private void plug() {
        Component editorComponent = fontEditor.getFontEditorComponent();
        editorComponent.addPropertyChangeListener(pcl);
    }

    private void unplug() {
        Component editorComponent = fontEditor.getFontEditorComponent();
        editorComponent.removePropertyChangeListener(pcl);
    }

    /** TODO unplug 要るかどうか要検証 */
    @Override
    public void setValue(Object value) {
        super.setValue(value);
        unplug();
        fontEditor.setSelectedFont((Font) value);
        plug();
    }

    @Override
    public boolean isPaintable() {
        return true;
    }

    @Override
    public void paintValue(Graphics g, Rectangle rect) {
    // Silent noop.
    Font oldFont = g.getFont();
    g.setFont((Font) getValue());
    FontMetrics fm = g.getFontMetrics();
    int vpad = (rect.height - fm.getAscent())/2;
    g.drawString(sampleText, 0, rect.height-vpad);
    g.setFont(oldFont);
    }

    @Override
    public String getJavaInitializationString() {
        Font font = (Font) getValue();

        return "new java.awt.Font(\"" +
            font.getFamily() + "\", " +
            font.getStyle() + ", " +
            font.getSize() + ")";
    }
}
