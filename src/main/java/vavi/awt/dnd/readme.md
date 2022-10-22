ドラッグアンドドロップを実装するための基本クラスを提供します．

結構便利なクラスです．実装の例は，

 * {@link vavi.apps.treeView.TreeViewTree}
 * {@link vavi.apps.treeView.TreeViewTreeDTListener}
 * {@link vavi.apps.treeView.TreeViewTreeNodeTransferable}

を参照してください．

あとは <code>Draggable</code> 中の <code>DragGestureListener</code> と <code>DragSourceListener</code> が外部から変更できるようになれば完璧ですね．

 * <code>BasicTransferable</code> を継承したクラスを作成し，その中にドラッグアンドドロップされるオブジェクトの判別を記述します．

```java

class FooTransferable extends BasicTransferable {
    ...
}

```

 * <code>BasicDTListener</code> を継承したクラスを作成し，その中にドロップされた場合のアクションを記述します．

```java

class FooDTListener extends BasicDTListener {
    ...
    protected boolean dropImpl(DropTargetDropEvent ev, Object data) {
        ...
        return true;
    }
}
```

 * ドラッグアンドドロップを行いたいコンポーネントに <code>Draggable</code> のインスタンスを作成します．

そして以下のように記述すればOKです．あら簡単．

```

class Foo /* want to drag and drop */ extends Component {

    Foo(...) {
        ...

        // ドラッグする側
        new Draggable(this, ...) {
            protected Transferable getTransferable(DragGestureEvent ev) {
                ...
                return new FooTransferable(...);
            }
        }

        // ドロップされる側
        new DropTarget(this, 
                       DnDConstants.ACTION_COPY_OR_MOVE,
                       new FooDTListener(this),
                       true);
    }
}

```
