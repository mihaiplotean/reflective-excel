# Reflective Excel Reader

Reflective Excel Reader allows mapping of Excel sheets to Java objects. To provide an idea
of what is possible, we will showcase each feature in the next sections.

## Table of contents

  * [Fixed columns](#fixed-columns)
  * [Deserializing cell values](#deserializing-cell-values)
    * [Default Deserializers](#default-deserializers)
    * [Custom Deserializer](#custom-deserializer)
      * [What if deserialization requires information outside the current cell?](#what-if-deserialization-requires-information-outside-the-current-cell)
    * [Handling invalid cell values](#handling-invalid-cell-values)
  * [Dynamic columns](#dynamic-columns)
  * [Grouped columns](#grouped-columns)
  * [Detecting table bounds, skipping rows](#detecting-table-bounds-skipping-rows)
  * [Reading more than one table](#reading-more-than-one-table)
    * [Reading multiple tables](#reading-multiple-tables)
    * [Reading a cell value](#reading-a-cell-value)
    * [Reading properties with values](#reading-properties-with-values)

## Fixed columns

Let's say we have the following table in the sheet (starting at cell "A1"):

| **ID** | **Employee** | **Worked hours** |
|--------|--------------|------------------|
| 1      | Joe          | 140              |
| 2      | Maria        | 120.5            |

The corresponding class will look as follows:

```java
class EmployeeRow {

    @ExcelColumn("ID")
    private Integer id;

    @ExcelColumn("Employee")
    private String name;

    @ExcelColumn("Worked hours")
    private Double workedHours;
}
```

To read the table rows, call `ReflectiveExcelReader#readRows`. For our example:

```java
List<EmployeeRow> rows = new ReflectiveExcelReader(excelFile).readRows(EmployeeRow.class);
```

Various options for customizing the reading process are defined through the `ExcelReadingSettings` and its corresponding
builder.
The previous example assumed that the table is in the first sheet of the Excel file. If this is not the
case, you can specify the sheet name using `ExcelReadingSettingsBuilder#sheetName`.

Example:

```java
ExcelReadingSettings settings = ExcelReadingSettings.builder()
        .sheetName("my-sheet-name")
        .build();
List<EmployeeRow> rows = new ReflectiveExcelReader(excelFile, settings).readRows(EmployeeRow.class);
```

An equivalent method exists if you would like to specify the sheet index instead of the name. A negative index can be
also used to specify a
sheet from the end, i.e. passing `-1` will read the last sheet.

## Deserializing cell values

### Default Deserializers

By default, converting the cell value to the following types is supported:

- `String`, all primitives and their corresponding wrappers.
- `Date`, `LocalDate` and `LocalDateTime`. This is assuming that in the sheet, the cell value has a date number format.
- `java.util.Currency` – the cell value needs to be the currency code.

All the default deserializers are defined by the `DefaultDeserializationContext`.

If your cell is formatted as text, but you need to deserialize/convert it to a date type, you will need to
specify the date format. A custom deserializer which does this already exists. For the `Date` type, you can use it as
follows:

```java
DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext();
deserializationContext.registerDeserializer(Date.class, CellDeserializers.forDate("dd/MM/yyyy"));

ExcelReadingSettings settings = ExcelReadingSettings.builder()
        .deserializationContext(deserializationContext)
        .build();
List<EmployeeRow> rows = new ReflectiveExcelReader(excelFile, settings).readRows(EmployeeRow.class);
```

Equivalent deserializers for `LocalDate` and `LocalDateTime` also exist inside the `CellDeserializers` class.

### Custom Deserializer

If you want to deserialize the cell value to a type `T` which is not supported by default, you can define a custom
deserializer by implementing `CellDeserializer<T>` and adding it to the `DeserializationContext`.

For example, let's say that we have a table where each employee has an "employment type" property, which can either
be "full-time" or "part-time". We will define this through an enum type.

| **Employee** | **Employment Type** |
|--------------|---------------------|
| Joe          | full-time           |
| Maria        | part-time           |

Where

```java
class EmployeeRow {

    @ExcelColumn("Employee")
    private String name;

    @ExcelColumn("Employment Type")
    private EmploymentType employmentType;
}
```

And

```java
enum EmploymentType {
    FULL_TIME,
    PART_TIME
}
```

To map the employment type to our enum, we define the following deserializer:

```java
class EmploymentTypeDeserializer implements CellDeserializer<EmploymentType> {

    @Override
    public EmploymentType deserialize(ReadingContext context, ReadableCell cell) throws BadInputException {
        String cellValue = cell.getValue();
        if (cellValue.equalsIgnoreCase("full-time")) {
            return EmploymentType.FULL_TIME;
        }
        if (cellValue.equalsIgnoreCase("part-time")) {
            return EmploymentType.PART_TIME;
        }
        throw new BadInputException(String.format(
                "Value \"%s\" defined in cell %s is not a known employment type", cellValue, cell.getCellReference()
        ));
    }
}
```

To briefly explain what we did:

- The `ReadableCell` object provides us useful information about the cell that we are deserializing. We retrieve the
  cell value.
- We map the cell value to the corresponding enum value.
- If we could not map the value to an enum, we throw a `BadInputException` to signal invalid values in the sheet. The
  exception message also contains the cell reference (e.g. "A1"), to make it easy to see which cell value is wrong.

The only thing remaining is to add the cell deserializer to the `DeserializationContext` and read the rows:

```java
DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext();
deserializationContext.registerDeserializer(EmploymentType.class, new EmploymentTypeDeserializer());

ExcelReadingSettings settings = ExcelReadingSettings.builder()
        .deserializationContext(deserializationContext)
        .build();
List<EmployeeRow> rows = new ReflectiveExcelReader(excelFile, settings).readRows(EmployeeRow.class);
```

Note that an implementation for deserialization to enum types already exists, and is provided
by `CellDeserializers#forEnum`.
The value mapping happens by matching against `Object#toString()`.

#### What if deserialization requires information outside the current cell?

In some cases, you might need information outside the current cell for deserialization.
For example, the cell you are trying to deserialize could contain the day of the month, but the month itself
is stated outside the table you are reading. The `CellDeserializer` provides information about the sheet and the sheet
reading process through the `ReadingContext`.

Some useful things we can do via the `ReadingContext`:

- Retrieve any cell value using `ReadingContext#getCellValue`.
- Retrieve the current row and column being read.
- Retrieve information about the headers of the current table.
- Get the cells of a given table row.

### Handling invalid cell values

As you might have noticed, by default, we throw a `BadInputException` to signal an invalid cell value that could not
be deserialized. This also stops the reading process. However, you might want to continue reading the sheet instead.
This can be achieved through the `BadInputExceptionConsumer`.
For example, if you would like to print all the invalid cell values:

```java
class ReportingExceptionConsumer implements BadInputExceptionConsumer {

    @Override
    public void accept(ReadingContext readingContext, BadInputException exception) {
        System.out.println(exception.getMessage());
    }
}
```

Then provide this implementation to the `ExcelReadingSettings` when reading the sheet.

```java
ExcelReadingSettings settings = ExcelReadingSettings.builder()
        .onException(new ReportingExceptionConsumer())
        .build();
```

## Dynamic columns

Now, let's say that we have in the table the amount of hours an employee has worked each day
of a month. In this case, these columns are "dynamic", as they vary between 28 and 31, depending on the month and year.

| **ID** | **Employee** | **1** | **2** | **3** | ... | **31** |
|--------|--------------|-------|-------|-------|-----|--------|
| 1      | Joe          | 8     | 8     | 10    | ... | 7.5    |
| 2      | Maria        | 4.5   | 8     | 8     | ... | 8      |

The `@DynamicColumns` annotation can be used to specify these columns as follows:

```java
class EmployeeRow {

    @ExcelColumn("ID")
    private Integer id;

    @ExcelColumn("Employee")
    private String name;

    @DynamicColumns
    private Map<Integer, Double> workedHoursPerDay;
}
```

The `@DynamicColumns` can only be used on `Map.class`. The map will contain the table column/header name as keys,
and the map values will represent the corresponding cell values. Annotating `List.class` is also possible in case you
do not care about the column names.

By default, the annotation will consider as dynamic all the columns which could not be mapped as
either fixed or grouped. If your table contains columns which should not be considered dynamic, or you have multiple
sets of dynamic columns, you should define a `DynamicColumnDetector`. In our case:

```java
class EmployeeRow {

    // ...

    @DynamicColumns(detector = WorkedHoursColumnDetector.class)
    private Map<Integer, Integer> workedHoursPerDay;
}
```

and

```java
class WorkedHoursColumnDetector implements DynamicColumnDetector {

    @Override
    public boolean test(ReadingContext context, MaybeDynamicColumn column) {
        String columnName = column.getName();
        if (column.isAfter("Employee") && isDayOfMonth(columnName)) {
            return true;
        }
        return false;
    }
}
```

In the provided snippet, we detect which columns should be mapped as "day of the month" by implementing
a `DynamicColumnDetector`.
`MaybeDynamicColumn` provides information about the currently read column. In our case, we check if it is after the "Employee" column and if its name represent a day of the month.

## Grouped columns

Sometimes, related columns/headers are grouped into one. For example:

<table class="tg"><thead>
  <tr>
    <th class="tg-64cd" rowspan="2">ID</th>
    <th class="tg-64cd" rowspan="2">Employee</th>
    <th class="tg-js8t" colspan="2">Address</th>
  </tr>
  <tr>
    <th class="tg-g9ke">Home</th>
    <th class="tg-js8t">Work</th>
  </tr></thead>
<tbody>
  <tr>
    <td class="tg-0pky">1</td>
    <td class="tg-0pky">Joe</td>
    <td class="tg-0pky">Home Street A</td>
    <td class="tg-0pky">Work Street</td>
  </tr>
  <tr>
    <td class="tg-0pky">2</td>
    <td class="tg-0pky">Maria</td>
    <td class="tg-0pky">Home Street B</td>
    <td class="tg-0pky">Work Street</td>
  </tr>
</tbody>
</table>

The grouped columns need to be grouped into a new java object and annotated with `@ExcelCellGroup`, as follows:

```java
class EmployeeRow {

    @ExcelColumn("ID")
    private Integer id;

    @ExcelColumn("Employee")
    private String name;

    @ExcelCellGroup("Address")
    private EmployeeAddress address;
    
    // ...

    static class EmployeeAddress {

        @ExcelColumn("Home")
        private String home;

        @ExcelColumn("Work")
        private String work;

        // ...
    }
}
```

An `ExcelCellGroup` can contain fixed columns, dynamic columns, or even other cell grouping columns.

## Detecting table bounds, skipping rows

So far we have assumed that the table starts at the top left of the sheet, i.e. at cell "A1". This is not always the
case. To solve this issue, we defined the `TableRowColumnDetector`. It allows to specify things as:
- Where the table header starts and ends. See `TableRowColumnDetector#isHeaderRow`, `#isHeaderStartColumn` and `#isHeaderLastColumn`.
- Where the table ends. See `TableRowColumnDetector#isLastRow`.
- Which rows to skip. See `TableRowColumnDetector#shouldSkipRow`.

By default, the table starts at cell "A1", ends at the first encountered empty table row and no rows are skipped.
This is defined by the `SimpleRowColumnDetector` implementation of the `TableRowColumnDetector`. It also has the
option to specify another fixed cell reference where the table starts. For example, if your table starts at cel "B1":

```java
ExcelReadingSettings settings = ExcelReadingSettings.builder()
        .rowColumnDetector(new SimpleRowColumnDetector("B1"))
        .build();
```

On top of this, there is a `TableRowColumnDetector` implementation with the possibility of detecting table bounds automatically! 
Even if the table is shifted, it will try to find its position based on the column names that you've defined in your java object.
This is provided by the `AutoRowColumnDetector`:

```java
ExcelReadingSettings settings = ExcelReadingSettings.builder()
        .rowColumnDetector(new AutoRowColumnDetector())
        .build();
```

## Reading more than one table

Besides reading one table, we can read multiple tables at once, as well as values outside tables.
This is done through the `ReflectiveExcelReader#read` method.

### Reading multiple tables

We can read multiple tables by using the `@TableId` annotation on the list of rows. Assuming we have an employee table, where the corresponding
java type is `EmployeeRow`, as well a vacation table, with the corresponding type `VacationRow`:

```java
class EmployeeSheet {

  @TableId("employee-table")
  private List<EmployeeRow> employeeRows;

  @TableId("vacation-table")
  private List<VacationRow> vacationRows;
}
```

The default reading settings assume that the tables start at cell "A1". However, this is not the case when there are multiple
tables in one sheet, so we will need to specify where each table starts using a `TableRowColumnDetector`. We will use the provided
`AutoRowColumnDetector`:

```java
ExcelReadingSettings settings = ExcelReadingSettings.builder()
        .rowColumnDetector(new AutoRowColumnDetector())
        .build();
EmployeeSheet sheet = new ReflectiveExcelReader(excelFile, settings).read(EmployeeSheet.class);
```

### Reading a cell value

We can also get a cell value using a cell reference by using the `@ExcelCellValue` annotation. The value will be deserialized
using the specified deserializer for the annotated type.

```java
class EmployeeSheet {

  @ExcelCellValue(cellReference = "B1")
  private Integer year;

  @TableId("employee-table")
  private List<EmployeeRow> employeeRows;
}
```

```java
ExcelReadingSettings settings = ExcelReadingSettings.builder()
        .rowColumnDetector(new AutoRowColumnDetector())
        .build();
EmployeeSheet sheet = new ReflectiveExcelReader(excelFile, settings).read(EmployeeSheet.class);
System.out.println(sheet.year);
```

### Reading properties with values

Sometimes, a sheet contains key-value properties. While we could read the value using the `@ExcelCellValue` annotation, we might
want to ensure the sheet correctness by checking that the property name is as expected. We can do that by using the `@ExcelProperty`
annotation. As parameters, we will need to specify the cell reference of the property name, the property name itself, and the
reference of the property value. If in the sheet, the property name at the given cell location does not match the provided one, a
`BadInputException` will be thrown during reading.

```java
class EmployeeSheet {

  @ExcelProperty(name = "Year:", nameReference = "B1", valueReference = "B2")
  private Integer year;

  @TableId("employee-table")
  private List<EmployeeRow> employeeRows;
}
```

```java
ExcelReadingSettings settings = ExcelReadingSettings.builder()
        .rowColumnDetector(new AutoRowColumnDetector())
        .build();
EmployeeSheet sheet = new ReflectiveExcelReader(excelFile, settings).read(EmployeeSheet.class);
System.out.println(sheet.year);
```
