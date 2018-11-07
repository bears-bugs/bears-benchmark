---
layout: page
title: Basics of JSaPar Schemas
---

# The schema
The schema is what describes the structure of the input or output data. The same schema can be used for both parsing and composing.
Usually the easiest way to work with a schema is to use the XML format but you may also create a schema directly in java code.
The documentation below is however based on the XML form.
The example below describes a simple schema for a CSV file taken from the first example above.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://jsapar.tigris.org/JSaParSchema/2.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://jsapar.tigris.org/JSaParSchema/2.0 http://jsapar.tigris.org/JSaParSchema/2.0/JSaParSchema.xsd">
   <csvschema lineseparator="\n">
    <line occurs="*" linetype="Person" cellseparator=";" quotechar="&quot;">
      <cell name="First name" />
      <cell name="Middle name" ignoreread="true"/>
      <cell name="Last name" />
      <cell name="Has dog"><format type="boolean" pattern="yes;no"/></cell>
    </line>
  </csvschema>
</schema>
```
## The schema of the schema
In the schema above, I have added the xsi:schemaLocation which helps intelligent xml editors to find the 
<a href="https://en.wikipedia.org/wiki/XML_Schema_(W3C)">XSD</a> that is used for JSaPar schemas. The XSD itself provides
a lot of documentation about the details of each allowed element and attribute within the schema xml. A published version
of the schema is located at. 

[http://jsapar.tigris.org/JSaParSchema/2.0/JSaParSchema.xsd](http://jsapar.tigris.org/JSaParSchema/2.0/JSaParSchema.xsd)

If you want to download the XSD as a file, you will probably need to right click on the link above and choose *"Save link as..."* depending on your browser.

## The Schema xml
After the leading root `<schema>` element you need to define what type of input or output you have. There are two choices:

1. `<csvschema>`
1. `<fixedwidthschema>`

Depending on the choice here, the rest of the schema will be different. Please note that `csvschema` can be used for any
type of delimited input/output, not only **comma** separated.

### The line separator
On this level you may also specify what type of line separator your input or output have. You can use any character 
sequence as line separator but for convenience the following escaped characters will also work within the xml:

* `\n` - LF (line feed) or hex 0A.
* `\r` - CR (carrige return) or hex 0D.
* `\t` - TAB (horizontal tab) or hex 09.
* `\f` - FF (form feed) or hex 0C.

You can also specify the line separator by using the hex code but in that case you need to use the XML-standard for
escaping. E.g. in order to use LF as line separator you need to write `lineseparator="&#10;"`

For Unix systems the normal line separator is `\n` and for Windows systems the normal line separator is `\r\n`. Omitting
the `lineseparator` attribute will result in that the system default is used. Be aware though that if you rely on system 
default, the schema will behave differently if you move it to a different platform. It is therefore recommended to always 
specify the line separator explicitly.

For fixed width files you may also specify an
empty string if lines are determined only by the length of the line which can be the case for Mainframe computers (COBOL).

When parsing, if you have specified one of either `\n` or `\r\n` as line separator, then the parser will consider both of them to be valid
line separators but when composing, only the specified line separator will be used.

## The Schema xml for CSV 
### Line
The `<line>` element describes a type of line that can occur in your input or output data. For instance, you may have a
different header line that has a different set of columns than the rest of the file. The `occurs` attribute describes
how many lines to expect of a certain type. By setting `occurs="*"` you indicate that the line may occur infinite number 
of times.

The attribute `linetype` sets the name of the type of line described by the line element. When parsing, the line type is
present in all parsed Line objects and can be used to determine how to treat the Line. When composing, you need to set the 
lineType of all Line objects that you provide to the Composer in order to make it produce lines of a specific type. 
This also means that when
converting from one format to another, the line type names of the input and the output schemas needs to match. 

On this level you need to specify the `cellseparator` attribute which should describe how cells/columns are separated 
within the input/output. You can use any character sequence 
and you can use the same escaped characters as with the line separator described above. Please note that if the cell 
separator may occur as valid text also within a value of a cell, you will need to quote the cell. See chapter about Quoted values below.

The attributes `ignoreread` and `ignorewrite` can be used to indicate that the line should be ignored while parsing or 
composing.

#### Line condition
Sometimes the type of line is not determined by position but instead by the value of one of the cells. If you for instance have the following file to parse:
```csv
H;06_NamesControlCell.csv;2007-07-07
B;Erik;Vidfare;Svensson
B;Fredrik;;Larsson
F;2
```
In this file, the value of the first column determines how to parse the rest of the line. `H` means header, `B` means body and `F` means footer.

When parsing this file you need a condition on the first cell on each line where a specific value should map to the type of the line.
By adding a line condition on the first cell you can specify a pattern that needs to match in order to use that line type.

The schema could look like this:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://jsapar.tigris.org/JSaParSchema/2.0">
  <csvschema lineseparator="\n">
    <line linetype="Header" cellseparator=";">
      <cell name="Type" default="H"><linecondition><match pattern="H"/></linecondition></cell>
      <cell name="FileName"/>
      <cell name="Created date"/>
    </line>
    <line linetype="Person" cellseparator=";" quotechar="&quot;">
      <cell name="Type" default="B" ><linecondition><match pattern="B"/></linecondition></cell>
      <cell name="First name"/>
      <cell name="Middle name" ignoreread="true"/>
      <cell name="Last name" />
    </line>
    <line linetype="Footer" cellseparator=";">
      <cell name="Type" default="F"><linecondition><match pattern="F"/></linecondition></cell>
      <cell name="Rowcount"/>
    </line>
  </csvschema>
</schema>
```
When parsing a file with a schema like this, it is important that you check the line type of the returned Line instance.

