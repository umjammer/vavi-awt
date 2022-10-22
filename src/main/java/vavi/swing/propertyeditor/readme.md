PropertyEditor 関連のクラスを提供します．

## 使用法

### JPropertyEditorPanel

   完全な bean のプロパティエディタです．

#### 一般的使用
  * <code>AbstractDescriptorTableModel</code> を継承した <code>TableModel</code> を作成する
    * see {@link vavi.swing.propertyeditor.PropertyDescriptorTableModel}
  * そのモデルを <code>JPropertyEditorTable</code> に適用する
  * 自作プロパティエディタを作成し使用する場合は <code>propertyEditor.properties</code> に追加してください

   clazz.n クラス プリミティブ型はそのまま指定 (e.g. <code>int, long ...</code>)
   editor.n プロパティエディタクラス

## これから実装すること

 * JPropertyEditorPanel，Down 失敗時の Up ボタン処理
