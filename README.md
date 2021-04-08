# [Archived] Green Tea Latte

A Java testing framework that implements a behavior driven development style.
The framework is inspired by [Mocha](https://mochajs.org/) & [Chai](http://chaijs.com/), a javascript testing framework.

**Contents:**

* [Examples](#examples)
    * [Basic Example](#basic-example)
    * [Using Hooks](#using-hooks)
* [Documentation](#documentation)
    * [GreenTeaLatte()](#greentealatte)
    * [GreenTeaLatte(String description)](#greentealattestring-description)
    * [void setIndentationToSpaces(int amountOfSpaces)](#void-setindentationtospacesint-amountofspaces)
    * [void describe(String nodeDescription, Runnable nodeCode)](#void-describestring-nodedescription-runnable-nodecode)
    * [void assertTest(Boolean resultFromTestExpression)](#void-asserttestboolean-resultfromtestexpression)
    * [void before(String description, Runnable setup)](#void-beforestring-description-runnable-setup)
    * [void after(String description, Runnable cleanup)](#void-afterstring-description-runnable-cleanup)
    * [void beforeEach(String description, Runnable setup)](#void-beforeeachstring-description-runnable-setup)
    * [void afterEach(String description, Runnable cleanup)](#void-aftereachstring-description-runnable-cleanup)
    * [void run()](#void-run)
* [Found an issue or bug?](#found-an-issue-or-bug)

## Examples

### Basic Example

Java code:

```java
GreenTeaLatte deliciousLatte = new GreenTeaLatte();
deliciousLatte.setIndentationToSpaces(2);

deliciousLatte.describe("className", () -> {
    deliciousLatte.describe("methodNameA", () -> {
        deliciousLatte.it("should do this but I haven't written the test yet");
        deliciousLatte.it("should do that but I haven't written the test yet", () -> {});
        deliciousLatte.it("should pass this test", () -> {
            deliciousLatte.assertTest(true);
        });
        deliciousLatte.it("should fail this test", () -> {
            deliciousLatte.assertTest(false);
        });
    });
    deliciousLatte.describe("methodNameB", () -> {
        deliciousLatte.it("should do this but I haven't written the test yet");
        deliciousLatte.it("should do that but I haven't written the test yet", () -> {});
        deliciousLatte.it("should pass this test", () -> {
            deliciousLatte.assertTest(true);
        });
        deliciousLatte.it("should fail this test", () -> {
            deliciousLatte.assertTest(false);
        });
    });
});

deliciousLatte.run();
```

Output:

```txt
Brewing a Green Tea Latte:

  className

    methodNameA
      ⚠ should do this but I haven't written the test yet
      ⚠ should do that but I haven't written the test yet
      ✓ should pass this test
      ✗ should fail this test
        java.lang.AssertionError
          Output.lambda$null$2(Output.java:17)
          Output.main(Output.java:32)

      1 passed ✓
      2 pending ⚠
      1 failed ✗

    methodNameB
      ⚠ should do this but I haven't written the test yet
      ⚠ should do that but I haven't written the test yet
      ✓ should pass this test
      ✗ should fail this test
        java.lang.AssertionError
          Output.lambda$null$6(Output.java:27)
          Output.main(Output.java:32)

      1 passed ✓
      2 pending ⚠
      1 failed ✗


    2 passed ✓
    4 pending ⚠
    2 failed ✗

Test Summary:
  2 passed ✓
  4 pending ⚠
  2 failed ✗

⚠ 4 pending tests!
✗ Tests have failed!
Exception in thread "main" java.lang.Error: Did not pass all tests
	at src.GreenTeaLatte.run(GreenTeaLatte.java:486)
	at Output.main(Output.java:32)
```

### Using Hooks

Java code:

```java
GreenTeaLatte deliciousLatte = new GreenTeaLatte();
deliciousLatte.setIndentationToSpaces(2);

deliciousLatte.describe("className", () -> {
    deliciousLatte.describe("methodName", () -> {
        deliciousLatte.before("beforeA", () -> { /* setup code */ });
        deliciousLatte.before("beforeB", () -> { /* setup code */ });
        deliciousLatte.beforeEach("beforeEachA", () -> { /* setup code */ });
        deliciousLatte.beforeEach("beforeEachB", () -> { /* setup code */ });
        deliciousLatte.after("afterA", () -> { /* cleanup code */ });
        deliciousLatte.after("afterB", () -> { /* cleanup code */ });
        deliciousLatte.afterEach("afterEachA", () -> { /* cleanup code */ });
        deliciousLatte.afterEach("afterEachB", () -> { /* cleanup code */ });
        deliciousLatte.it("testA", () -> {
            deliciousLatte.assertTest(true);
        });
        deliciousLatte.it("testB", () -> {
            deliciousLatte.assertTest(true);
        });
        deliciousLatte.it("testC", () -> {
            deliciousLatte.assertTest(true);
        });
    });
});

deliciousLatte.run();
```

Output:

```txt
Brewing a Green Tea Latte:

  className

    methodName
      beforeA
      beforeB
      beforeEachA
      beforeEachB
      ✓ testA
      afterEachA
      afterEachB
      beforeEachA
      beforeEachB
      ✓ testB
      afterEachA
      afterEachB
      beforeEachA
      beforeEachB
      ✓ testC
      afterEachA
      afterEachB
      afterA
      afterB

      3 passed ✓
      0 failed ✗


    3 passed ✓
    0 failed ✗

Test Summary:
  3 passed ✓
  0 failed ✗

✓ All tests passed!
```

## Documentation

### GreenTeaLatte()

Creates a GreenTeaLatte instance

### GreenTeaLatte(String description)

Creates a GreenTeaLatte instance with a description

**Parameters:**

description - description for entire testing tree

### void setIndentationToSpaces(int amountOfSpaces)

Sets the output indentation to the desired amount of spaces (default is to use tabs)

**Parameters:**

amountOfSpaces - amount of spaces

### void describe(String nodeDescription, Runnable nodeCode)

Creates a new node (branch) in the testing code with a description of it

**Parameters:**

nodeDescription - description of the new node

nodeCode - code for the tests in the current node

### void it(String testDescription)

Creates and attaches a pending test to the current node

This is meant to be called with the runnable of a describe method, but can be be called on the root node.

**Parameters:**

testDescription - description of the test

### void it(String testDescription, Runnable testCode)

Creates and attaches a test to the current node

This is meant to be called with the runnable of a describe method, but can be be called on the root node.

Not including a "assertTest" call within a test will act as a pending test and result in a warning.

**Parameters:**

testDescription - description of the test

testCode - code for the test

### void assertTest(Boolean resultFromTestExpression)

Evaluates the result from a test expression

This must be called within a test (inside a runnable passed to the `it` method), used to determine if a test failed or passed.

**Parameters:**

resultFromTestExpression - boolean from a user defined test expression

### void before(String description, Runnable setup)

Define a block of code to run before the current node executes any tests

This only runs once for the "describe" node that it is defined in.
Multiple before code blocks can be defined. When there are multiple
blocks defined, they will execute in the order that they are defined.

**Parameters:**

description - description for the setup

setup - code for the setup

### void after(String description, Runnable cleanup)

Define a block of code to run after the current node executes all tests

This only runs once for the "describe" node that it is defined in.
Multiple after code blocks can be defined. When there are multiple
blocks defined, they will execute in the order that they are defined.

**Parameters:**

description - description for the cleanup

cleanup - code for the cleanup

### void beforeEach(String description, Runnable setup)

Define a block of code to run before each test

This only runs once for each "it" test in the current node.
Multiple beforeEach code blocks can be defined. When there are multiple
blocks defined, they will execute in the order that they are defined.

**Parameters:**

description - description for the setup

setup - code for the setup

### void afterEach(String description, Runnable cleanup)

Define a block of code to run after each test

This only runs once for each "it" test in the current node.
Multiple afterEach code blocks can be defined. When there are multiple
blocks defined, they will execute in the order that they are defined.

**Parameters:**

description - description for the cleanup

cleanup - code for the cleanup

### void run()

Runs all tests that have been defined

Runs deepest level first

Throws any error that occured to stop tests from running or
throws a RuntimeException after all tests have been run if one of them failed.

## Found an issue or bug?

Check to see if the bug was reported under open issues. If if isn't reported, please create an issue.