For instance, in your `LineEventListener` you should have a check like this:
```java
void lineParsedEvent(LineParsedEvent event){
   Line line = event.getLine();
   switch(line.getLineType()){
      case "Header":
         handleHeader(line);
         break;
      case "Person":
         handlePerson(line);
         break;
      case "Footer":
         handleFooter(line);
         break;
   }
}
```

You may add a line condition on any cell within your schema. If you add more than one line condition on the same line, 
all of them need to comply in order for the line type to be used.
 
You may combine the a line condition with the `occurs` attribute. In this case, the `occurs` value indicates the maximum number
of times that a line type is used when parsing.

When composing, you set the value of the line condition cell as with any other cell so the line condition as no effect 
when composing. By assigning a default value for the line condition cell as we do above, we make sure that we do not need 
to explicitly assign any value to that cell while composing.

*See [this example in the jsapar-examples project](https://github.com/org-tigris-jsapar/jsapar-examples/tree/master/src/main/java/org/jsapar/examples/schemabasics/c1)*
### Cell
The `<cell>` element describes the format of a particular cell or column. Each cell needs to have a name. By default the 
cell type is string so if you do not want the library to do any type conversion, the minimal configuration for a cell is:

```xml
<cell name="TheName"/>
```
With the attribute `mandatory="true"`, you can specify that an error is generated if a cell does not have any value. See 
chapter about error handling in the [basics](basics) article.
 
The attribute `default` can be used to assign a default value that will be used if the cell does not contain any value.
This works both while parsing and while composing.

As with lines, you can use `ignoreread` and `ignorewrite` on cell level to skip reading while parsing or to skip writing 
a cell value while composing. If `ignorewrite=true`, an empty cell will be written as if it contained an empty string. 

You can specify a maximum length to read or write to a cell value with the `maxlenght` attribute. Input and output
value will then be silently truncated to this length. If you want to get an error when field is to
long, use the format regexp pattern described below instead.
#### Cell formats
If you want the library to do type conversion while parsing or composing, you need to specify the format of a cell. For 
example, by adding the format:
```xml
<cell name="Birthdate"><format type="date" pattern="YYYY-mm-DD"/></cell>
```
The parser will convert string date values into `DateCell` containing a `java.util.Date` with the parsed date.

The following types are supported:
* string
* character
* decimal
* integer
* float
* boolean
* date
* local_date
* local_time
* local_date_time
* zoned_date_time

The `pattern` attribute behaves differently depending on the type: 
* If the type is string then the pattern should contain a regular expression to which the value is validated against. See [java.util.regex.Pattern](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html) This only works while parsing.
* If the type is any of the numerical types, then the pattern should be described according to the [java.text.DecimalFormat](https://docs.oracle.com/javase/8/docs/api/java/text/DecimalFormat.html). See chapter Internationalization below to be able to handle locale specific formatting of numerical values.
* If the type is date, then the pattern should be described according to [java.text.SimpleDateFormat](https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html).
* If the type is boolean, the pattern should contain the true and false values separated with a `;` character. Example: `pattern="Y;N"` will imply that `Y` represents true and `N` to represents false. Comparison while parsing is not case sensitive. Multiple true or false values can be specified, separated with the `|` character but the first value is always the one used while composing. Example: `pattern="Y|YES;N|NO"`

If the `pattern` attribute is omitted, the default system pattern is used.
#### Empty cell values while parsing
Sometimes empty cells in the input data are not really empty. They may contain a text like `NULL` or something like that. 
In that case you can still make the parser consider this to be an empty cell by specifying an `<emptypattern>` element within the cell.
For example:
```xml
<cell name="TheName"><emptypattern><match pattern="NULL"/></emptypattern></cell>
```  
This example will make sure that all cells that contain the string `NULL` will be regarded as empty. The pattern attribute can contain
any [regular expression](https://en.wikipedia.org/wiki/Regular_expression) that will be matched against the whole cell content. 
This means that for example if your input may contain white spaces before or after the text `NULL` and still should be considered empty you can 
change the match pattern to:
```xml
<match pattern="\s*NULL\s*"/>
```  
While parsing a delimited (CSV) input, cells containing white space characters are by default not considered empty, 
instead white space characters are parsed as cell value. If you instead want the parser to consider all cells that contain only white spaces as empty, 
you may specify an empty pattern that matches any number of white space characters. Like this:  
```xml
<match pattern="\s*"/>
```  
* See [this example in the jsapar-examples project](https://github.com/org-tigris-jsapar/jsapar-examples/tree/master/src/main/java/org/jsapar/examples/schemabasics/c2)

### Quoted values
The problem with delimited (CSV) data is that the value of a specific cell may also contain the delimiter character or even the line separator. 
In order to handle this scenario the JSaPar library is capable of handling quoted cells. It does not fully comply to the 
CSV standard [RFC-4180](https://tools.ietf.org/html/rfc4180) but we will get back to that in a moment.

You activate support for quoted values on a line type by specifying a quote character with the `quotechar` attribute:
```xml
...
    <line occurs="*" linetype="Person" cellseparator=";" quotechar="&quot;">
...
```
You can specify any character as quote character except the one you are using as line separator and cell separator. If you use a character that is 
reserved by the markup language (XML), you will need to [escape it](https://en.wikipedia.org/wiki/List_of_XML_and_HTML_character_entity_references) as in the example above.    
#### Parsing quoted values
As long as you have activated quoting as described above, the parser will automatically detect if a cell is quoted or not. Not all cells needs quoting. 
*A cell is considered to be quoted if and only if the first and the last character of the cell is the quote character.* The quote characters will always be removed
from the parsed value. 
This differs slightly from the CSV standard [RFC-4180](https://tools.ietf.org/html/rfc4180)
which states that:
```text
   7.  If double-quotes are used to enclose fields, then a double-quote
       appearing inside a field must be escaped by preceding it with
       another double quote.  For example:

       "aaa","b""bb","ccc"
```
JSaPar will instead only strip the first and the last quote of a cell regardless of if the cell content contains one or
more additional quote characters. In the example above JSaPar would parse the value `b""bb` for the second cell. This applies 
better to the majority of the real life delimited files since most of them do not really comply to [RFC-4180](https://tools.ietf.org/html/rfc4180). 
This also means that if you have a correctly placed start quote and the end quote is not the last character of the cell, the cell is not 
considered to be quoted and the quote characters will instead be part of the cell value. 

If the line separator or cell separator appears within the quotes of a cell it will be regarded as the content of the cell.
A maximum of 25 line separators are allowed within the same cell. If that value is exceeded a JSaParException is thrown 
since the input is probably invalid in some sense. This means that if a start quote is found but no end quote within the 
following 25 lines or until the end of input data, it is considered an irreparable error.
#### Composing quoted values
If your data might contain characters like the line separator or the cell separator you will need to quote the output when 
composing a delimited file. You activate quoting by specifying the quote character as described above. Now you have the option
to specify the quoting behavior on each cell by adding the `quotebehavior` attribute like this:
```xml
...
      <cell name="First name" quotebehavior="ALWAYS"/>
...
``` 
There are several options:
1. **ALWAYS** - Always quote this cell in the output.
1. **AUTOMATIC** - Quotes the cell only if needed, i.e. only if there is a cell separator, a line separator or a quote character 
present in the data. This is the default. 
1. **NEVER** - Never quote this cell. This may lead to invalid delimited output if the cell or line separator is present in the cell value.
1. **REPLACE** - No quoting is done, instead it replaces all illegal characters with non breakable space in order to always 
guarantee consistency of the delimited output. Can be used if the consumer of the output does not support quoted cells. 
This is the default if no quote character is specified on the line. 

You can change the default quote behavior for a whole line type by specifying the `quotebehavior` attribute on the line level:
```xml
...
    <line occurs="*" linetype="Person" cellseparator=";" quotechar="&quot;" quotebehavior="ALWAYS">
...
```

* See [this example in the jsapar-examples project](https://github.com/org-tigris-jsapar/jsapar-examples/tree/master/src/main/java/org/jsapar/examples/schemabasics/c3)

### The first line describes cell layout
It is quite common in CSV files to have one header row that contains the name of the columns within the file. For instance, the file might look like this:
```csv
First name;Middle name;Last name;Has dog
Erik;Vidfare;Svensson;yes
Fredrik;Allvarlig;Larsson;no
"Alfred";"Stark";Nilsson;yes
```
This type of format is supported by the library out-of-the-box. All you need to do is to is to set the attribute 
`firstlineasschema="true"` on the `<line>` element. Then the order of the cell while parsing is no longer denoted by the 
order of the `<cell>` elements in the schema. Instead the order is fetched from the first header row. It is important though that 
the name of the cells within the schema matches the names in the header. The advantage of using such a format is that the
producer of the CSV file can choose to re-arrange, add or remove columns without impacts on neither the code nor the schema.   

Here is a schema that could be used to parse the file above:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://jsapar.tigris.org/JSaParSchema/2.0">
  <csvschema lineseparator="\n">
    <line occurs="*" linetype="Person" cellseparator=";" quotechar="&quot;" firstlineasschema="true">
      <cell name="Middle name" ignoreread="true"/>
      <cell name="Has dog"><format type="boolean" pattern="yes;no"/></cell>
    </line>
  </csvschema>
</schema>
```   
As you can see in this example, not all cells are described in the schema. Only those cells, where additional information 
is needed from the schema, needs to be present. By default a string cell is otherwise expected. For instance the values of the 
`First name` column will still be parsed as if they are string value cells. This means that we could have omitted all the `<cell>` elements
from the schema above but then the parser would have parsed also the `Middle name` cells which we have no interest in 
and the `Has dog` cell would have
been parsed as string values `"yes"` and `"no"` instead of true boolean values. 

It is important though that if you provide `<cell>` elements for such a schema, the cell names need to match exactly what 
is specified in the header line. Matching is case sensitive.

You may for instance provide default values for missing columns or specify that a cell is mandatory by adding a `<cell>` element for that column.   

When composing, if `firstlineasschema="true"` then the output will be produced according to the cell layout of the schema
and with an additional header line with the name of the cells as specified by the schema. So in this case it is important that all the cells are present
and in the correct order.

## The Schema xml for fixed width data
### Line
The line schema for fixed with data source is very similar to the CSV line schema described above. The difference is that you have two additional attributes:
* `minlength` - The minimal length of a fixed with line when writing output.
                If the sum of the length of the cells is less than this value then the line is filled with the pad character so that the line will never be shorter than this length. 
* `padcharacter` - The pad character to use to pad lines that are not reaching the minimum length. Also used as default
 pad character for cells of this line. Default is space character (ASCII 20).
 
As with CSV schema lines you can specify a line condition on any cell if the line type is determined by the value of one of the cells. See above.
### Cell
The cell has these additional attributes compared to CSV cell schema:
* `length` - The length of this cell in the input or output data. When parsing, only this amount of characters are read 
and when composing, the value of the cell is padded or truncated to fit this length.    
* `alignment` - Defines the cell alignment. The remaining space is filled with the pad character. Have to be one of the following:
  * `left` - The value of the cell is assumed to be to left within the cell. Padding is done to the right when composing and pad characters are removed from the right when parsing. In case value does not fit the cell, truncating is done from the right of the value. 
  * `right` - The value of the cell is assumed to be to right within the cell. Padding is done to the left when composing and pad characters are removed from the left when parsing. In case value does not fit the cell, truncating is done from the left of the value. 
  * `center` - The value of the cell is assumed to be in the center of the cell. Padding is done equally much to the left and to the right when composing and pad characters are removed from both the left and right side when parsing. In case value does not fit the cell, truncating is done both from the left and from the right of the value.
  
  Default is `right` when cell type is any kind of number type and `left` for all other type of cells. 
* `padcharacter` - Specifies the pad character to use to pad cells that are not reaching the minimum length. The alignment attribute specifies if padding should be done to the right, to the left or both. Default is space character (ASCII 20) unless there is a default pad character specified on the line level. 

## Internationalization 
The representation of the numbers and dates differ between different countries and regions around the world. The JSaPar 
library can handle different formats depending on the locale. By adding a locale element, you indicate that numbers and 
dates should be parsed and composed according to that locale.

For instance in the example below, the decimal symbol `,` will be used instead of the British and US `.` character. 
```xml
<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://jsapar.tigris.org/JSaParSchema/2.0">
  <csvschema lineseparator="\n">
    <locale language="sv" country="SE"/>
    <line occurs="*" linetype="City" cellseparator=";">
      <cell name="Name" />
      <cell name="Latitude" /><format type="float" pattern="#.#####"/></cell>
      <cell name="Longitude" /><format type="float" pattern="#.#####"/></cell>
    </line>
  </csvschema>
</schema>
``` 
The locale of the schema affects both parsing and composing text. 

You can override the locale with an additional locale element on cell level if one of the cells should be read/written using a different locale compared to the rest of the cells described in the schema.  

The default locale is [US](https://docs.oracle.com/javase/8/docs/api/java/util/Locale.html#US).