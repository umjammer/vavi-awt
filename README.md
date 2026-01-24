# package vavi.swing.binding.table

provides easy swing binding series for `JTable`.

## Usage

suppose we'd like to show entries in an Archive object.

* definition

  * mark `@Table` to a pojo you want display in a `JTable` (*1)
    * specify a row definition *class* to `row` element (*2)
    * specify a *method* to iterate rows from the pojo to `iterable` element (*3)
  * write a row definition class (*4)
  * mark `@Row` to the class that you specified at above as a row definition (*5)
    * mark `@Column` to a methods in the row definition class for describing each column order, size etc. (*6)
    * specify a setter *method* that fill the rpw definition class (*7) 
  * prepare the iteration method that you specified at above 

```java
// *1  *2                                          *3
@Table(row = ArchiveModel.ArchiveEntryModel.class, iterable = "entries")
public class ArchiveModel {

//  *5   *7
    @Row(setter = "setEntry")
    public static class ArchiveEntryModel { // *4
        private Entry entry;
        public void setEntry(Entry entry) { // *7
            this.entry = entry;
        }
        @Column(sequence = 0, width = 200) // *6
        public String getName() {
            return getFileName(entry.getName());
        }
        @Column(sequence = 1, width = 100)
        public String getType() {
            return mimeTable.getContentTypeFor(getFileName(entry.getName()));
        }

         ⋮
    }

    private final Archive archive;

    public ArchiveModel(Archive archive) {
        this.archive = archive;
    }

    public Iterable<Entry> entries() { // *8
        return Arrays.asList(archive.entries());
    }

     ⋮
}
```

 * binding

```java
        table = new JTable();
        table.setShowHorizontalLines(false);
         ⋮

        archive = Archives.getArchive(new File(args[0]));

        TableModel<?> model = new TableModel<>(new ArchiveModel(archive));
        model.bind(table);

```