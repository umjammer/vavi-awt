/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing;

import java.io.File;


/**
 * ファイル名を入力するためのコンポーネントです．
 * ヒストリ付きコンボボックスに"参照"ボタンがついています．
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020503 nsano initial version <br>
 *          0.10 020504 nsano deprecate Text setter, getter <br>
 *          0.11 020515 nsano fix getSelectedFile <br>
 *          0.12 020515 nsano revive Text setter, getter <br>
 */
public class JFileChooserHistoryComboBox extends JFileChooserField {

    /**
     * ヒストリ付きコンボボックス付きファイルチューザを構築します．
     */
    public JFileChooserHistoryComboBox() {
        super();
    }

    /**
     * ヒストリ付きコンボボックス付きファイルチューザを構築します．
     */
    public JFileChooserHistoryComboBox(File file) {
        super(file);
    }

    /** ヒストリ付きコンボボックス */
    protected void setPathFieldImpl() {
        pathField = new JHistoryComboBox();
    }

    /** コンボボックスのアクションリスナ */
    protected void addActionListenerImpl() {
        ((JHistoryComboBox) pathField).addActionListener(pathFieldActionListener);
    }

    /**
     * 文字列をコンボボックスに設定します．
     *
     * @param text 文字列
     */
    protected void setTextImpl(String text) {
        ((JHistoryComboBox) pathField).setSelectedItem(text);
    }

    /**
     * コンボボックスで選択されている文字列を返します．
     *
     * @return 選択されている文字列
     */
    protected String getTextImpl() {
        return (String) ((JHistoryComboBox) pathField).getSelectedItem();
    }

    /**
     * ファイルをコンボボックスに設定します．
     *
     * @param file ファイル
     */
    protected void setSelectedFileImpl(File file) {
        setTextImpl(file.toString());
    }

    /**
     * コンボボックスで選択されているファイルを返します．
     *
     * @return 選択されているファイル
     */
    protected File getSelectedFileImpl() {
        String path = getTextImpl();
        return path == null ? null : new File(path);
    }
}

/* */
