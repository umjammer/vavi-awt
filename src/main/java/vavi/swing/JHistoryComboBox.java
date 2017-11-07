/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing;

import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;

import vavi.awt.dnd.BasicDTListener;


/**
 * ヒストリ付きコンボボックスです．
 * エクスプローラ等からのファイルの
 * ドラッグアンドドロップをサポートします．
 *
 * @todo ヒストリのクラス化？
 *       ヒストリの取り出し
 *       ヒストリの削除
 *       DnD って UI で指定するもの？
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020503 nsano initial version <br>
 *          0.01 021222 nsano try native DnD <br>
 *          0.02 021223 nsano fix native DnD <br>
 */
public class JHistoryComboBox extends JComboBox<Object> {

    /** */
    public JHistoryComboBox() {

        JTextField editor = (JTextField) this.getEditor().getEditorComponent();

        @SuppressWarnings("unused")
        JEditorPopupMenu popup = new JEditorPopupMenu(editor);

        this.setEditable(true);
        this.addActionListener(actionListener);

        new DropTarget(editor,
                       DnDConstants.ACTION_COPY_OR_MOVE,
                       new DTListener(),
                       true);
    }

    //

    /** */
    private ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            String item = (String) getSelectedItem();
            if (item == null || item.length() == 0) {
                return;
            }

            for (int i = 0; i < getItemCount(); i++) {
                if (item.equals(getItemAt(i)))
                    return;
            }
//Debug.println(Debug.getCallerMethod() + ": " + item);
            insertItemAt(item, 0);
        }
    };

    //----

    /** */
    private class DTListener extends BasicDTListener {

        public DTListener() {
            this.dragAction = DnDConstants.ACTION_COPY_OR_MOVE;
        }

        /**
         * Called by isDragOk
         * Checks to see if the flavor drag flavor is acceptable
         * @param    ev    the DropTargetDragEvent object
         * @return    whether the flavor is acceptable
         */
        protected boolean isDragFlavorSupported(DropTargetDragEvent ev) {
            return ev.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
        }

        /**
         * Called by drop
         * Checks the flavors and operations
         * @param    ev    the DropTargetDropEvent object
         * @return    the chosen DataFlavor or null if none match
         */
        protected DataFlavor chooseDropFlavor(DropTargetDropEvent ev) {
//Debug.println(ev.getCurrentDataFlavorsAsList());
            if (ev.isLocalTransfer() == true &&
                ev.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                return DataFlavor.javaFileListFlavor;
            }
            DataFlavor chosen = null;
            if (ev.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                chosen = DataFlavor.javaFileListFlavor;
            }
            return chosen;
        }

        /**
         * ドラッグ動作中に呼ばれます．
         */
//      public void dragOver(DropTargetDragEvent ev) {
//          super.dragOver(ev);
//Debug.println("here: " + ev.isDataFlavorSupported(DataFlavor.javaFileListFlavor));
//      }

        /**
         * You need to implement here dropping procedure.
         * data はシリアライズされたものをデシリアライズした
         * ものなのでクローンです．
         * @param data ドロップされたデータ
         */
        @SuppressWarnings("unchecked")
        protected boolean dropImpl(DropTargetDropEvent ev, Object data) {
//Debug.println(((List<File>)data).get(0).getClass());
            setSelectedItem(((List<File>) data).get(0).getPath());
            return true;
        }
    }

    //----

    /** */
    public static void main(String[] args) {
        JFrame frame = new JFrame("JHistiryComboBox Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        frame.getContentPane().setLayout(new BorderLayout());
        JHistoryComboBox cb = new JHistoryComboBox();
//      cb.setPreferredSize(new Dimension(120, 100));
        frame.getContentPane().add(BorderLayout.NORTH, cb);
        frame.setVisible(true);
    }
}

/* */
