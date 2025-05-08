Provides a base class for implementing drag and drop.

This is a pretty useful class. An example of its implementation is:

 * {@link vavi.apps.treeView.TreeViewTree}
 * {@link vavi.apps.treeView.TreeViewTreeDTListener}
 * {@link vavi.apps.treeView.TreeViewTreeNodeTransferable}

and refer to.

Now all that's left now is to make it possible to change the <code>DragGestureListener</code> and
<code>DragSourceListener</code> in <code>Draggable</code> from outside, and it'll be perfect.

 * Create a class that inherits from <code>BasicTransferable</code>
   and write the code in it to determine the object to be dragged and dropped.

```java

class FooTransferable extends BasicTransferable {
    ...
}

```

 * Create a class that inherits <code>BasicDTListener</code> 
   and write the action that will occur when a drop occurs in it.

```java

class FooDTListener extends BasicDTListener {
    ...
    protected boolean dropImpl(DropTargetDropEvent ev, Object data) {
        ...
        return true;
    }
}
```

 * Create an instance of <code>Draggable</code> on the component you want to be able to drag and drop.

Then just write it as shown below. Easy.

```

class Foo /* want to drag and drop */ extends Component {

    Foo(...) {
        ...

        // Drag Side
        new Draggable(this, ...) {
            protected Transferable getTransferable(DragGestureEvent ev) {
                ...
                return new FooTransferable(...);
            }
        }

        // The one being dropped
        new DropTarget(this, 
                       DnDConstants.ACTION_COPY_OR_MOVE,
                       new FooDTListener(this),
                       true);
    }
}

```
