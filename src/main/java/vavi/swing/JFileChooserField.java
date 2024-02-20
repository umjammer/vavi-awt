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
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import vavi.util.Debug;


/**
 * ファイル名を入力するためのコンポーネントの基本クラスです．
 * 入力フィールドに"参照"ボタンがついています．
 * 現在のところファイルチューザとテキストフィールドの拡張とみなしています．
 *
 * @event PropertyChangeEvent("text",,String)
 * @event PropertyChangeEvent("selectedFile",,File)
 *
 * @depends /vavi/swing/resource${I18N}.properties
 *
 * @done Listener の見直し
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020503 nsano initial version <br>
 *          0.10 020504 nsano deprecate Text setter, getter <br>
 *          0.20 020515 nsano revive Text setter, getter <br>
 *          0.21 020603 nsano refine <br>
 *          0.22 020618 nsano fix firePropertyChange <br>
 */
public abstract class JFileChooserField extends JComponent {

    /** */
    private static final ResourceBundle rb = ResourceBundle.getBundle("vavi.swing.resource", Locale.getDefault());

    /** ファイルチューザ */
    private JFileChooser chooser;

    /** filechooser's approve button title */
    private String title;

    /** ファイルチューザ起動ボタン */
    private JButton selectButton;

    /** 入力フィールド */
    protected JComponent pathField;

    /**
     * 入力フィールド付きファイルチューザのコンポーネントを構築します．
     */
    public JFileChooserField(File file) {
        this();
        setSelectedFile(file);
    }

    /**
     * 入力フィールド付きファイルチューザのコンポーネントを構築します．
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
     * #pathField にコンポーネントのインスタンスを設定して下さい．
     */
    protected abstract void setPathFieldImpl();

    /**
     * #pathField に #pathFieldActionListener を add してください． JComponent には
     * addActionListener は無い．
     */
    protected abstract void addActionListenerImpl();

    /** フィールドの処理 TODO text only でいいのか？ */
    protected ActionListener pathFieldActionListener = ev -> {
Debug.println(Level.FINER, ev.getSource().getClass().getName());
        setText(getText());
    };

    /** 選択ボタンの処理 */
    private final ActionListener selectButtonActionListener = ev -> {
Debug.println(Level.FINER, ev.getSource().getClass().getName());
        int returnValue = chooser.showDialog(getParent(), title);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            setSelectedFile(chooser.getSelectedFile());
        }
    };

    /**
     * #pathField に文字列を設定して下さい．
     *
     * @param text 文字列
     */
    protected abstract void setTextImpl(String text);

    /**
     * #pathField から取得した文字列を返して下さい．
     *
     * @return 表示されている文字列
     */
    protected abstract String getTextImpl();

    /**
     * #pathField にファイルを設定して下さい．
     *
     * @param file ファイル
     */
    protected abstract void setSelectedFileImpl(File file);

    /**
     * #pathField から取得したファイルを返して下さい．
     *
     * @return 表示されているファイル
     */
    protected abstract File getSelectedFileImpl();

    // Field としての機能 -----------------------------------------------------

    /**
     * 文字列を設定します．
     */
    public void setText(String text) {
        String oldString = getTextImpl();
        setTextImpl(text);
        firePropertyChange("text", oldString, getText());
    }

    /**
     * 文字列を取得します．
     */
    public String getText() {
        return getTextImpl();
    }

    // FileChooser としての機能 -----------------------------------------------

    /**
     * 選択されたファイルを設定します． ファイルチューザのカレントディレクトリは指定したファイルの ディレクトリになります．
     *
     * @param file ファイル
     */
    public void setSelectedFile(File file) {
        File oldFile = getSelectedFileImpl();
        setSelectedFileImpl(file);
        firePropertyChange("selectedFile", oldFile, getSelectedFile());
    }

    /**
     * 選択されたファイルを返します．
     *
     * @return 表示されているファイル
     */
    public File getSelectedFile() {
        return getSelectedFileImpl();
    }

    /** ファイルチューザのカレントディレクトリを設定します． */
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
Debug.println(Level.FINER, defaultPath);
        chooser.setCurrentDirectory(defaultPath);
    }

//    /** ファイルチューザのカレントディレクトリを取得します． */
//    private File getCurrentDirectory() {
//        return chooser.getCurrentDirectory();
//    }

    /**
     * ファイルチューザのファイル選択モードを設定します．
     *
     * @param selectionMode ファイル選択タイプ
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

    // -------------------------------------------------------------------------

    /**
     * すべてのコンポーネントに setEnabled するようにオーバーライドします．
     */
    public void setBackground(Color background) {
        super.setBackground(background);
        pathField.setBackground(background);
        selectButton.setBackground(background);
    }

    /**
     * すべてのコンポーネントに setEnabled するようにオーバーライドします．
     */
    public void setForeground(Color foreground) {
        super.setForeground(foreground);
        pathField.setForeground(foreground);
        selectButton.setForeground(foreground);
    }

    /**
     * すべてのコンポーネントに setEnabled するようにオーバーライドします．
     */
    public void setFont(Font font) {
        super.setFont(font);
        pathField.setFont(font);
        selectButton.setFont(font);
    }

    /**
     * すべてのコンポーネントに setEnabled するようにオーバーライドします．
     */
    public void setEnabled(boolean isEnabled) {
        super.setEnabled(isEnabled);
        pathField.setEnabled(isEnabled);
        selectButton.setEnabled(isEnabled);
    }

    /**
     * すべてのコンポーネントに add するようにオーバーライドします．
     *
     * @param l マウスリスナ
     */
    public synchronized void addMouseListener(MouseListener l) {
        super.addMouseListener(l);
        pathField.addMouseListener(l);
        selectButton.addMouseListener(l);
    }

    /**
     * すべてのコンポーネントに add するようにオーバーライドします．
     *
     * @param l マウスモーションリスナ
     */
    public synchronized void addMouseMotionListener(MouseMotionListener l) {
        super.addMouseMotionListener(l);
        pathField.addMouseMotionListener(l);
        selectButton.addMouseMotionListener(l);
    }

    /**
     * すべてのコンポーネントに remove するようにオーバーライドします．
     *
     * @param l マウスリスナ
     */
    public synchronized void removeMouseListener(MouseListener l) {
        super.removeMouseListener(l);
        pathField.removeMouseListener(l);
        selectButton.removeMouseListener(l);
    }

    /**
     * すべてのコンポーネントに remove するようにオーバーライドします．
     *
     * @param l マウスモーションリスナ
     */
    public synchronized void removeMouseMotionListener(MouseMotionListener l) {
        super.removeMouseMotionListener(l);
        pathField.removeMouseMotionListener(l);
        selectButton.removeMouseMotionListener(l);
    }
}

/* */
