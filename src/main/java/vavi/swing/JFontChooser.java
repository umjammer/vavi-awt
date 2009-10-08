/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.plaf.FontUIResource;

import vavi.swing.fontchooser.DefaultFontEditor;
import vavi.swing.fontchooser.FontEditor;
import vavi.util.ClassUtil;
import vavi.util.Debug;


/**
 * JFontChooser
 * <p>
 * JFontChooser.properties にデフォルトのフォントエディタとデフォルトの フォントを指定できます．
 * <p>
 * 
 * <pre>
 * 
 *   editorClass	フォントエディタのクラス
 *   font	フォントのプロパティ
 *  
 * </pre>
 * 
 * @depends /vavi/swing/resource${I18N}.properties
 * @depends /vavi/swing/JFontChooser.properties
 * 
 * @thanks <A HREF="mailto:emeade@teknolsut.com">Erik Meade</A>
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020502 nsano initial version <br>
 *          0.10 020517 nsano separate UI <br>
 *          0.11 020517 nsano FontEditor to be specifiable <br>
 *          0.12 020517 nsano the default font to be specifiable <br>
 */
public class JFontChooser extends JComponent {
    /** */
    private static final ResourceBundle rb = ResourceBundle.getBundle("vavi.swing.resource", Locale.getDefault());

    /** Return value if cancel is chosen. */
    public static final int CANCEL_OPTION = 1;

    /** Return value if approve (yes, ok) is chosen. */
    public static final int APPROVE_OPTION = 0;

    /** Return value if an error occured. */
    public static final int ERROR_OPTION = -1;

    /** */
    private int returnValue = ERROR_OPTION;

    /** */
    private String dialogTitle = null;

    /** */
    private JDialog dialog = null;

    /** */
    private FontEditor fontEditor;

    /** */
    private static Font defaultFont;

    /** */
    public JFontChooser() {
        this(defaultFont);
    }

    /** */
    public JFontChooser(Font font) {
        setSelectedFont(font);

        setLayout(new BorderLayout());

        add(fontEditor.getFontEditorComponent());

        // buttons
        JPanel panel = new JPanel();
        JButton button = new JButton(rb.getString("common.button.ok.text"));
        button.addActionListener(okListener);
        panel.add(button);
        button = new JButton(rb.getString("common.button.cancel.text"));
        button.addActionListener(cancelListener);
        panel.add(button);
        add(BorderLayout.SOUTH, panel);
    }

    /** */
    private ActionListener okListener = new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            returnValue = APPROVE_OPTION;
            if (dialog != null) {
                dialog.setVisible(false);
            }
        }
    };

    /** */
    private ActionListener cancelListener = new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            returnValue = CANCEL_OPTION;
            if (dialog != null) {
                dialog.setVisible(false);
            }
        }
    };

    /** */
    public void setSelectedFont(Font font) {
        fontEditor.setSelectedFont(font);
    }

    /** */
    public Font getSelectedFont() {
        return fontEditor.getSelectedFont();
    }

    /** */
    public int showDialog(Component parent) throws HeadlessException {

        dialog = createDialog(parent);
        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                returnValue = CANCEL_OPTION;
            }
        });
        returnValue = ERROR_OPTION;

        dialog.setVisible(true);
        dialog.dispose();
        dialog = null;
        return returnValue;
    }

    /** */
    protected JDialog createDialog(Component parent) throws HeadlessException {
        Frame frame = parent instanceof Frame ? (Frame) parent : (Frame) SwingUtilities.getAncestorOfClass(Frame.class, parent);

        String title = dialogTitle == null ? "" : dialogTitle;

        JDialog dialog = new JDialog(frame, title, true);

        Container contentPane = dialog.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(this, BorderLayout.CENTER);

        dialog.pack();
        dialog.setLocationRelativeTo(parent);

        return dialog;
    }

    /** */
    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    /**
     */
    public String getDialogTitle() {
        return dialogTitle;
    }

    // -------------------------------------------------------------------------

    /** */
    {
        try {
            @SuppressWarnings("unchecked")
            Class<FontEditor> clazz = (Class<FontEditor>) Class.forName(props.getProperty("editorClass"));
            fontEditor = clazz.newInstance();
        } catch (Exception e) {
Debug.println(Level.SEVERE, e);
            fontEditor = new DefaultFontEditor();
        }
    }

    /** */
    private static final Properties props = new Properties();

    /** */
    static {
        final Class<?> c = JFontChooser.class;
        final String path = "JFontChooser.properties";

        try {
            props.load(c.getResourceAsStream(path));

            String args = props.getProperty("font");
            defaultFont = (Font) ClassUtil.newInstance("javax.swing.plaf.FontUIResource",
                                                       "java.lang.String, int, int", args);
            // Debug.println(defaultFont + ", " + args);
        } catch (Exception e) {
Debug.println(Level.SEVERE, e);
            defaultFont = new FontUIResource("Dialog", Font.BOLD, 12);
        }
    }

    // -------------------------------------------------------------------------

    /** */
    public static void main(String[] args) {
        JFontChooser fc = new JFontChooser();
        fc.setDialogTitle("フォントチューザ");
        int r = fc.showDialog(null);
System.err.println(r + ": " + fc.getSelectedFont());
        System.exit(0);
    }
}

/* */
