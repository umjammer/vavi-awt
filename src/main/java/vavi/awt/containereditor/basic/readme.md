# vavi.awt.containereditor.basic

Provides Basic container editor related classes.

## Purpose

Visually manipulate components within a container.
The feel of the operation is similar to that of Draw-type applications.

## Usage

For now, all you need to do is specify a container and create a `BasicContainerEditor`.

```java
    ContainerEditor ce = new BasicContainerEditor(container);
```

Making a container editable allows you to perform editing operations on the components within the container.

```java
    ce.setEditable(true);
```

The following operations are possible:

* Select an area to select the components in that area (red rubber band)
* Click an unselected component to select it (blue rubber band)
* Shift + click to select multiple components (blue rubber band)
* When multiple components are selected, click a selected component to deselect it
* Drag a selected component to move it (blue rubber band)
* Drag a selected component to resize it (blue rubber band)

`The following events occur from BasicContainerEditor`, so users should make use of them.

| Event Type                | Event occurrence conditions                                | Event arguments                 |
|---------------------------|------------------------------------------------------------|---------------------------------|
| `EditorEvent("select")`   | When a component in the container is selected              | A vector of selected components |
| `EditorEvent("location")` | When the component's position within the container changes | Modified components             |
| `EditorEvent("bounds")`   | When a component in a container changes size               | Modified components             |

## Limitation

* Currently, the layout of the target container is only assumed to be `null`.
* The container to be edited must have a parent component. Hmm...
* ~~020613 Since a component is used for the controller, a `ContainerEvent` is generated when a controller is added or removed in the internal processing. Users must add a program to ignore this. @see `ContainerEditor#processContainerEvent,JEditablePanel#processContainerEvent`~~

## TODO

* Completion of the `RubberBandRenderer` mechanism
* Keyboard operation
* Separate operation methods as actions?
* ~~020613 When container is selected, glassPane is removed~~