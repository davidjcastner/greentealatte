package greentealatte;

public class GreenTeaLatte implements GreenTeaLatteInterface {
    // behavior driven development testing framework
    protected int depth = 0;
    protected GreenTeaLatte parent;
    protected GreenTeaLatte[] children;
    protected String branchDescription = "";
    protected String padding = "\t";
    protected AdvancedRunnable[] tests;
    protected AdvancedRunnable[] hookBefore;
    protected AdvancedRunnable[] hookBeforeEach;
    protected AdvancedRunnable[] hookAfterEach;
    protected AdvancedRunnable[] hookAfter;
    protected int successfulTests      = 0;
    protected int pendingTests         = 0;
    protected int failedTests          = 0;
    protected boolean isTestSuccessful = true;

    protected void incrementSuccessfulTests() {
        // recursively adds to counter
        this.successfulTests++;
        if (this.parent != null) this.parent.incrementSuccessfulTests();
    }

    protected void incrementPendingTests() {
        // recursively adds to counter
        this.pendingTests++;
        if (this.parent != null) this.parent.incrementPendingTests();
    }

    protected void incrementFailedTests() {
        // recursively adds to counter
        this.failedTests++;
        if (this.parent != null) this.parent.incrementFailedTests();
    }

    protected void resetTestState() {
        // recursively resets the test state
        // test state will likely be called by the root element, so it is
        // important to propagate test state upward through the tree
        //
        // using a structure that propagates downward to children is inefficient
        //
        // this method ensures that all parents elements are reset to true
        this.isTestSuccessful = true;
        if (this.parent != null) this.parent.resetTestState();
    }

    protected boolean wasTestSuccessful() {
        // recursively searches for any parent that is false, if it is not false itself
        if (this.parent == null) return this.isTestSuccessful;
        // short circuits if this branch is false
        else return this.isTestSuccessful && this.parent.wasTestSuccessful();
    }

    protected void report(String reportString) {};

    public GreenTeaLatte() {}

    public GreenTeaLatte(GreenTeaLatte parentGreenTeaLatte) {
        this.parent = parentGreenTeaLatte;
        this.depth  = parentGreenTeaLatte.depth + 1;
    }

    public void setPaddingToSpaces(int amountOfSpaces) {
        // sets the padding for testing to the desired amount of spaces
        String newPadding = "";

        for (int i = 0; i < amountOfSpaces; i++) newPadding += " ";
        this.padding = newPadding;
    }

    public void describe(String description, Runnable testsToBeRun) {
        // creates a category in a tree structure, so a category can be created inside of another category
        // places tests inside of a category
    }

    public void it(String description) {
        // pending test, test not yet implemented
        // results in a warning, but not a failed test
    }

    public void it(Runnable testToRun) {}

    public void it(String description, Runnable testToRun) {
        // creates a test to run
        // call inside of a category
    }

    public void assertTest(Boolean expressionToTest) {
        // takes the boolean value of the result of a test and applies it to the test
        // call inside of a test
        if (!expressionToTest) {
            this.isTestSuccessful = false;
        }
    }

    public void before(Runnable setup) {}

    public void before(String description, Runnable setup) {
        // runs the setup runnable once before the category's tests begin
        // all hooks are run  in the order that defined
    }

    public void after(Runnable cleanup) {}

    public void after(String description, Runnable cleanup) {
        // runs the cleanup runnable once after the category's tests complete
        // all hooks are run  in the order that defined
    }

    public void beforeEach(Runnable setup) {}

    public void beforeEach(String description, Runnable setup) {
        // runs the setup runnable before each test in the category
        // all hooks are run  in the order that defined
    }

    public void afterEach(Runnable cleanup) {}

    public void afterEach(String description, Runnable cleanup) {
        // runs the cleanup runnable after each test in the category
    }

    public void run() {
        // runs all tests after the tests have been defined

        // report the branch description

        // first attempts to run all branches
        // not possible to deteremine if a branch or test was defined first,
        // but it is assumed that unit tests would be the deepest in the test structure
        // unit tests should be run first, therefore the lowest level tests will be run first
        for (GreenTeaLatte child : this.children) child.run();

        // run the before hooks
        for (AdvancedRunnable beforeCode : this.hookBefore) beforeCode.run();

        // run each test
        for (AdvancedRunnable test : this.tests) {
            // run the before each hooks
            for (AdvancedRunnable beforeEachCode : this.hookBeforeEach) beforeEachCode.run();

            // run the actual test
            this.resetTestState();
            test.run();
            if (this.wasTestSuccessful()) this.incrementSuccessfulTests();
            else this.incrementFailedTests();

            // run the after each hooks
            for (AdvancedRunnable afterEachCode : this.hookAfterEach) afterEachCode.run();
        }

        // run the after hooks
        for (AdvancedRunnable afterCode : this.hookAfter) afterCode.run();

        // complete this branch, report results
    } /* run */
}
