# valuestreams

[![Build Status](https://travis-ci.org/kmehrunes/valuestreams.svg?branch=master)](https://travis-ci.org/kmehrunes/valuestreams)
[![](https://jitpack.io/v/kmehrunes/valuestreams.svg)](https://jitpack.io/#kmehrunes/valuestreams)

A set of some optional-like value wrappers which provide more validation and mapping options. There is a also a pipeline to define a sequence of validation and mapping operations once and use it multiple times. This project was created just for fun, but turned out to be quite convienent so I decided to share it. May it serve you in the projects to come.

**Feel free to conribute by adding functionalities and other convient features you think could be of use to other people.**

### NATQ (Nobody Asked These Questions)
**Q: Why is it called a stream when it doesn't implement the stream interface?**
A: IDK, couldn't think of a better name.

**Q: Why would I use a value stream?**
A: It's more convenient than performing those tasks manually, provides more declarative functionality, and makes the code more concise and easier to read. Also unlike `Optional`, those implementations don't create a new instance with every operation; pretty much everything is done in-place.

## Dependency
```xml
<repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
</repositories>

<dependencies>
        <dependency>
	    <groupId>com.github.kmehrunes</groupId>
	    <artifactId>valuestreams</artifactId>
	    <version>v0.2</version>
	</dependency>
</dependencies>
```

## Value Streams Overview
The provided classes expose an interface which is similar in a way to optional values. They allow applying a series of operations on a wrapped value. The provided classes are:
- **Value**: a generic class for general purpose objects

- **StringValue**: a class specific for processing `String` values

- **NumericalValue**: a generic class for any type `N` which represents a number whether built into the a language or custom; specific sub-classes are provided: `IntegerValue`, `LongValue`, and `DoubleValue`

- **DateValue**: a class specific for processing `Date` values

### Creating a Value Instance
Just like optionals, creating an value instance must be done by using the functions `of()` and `empty()` provided by every class. For example, `Value.of("test")` will return an instance of type `Value<String>`. Note that `Value<String>` isn't the same as `StringValue`, if you need a `StringValue` then you need to use `StringValue.of("test")`. This is due to the fact that `StringValue` offers more functionality for strings.

### Validation and Mapping
The two main operations on values are validation and mapping. The former takes a predicate which verifies that the value conforms to a rule, while the latter tranforms the value. Specialized classes (e.g. `IntegerValue`) offer two types of mapping: normal one which must match the type of the value and is performed in-place, and a casting map which changes the type of the value and will result in another object containing the new value.

### Getting Results
After you are done with processing a value, you can retrieve it using mulitple options:
- **getOrThrow**: returns the value or throws an exception (you decide what exception to throw)
- **getNullable**: returns the value or `null` if none was found
- **toOptional**: returns an optional containing the value or an empty one

## Pipeline Overview
Besides direct value streams, the library also supports processing pipeline. A pipeline is a pre-defined sequence of valiation and mapping operations to be applied on an input value. It also allows you to create a base pipeline and extend it multiple times.

### Creating a Pipeline
A pipeline must be created with an input and output types specified, and there are three ways one can create a new pipeline. 
```java
Pipeline<String, String> pipeline = Pipeline.<String>input();

Pipeline<String, String> pipeline = Pipeline.input(String.class);

Pipeline<String, String> pipeline = Pipeline.input(new IdentityOperation());
```

### Chaining Operations to a Pipeline
Once a pipeline is created operations can be chained to it. You can chain a mapping operation using `map()`, a validation operation using `validate()`, or whatever kind of operation you wish to define using `chain()`.
```java
Pipeline.input(String.class)
	.map(Decoders::decodeBase64ToString)
	.validate(str -> str.length() > 10)
	.chain(new CustomOperation());
```
In this example we created a pipeline which accepts a string as its input, decodes it, checks the length, and then applies a custom operation on it.

### Extending a Pipeline
Pipelines are immutable; every chain operation creates a new pipeline which contains the previous operations and the new one added to them without modifying the original pipeline. We can make use of this to create a base pipeline and extending it for different causes.
```java
Pipeline<String, String> emailPipeline = Pipeline.input(String.class)
	.validate(Email::isValid);
	
Pipeline<String, String> sendEmailPipeline = emailPipeline.map(Email::sendTo);
Pipeline<String, ServiceResponse> updateUserEmailPipeline = emailPipeline.chain(usersService::updateUserEmail);
```

### Applying a Pipeline
A pipeline can be applied simply when `apply()` is called. `apply()` takes as an argument a value of the same type as the input type of the pipeline, and runs all operations on it in the sequence they are defined. It returns the final result as a value stream of the output type (`Value<O>`).

## Examples
- Generic values
```java
/*
 * create a value of a string, check that its length is 
 * between 3 and 5, convert it to uppercase, then check 
 * its value.
 */
Value.of("test")
        .validate(s -> s.length() > 3 && s.length() < 5)
        .map(String::toUpperCase)
        .validate(s -> s.equals("TEST"))
        .getValueOrThrow(Exception.class, "Invalid test value");
        
/*
 * create a value of a mock class, get only the second 
 * field, check that it's less than 4, and get the 
 * result as an optional
 */
Value.of(new MockClass("unnecessary first field", 2))
        .map(mock -> mock.secondField)
        .validate(i -> i < 4)
        .toOptional();
```

- String values
```java
// same as the previous example
StringValue.of("test")
        .lengthBetween(3, 5)
        .map(String::toUpperCase)
        .isEqualTo("TEST")
        .getValueOrThrow(Exception.class, "Invalid test value");
```

- Numerical values
```java
/*
 * create a value, check that it's between -90.0 and 90.0
 * and return the result as a nullable object (for whatever 
 * reason that might be)
 */
DoubleValue.of(-45.3)
         .betweenInclusive(-90.0, 90.0)
         .getNullable();
```

- Date values
```java
/*
 * create a date value for 5th of May 2011, validate 
 * the month, and check that the date is in the past.
 */
DateValue.of(5, Month.MAY, 2011)
         .inMonth(Month.MAY)
         .isBefore(someOtherDate)
         .past()
         .getValueOrThrow(Exception.class, "Invalid date");
```
