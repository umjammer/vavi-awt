# vavi.swing.propertyeditor

Provides PropertyEditor related classes.

## Usage

### JPropertyEditorPanel

A complete bean property editor.

#### General Use

* Create a <code>TableModel</code> that inherits from <code>AbstractDescriptorTableModel</code>
  * see {@link vavi.swing.propertyeditor.PropertyDescriptorTableModel}
* Apply that model to <code>JPropertyEditorTable</code>
* If you create and use your own property editor, add it to <code>propertyEditor.properties</code>

```
clazz.n = Class Primitive types are specified as is (e.g. <code>int, long ...</code>)
editor.n = Property editor class
```

## TODO

* JPropertyEditorPanel, Up button handling when Down fails