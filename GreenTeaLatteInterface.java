package greentealatte;

public interface GreenTeaLatteInterface {
    // GreenTeaLatteInterface is an inferace for a behavior driven development testing framework

    void setPaddingToSpaces(int amountOfSpaces);
    // sets the padding for testing to the desired amount of spaces

    void describe(String description, Runnable testsToBeRun);
    // creates a category in a tree structure, so a category can be created inside of another category
    // places tests inside of a category

    void it(String description);
    // pending test, test not yet implemented
    // results in a warning, but not a failed test

    void it(Runnable testToRun);
    void it(String description, Runnable testToRun);
    // creates a test to run
    // call inside of a category

    void assertTest(Boolean expressionToTest);
    // takes the boolean value of the result of a test and applies it to the test
    // call inside of a test

    void before(Runnable setup);
    void before(String description, Runnable setup);
    // runs the setup runnable once before the category's tests begin
    // all hooks are run  in the order that defined

    void after(Runnable cleanup);
    void after(String description, Runnable cleanup);
    // runs the cleanup runnable once after the category's tests complete
    // all hooks are run  in the order that defined

    void beforeEach(Runnable setup);
    void beforeEach(String description, Runnable setup);
    // runs the setup runnable before each test in the category
    // all hooks are run  in the order that defined

    void afterEach(Runnable cleanup);
    void afterEach(String description, Runnable cleanup);
    // runs the cleanup runnable after each test in the category

    void run();
    // runs all tests after the tests have been defined
}
