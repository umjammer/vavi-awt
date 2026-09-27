# vavi.swing.propertyeditor

Provides PropertyEditor related classes.

## Usage

### JPropertyEditorPanel

A complete bean property editor.

#### General Use

* Create a `TableModel` that inherits from `AbstractDescriptorTableModel`
  * see `vavi.swing.propertyeditor.PropertyDescriptorTableModel`
* Apply that model to `JPropertyEditorTable`
* If you create and use your own property editor, add it to `propertyEditor.properties`

```properties
clazz.n = Class Primitive types are specified as is (e.g. `int, long ...`)
editor.n = Property editor class
```

## TODO

* `JPropertyEditorPanel`, Up button handling when Down fails
* i eliminated `ClassUtil`, is this still needed?