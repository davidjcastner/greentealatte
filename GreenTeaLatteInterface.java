// David Castner
package greentealatte;

public interface GreenTeaLatteInterface {
    // GreenTeaLatteInterface is an inferace that implements a behavior driven development testing framework

    void describe(String description, Method testsToBeRun);
    // creates a category in a tree structure, so a category can be created inside of another category
    // places tests inside of a category

    void it(String descriptionOfFunctionality);
    // pending test, test not yet implemented
    // results in a warning, but not a failed test

    void it(Method testToRun);
    void it(String descriptionOfFunctionality, Methods testToRun);
    // creates a test to run
    // call inside of a category

    void assertTest(Boolean expressionToTest);
    void assertTest(String description, Boolean expressionToTest);
    // takes the boolean value of the result of a test and applies it to the test
    // call inside of a test

    void before(Method setup);
    void before(String description, Method setup);
    // runs the setup method once before the category's tests begin
    // all hooks are run  in the order that defined

    void after(Method cleanup);
    void after(String description, Method cleanup);
    // runs the cleanup method once after the category's tests complete
    // all hooks are run  in the order that defined

    void beforeEach(Method setup);
    void beforeEach(String description, Method setup);
    // runs the setup method before each test in the category
    // all hooks are run  in the order that defined

    void afterEach(Method cleanup);
    void afterEach(String description, Method cleanup);
    // runs the cleanup method after each test in the category

    void run();
    // runs all tests after the tests have been defined
}
