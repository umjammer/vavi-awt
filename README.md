[![Release](https://jitpack.io/v/umjammer/vavi-awt.svg)](https://jitpack.io/#umjammer/vavi-awt)
[![Java CI with Maven](https://github.com/umjammer/vavi-awt/workflows/Java%20CI%20with%20Maven/badge.svg)](https://github.com/umjammer/vavi-awt/actions)
[![CodeQL](https://github.com/umjammer/vavi-awt/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/umjammer/vavi-awt/actions/workflows/codeql-analysis.yml)
![Java](https://img.shields.io/badge/Java-8-b07219)

# vavi-awt

awt, swing helpers

## 🎨 Contents

### 🖌️ easy swing binding system

auto wiring between a bean and swing components

[sample](src/test/java/vavi/swing/binder/BinderTest.java)

### 🖌️ easy drag and drop system

make a component by one liner

```java
    Droppable.makeComponentSinglePathDroppable(componebt, p -> { ... });
```

### 🖌️ rubber band selection

the ordinary gui bound selector

[sample](https://github.com/umjammer/vavi-image-sandbox/tree/master/src/test/java/Test32.java)

### 🖌️ JImageComponent

the image component which has auto size adjustment

[sample](src/test/java/JImageComponentTest.java)

### 🖌 JHistoryComboBox

the text field with history dropdown

[sample](src/test/java/FileRenamer.java)

### 🖌 JFileChooserHistoryComboBox

the file name field with history dropdown and the file chooser button

[sample](https://github.com/umjammer/vavi-apps-jwindiff)