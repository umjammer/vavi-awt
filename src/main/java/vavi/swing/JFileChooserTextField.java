/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing;

import java.io.File;

import javax.swing.JTextField;


/**
 * ファイル名を入力するためのコンポーネントです． テキストフィールドに"参照"ボタンがついています．
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 010823 nsano initial version <br>
 *          0.10 020427 nsano repackage <br>
 *          0.11 020427 nsano refine <br>
 *          0.12 020428 nsano refine ResourceBundle <br>
 *          0.20 020503 nsano separate base class <br>
 *          0.30 020504 nsano deprecate Text setter, getter <br>
 *          0.31 020515 nsano fix getSelectedFile <br>
 *          0.32 020515 nsano revive Text setter, getter <br>
 */
public class JFileChooserTextField extends JFileChooserField {

    /**
     * テキストフィールド付きファイルチューザを構築します．
     */
    public JFileChooserTextField() {
        super();
    }

    /**
     * テキストフィールド付きファイルチューザを構築します．
     */
    public JFileChooserTextField(File file) {
        super(file);
    }

    /** テキストフィールド */
    protected void setPathFieldImpl() {
        pathField = new JTextField(20);
    }

    /** テキストフィールドのアクションリスナ */
    protected void addActionListenerImpl() {
        ((JTextField) pathField).addActionListener(pathFieldActionListener);
    }

    /**
     * 文字列をテキストフィールドに設定します．
     * 
     * @param text 文字列
     */
    protected void setTextImpl(String text) {
        ((JTextField) pathField).setText(text);
    }

    /**
     * テキストフィールドに表示されている文字列を返します．
     * 
     * @return 表示されている文字列
     */
    protected String getTextImpl() {
        return ((JTextField) pathField).getText();
    }

    /**
     * ファイルをテキストフィールドに設定します．
     * 
     * @param file ファイル
     */
    protected void setSelectedFileImpl(File file) {
        setTextImpl(file.toString());
    }

    /**
     * テキストフィールドに表示されているファイルを返します．
     * 
     * @return 表示されているファイル
     */
    protected File getSelectedFileImpl() {
        String path = getTextImpl();
        return path == null ? null : new File(path);
    }
}

/* */
