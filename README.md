<h1 align="center">Reflective Excel</h1>

<p align="center"><img height="200" width="200" src="src/test/resources/docs-images/reflective-excel-logo.png" alt="Reflective Excel Logo"/></p>

<p align="center">
    Reading and writing Excel sheets made easy.
</p>

<hr>

![Build](https://github.com/mihaiplotean/reflective-excel/actions/workflows/build-and-test.yml/badge.svg)
[![codecov](https://codecov.io/github/mihaiplotean/reflective-excel/graph/badge.svg?token=WVV881E6XZ)](https://codecov.io/github/mihaiplotean/reflective-excel)

Reflective Excel is a java library for mapping Excel sheets to Java Objects and vice-versa. 
It is designed to be versatile and simple to use. 
Under the hood, it uses <a href="https://poi.apache.org/">Apache POI</a>.

Some supported features:
- Tables with fixed column names.
- Tables with dynamic columns.
- Tables with grouped columns.
- [De]serialization of cell values from/to any java type.
- Styling the table/cell with custom fonts, background colors, and other.
- Reading and writing a table no matter the location in the sheet. For reading, auto-detection of the table is supported.
- Reading and writing a cell value at a fixed location in the sheet.
- For writing, providing a template Excel file.
- Multiple tables in one sheet.
<hr>

## Development Setup

### Prerequisites

- Java 17 or later.
- Project built with Maven.

### Setting Up

In the `pom.xml` file of your project, add the following dependency:

```xml
<dependency>
    <groupId>io.github.mihaiplotean</groupId>
    <artifactId>reflective-excel</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Examples

### Reading an Excel sheet

Let's say we have the following table in the sheet (starting at cell "A1"):

| **ID** | **Description**   | **Due date** |
|--------|-------------------|--------------|
| 1      | Buy Milk          | 20/01/2024   |
| 2      | Become productive | 10/12/2099   |

The corresponding class will look as follows:

```java
class TodoRow {

    @ExcelColumn("ID")
    private Integer id;
    
    @ExcelColumn("Description")
    private String description;
    
    @ExcelColumn("Due date")
    private Date dueDate;
}
```

Reading the rows:
```java
List<TodoRow> rows = new ReflectiveExcelReader(excelFile).readRows(TodoRow.class);
```

This was the most trivial table example. Reflective Excel supports reading of more complex sheets
and tables as well, including, but not limited to:
- Tables which start anywhere in the sheet.
- Columns which are dynamic. For example, columns which represent the days of a month.
- Grouped columns.
- Deserialization of the cell value to any java type.  

For further instructions, see the documentation for the [Reflective Excel Reader](src/main/java/com/reflectiveexcel/reader/README.md).

### Writing an Excel sheet

Let's say we want to write the following table to the sheet:

| **ID** | **Description**   | **Due date** |
|--------|-------------------|--------------|
| 1      | Buy Milk          | 20/01/2024   |
| 2      | Become productive | 10/12/2099   |

The corresponding class will look as follows:

```java
class TodoRow {

    @ExcelColumn("ID")
    private Integer id;

    @ExcelColumn("Description")
    private String description;

    @ExcelColumn("Due date")
    private LocalDate dueDate;

    // ... all args constructor
}
```

Writing the rows:
```java
List<TodoRow> rows = List.of(
    new TodoRow(1, "Buy Milk", LocalDate.of(2024, 1, 20)),
    new TodoRow(2, "Become productive", LocalDate.of(2099, 12, 10))
);
new ReflectiveExcelWriter(outputFile).writeRows(rows, TodoRow.class);
```

This was the most trivial table example. Reflective Excel supports writing of more complex sheets
and tables as well, including, but not limited to:
- Table and cell styling.
- Columns which are dynamic.
- Grouped columns.
- Serialization of any java type to a cell value.
- Tables which start anywhere in the sheet.

For further instructions, see the documentation for the [Reflective Excel Writer](src/main/java/com/reflectiveexcel/writer/README.md).

## Contributing

Feel free to provide improvement suggestions, feature ideas, report a bug or even ask a question by creating an issue.

## License

This project is licensed under the terms of the MIT license.
