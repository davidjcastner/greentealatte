package src;

import java.util.LinkedList;

/**
 * A behavoir driven development testing framework
 */
public class GreenTeaLatte implements GreenTeaLatteInterface {
    // symbols used in ouput
    private static final String SYMBOL_SUCCESSFUL = "\u2713"; // check mark
    private static final String SYMBOL_WARNING    = "\u26A0"; // triangle w/ !
    private static final String SYMBOL_FAILED     = "\u2717"; // cross

    // GreenTeaLatte forms a tree of testing nodes
    // standard variables for tracking a tree structure
    private String description   = ""; // description for current node
    private GreenTeaLatte parent = null;
    private int depth = 0;
    private LinkedList<GreenTeaLatte> children = new LinkedList<GreenTeaLatte>();

    // storage for all tests and hooks in the current node
    // preserves order in which they are defined
    private LinkedList<ExtendedRunnable> tests           = new LinkedList<ExtendedRunnable>();
    private LinkedList<ExtendedRunnable> beforeHooks     = new LinkedList<ExtendedRunnable>();
    private LinkedList<ExtendedRunnable> beforeEachHooks = new LinkedList<ExtendedRunnable>();
    private LinkedList<ExtendedRunnable> afterEachHooks  = new LinkedList<ExtendedRunnable>();
    private LinkedList<ExtendedRunnable> afterHooks      = new LinkedList<ExtendedRunnable>();

    /**
     * Extension of a runnable which attaches a description to each runnable
     */
    private class ExtendedRunnable {
        // storage
        String description;
        Runnable runnable;

        /**
         * creates an ExtendedRunnable
         *
         * @param description description to attach to the runnable
         * @param runnable the actual runnable
         */
        ExtendedRunnable(String description, Runnable runnable) {
            this.description = description;
            this.runnable    = runnable;
        }
    }

    // testing statistics for current node and all descendants
    private int successfulTests = 0;
    private int pendingTests    = 0;
    private int failedTests     = 0;

    // full tree state
    private GTLState state;

    private class GTLState {
        GreenTeaLatte currentNode = null;

        // indentation information, used in output
        String singleIndentation = "\t";

        // variables for tracking tests
        boolean isRunningAllTests   = false;
        boolean isRunningTest       = false;
        boolean wasAssertTestCalled = false;
        boolean hasFailedTest       = false;

        // exception handling
        AssertionError currentAssertionError;
    }

    /**
     * Is root checker
     *
     * @return true if this is the root node
     */
    private boolean isRoot() {
        return this.depth == 0;
    }

    /**
     * Increments the successful test counter for current node and all parents
     */
    private void incrementSuccessfulTests() {
        this.successfulTests++;
        if (!this.isRoot()) this.parent.incrementSuccessfulTests();
    }

    /**
     * Increments the pending test counter for current node and all parents
     */
    private void incrementPendingTests() {
        this.pendingTests++;
        if (!this.isRoot()) this.parent.incrementPendingTests();
    }

    /**
     * Increments the failed test counter for current node and all parents
     */
    private void incrementFailedTests() {
        this.failedTests++;
        if (!this.isRoot()) this.parent.incrementFailedTests();
    }

    /**
     * Creates a string that represents the full indentation
     *
     * @param additionalIndents allows for adding additional indentation
     * @return full indentation
     */
    private String getFullIdentation(int additionalIndents) {
        int indentAmount       = this.depth + additionalIndents;
        String fullIndentation = "";

        for (int i = 0; i < indentAmount; i++) fullIndentation += this.state.singleIndentation;
        return fullIndentation;
    };

    /**
     * removes uncessary information from the stack trace for easier debugging
     *
     * @param  Throwable throwable to filter
     */
    private void filterStackTrace(Throwable throwable) {
        LinkedList<StackTraceElement> filteredStackTrace = new LinkedList<StackTraceElement>();
        for (StackTraceElement element : throwable.getStackTrace()) {
            boolean isFromClass      = element.getClassName().equals(this.getClass().getCanonicalName());
            String methodName        = element.getMethodName();
            boolean isSkipableMethod = methodName.equals("run") || methodName.equals("runHooks") ||
                methodName.equals("executeWithFilteredStackTrace") || methodName.equals("assertTest");
            if (!(isFromClass && isSkipableMethod)) {
                // the stack element was not generated from this class
                filteredStackTrace.add(element);
            }
        }
        throwable.setStackTrace(filteredStackTrace.toArray(new StackTraceElement[0]));
    }

    /**
     * Executes the runnable but filters the stack trace for easier debugging
     *
     * @param runnable the runnable which need to execute
     * @return null if no errors occured, or the altered throwable if an error did occur
     */
    private Throwable executeWithFilteredStackTrace(Runnable runnable) {
        try {
            runnable.run();
            return null; // successful
        } catch (Throwable throwable) {
            if (this.state.isRunningTest) {
                // ensuring that an error will result in a failed test rather than a pending test
                this.state.hasFailedTest = true;
            }
            // filtering the stack trace
            this.filterStackTrace(throwable);
            return throwable;
        }
    }

    /**
     * Prints out the strack trace of the throwable, but includes indentation
     *
     * @param throwable the throwable whose track trace needs to be printed
     */
    private void printIndentedStackTrace(Throwable throwable) {
        System.out.printf("%s%s\n", this.getFullIdentation(2), throwable.toString());
        for (StackTraceElement element : throwable.getStackTrace()) {
            System.out.printf("%s%s\n", this.getFullIdentation(3), element.toString());
        }
    }

    /**
     * Create a child node
     *
     * @param parent parent of node being created
     * @param nodeDescription description for the node
     */
    private GreenTeaLatte(GreenTeaLatte parent, String nodeDescription) {
        this.description = nodeDescription;
        this.parent      = parent;
        this.depth       = parent.depth + 1;
        this.state       = parent.state;
    }

    /**
     * Creates a GreenTeaLatte instance
     */
    public GreenTeaLatte() {
        this.description       = "Brewing a Green Tea Latte:";
        this.parent            = null;
        this.state             = new GTLState();
        this.state.currentNode = this;
    }

    /**
     * Creates a GreenTeaLatte instance with a description
     *
     * @param description description for entire testing tree
     */
    public GreenTeaLatte(String description) {
        this.description       = description;
        this.parent            = null;
        this.state             = new GTLState();
        this.state.currentNode = this;
    }

    /**
     * Sets the output indentation to the desired amount of spaces
     *
     * @param amountOfSpaces amount of spaces
     */
    public void setIndentationToSpaces(int amountOfSpaces) {
        // indentation information only kept on the root node
        String newIndentation = "";

        for (int i = 0; i < amountOfSpaces; i++) newIndentation += " ";
        this.state.singleIndentation = newIndentation;
    }

    /**
     * Creates a new node (branch) in the testing code with a description of it
     * <p>
     * {@code GreenTeaLatte deliciousLatte = new GreenTeaLatte();
     * deliciousLatte.describe("className", () -> {
     *     deliciousLatte.describe("methodName", () -> {
     *         // define tests and hooks for testing methodName
     *     });
     * });}
     *
     * @param nodeDescription description of the new node
     * @param nodeCode code for the tests in the current node
     */
    public void describe(String nodeDescription, Runnable nodeCode) {
        if (this.state.isRunningAllTests) {
            throw new IllegalStateException("Cannot call 'describe' within a test or hook runnable.");
        }
        // creating the new child with appropriate parent, and add to tree
        GreenTeaLatte child = new GreenTeaLatte(this.state.currentNode, nodeDescription);

        this.state.currentNode.children.add(child);

        // update current branch
        this.state.currentNode = child;

        // process child code
        nodeCode.run();

        // child code complete, reset current branch
        this.state.currentNode = child.parent;
    }

    /**
     * Creates and attaches a pending test to the current node
     * <p>
     * A pending test is a test that does not have the test code written yet.
     * Pending tests will result in a warning, but not an error.
     * <p>
     * This is meant to be called with the runnable of a describe method, but can be be
     * called on the root node.
     *
     * @param testDescription description of the pending test
     */
    public void it(String testDescription) {
        if (this.state.isRunningAllTests) {
            throw new IllegalStateException("Cannot call 'it' within a test or hook runnable.");
        }
        // add test to current level
        this.state.currentNode.tests.add(new ExtendedRunnable(testDescription, () -> {}));
    }

    /**
     * Creates and attaches a test to the current node
     * <p>
     * This is meant to be called with the runnable of a describe method, but can be be
     * called on the root node.
     * <p>
     * Not including a "assertTest" call within a test will act as a pending test and result in a warning.
     *
     * @param testDescription description of the test
     * @param testCode code for the test
     */
    public void it(String testDescription, Runnable testCode) {
        if (this.state.isRunningAllTests) {
            throw new IllegalStateException("Cannot call 'it' within a test or hook runnable.");
        }
        // add test to current level
        this.state.currentNode.tests.add(new ExtendedRunnable(testDescription, testCode));
    }

    /**
     * Evaluates the result from a test expression
     * <p>
     * This must be called within a test (inside a runnable passed to the `it` method)
     * used to determine if a test failed or passed
     *
     * @param resultFromTestExpression boolean from a user defined test expression
     */
    public void assertTest(Boolean resultFromTestExpression) {
        if (!this.state.isRunningTest) {
            throw new IllegalStateException("All 'assertTest' calls must be made within an 'it' call");
        }
        this.state.wasAssertTestCalled = true;
        if (!resultFromTestExpression) {
            this.state.hasFailedTest         = true;
            this.state.currentAssertionError = new AssertionError();
            throw this.state.currentAssertionError;
        }
    }

    /**
     * Define a block of code to run before the current node executes any tests
     * <p>
     * This only runs once for the "describe" node that it is defined in.
     * Multiple before code blocks can be defined. When there are multiple
     * blocks defined, they will execute in the order that they are defined.
     *
     * @param description description for the setup
     * @param setup code for the setup
     */
    public void before(String description, Runnable setup) {
        if (this.state.isRunningAllTests) {
            throw new IllegalStateException("Cannot call 'before' within a test or hook runnable.");
        }
        // add hook to current level
        this.state.currentNode.beforeHooks.add(new ExtendedRunnable(description, setup));
    }

    /**
     * Define a block of code to run after the current node executes any tests
     * <p>
     * This only runs once for the "describe" node that it is defined in.
     * Multiple after code blocks can be defined. When there are multiple
     * blocks defined, they will execute in the order that they are defined.
     *
     * @param description description for the cleanup
     * @param cleanup code for the cleanup
     */
    public void after(String description, Runnable cleanup) {
        if (this.state.isRunningAllTests) {
            throw new IllegalStateException("Cannot call 'after' within a test or hook runnable.");
        }
        // add hook to current level
        this.state.currentNode.afterHooks.add(new ExtendedRunnable(description, cleanup));
    }

    /**
     * Define a block of code to run before each test
     * <p>
     * This only runs once for each "it" test in the current node.
     * Multiple before code blocks can be defined. When there are multiple
     * blocks defined, they will execute in the order that they are defined.
     *
     * @param description description for the setup
     * @param setup code for the setup
     */
    public void beforeEach(String description, Runnable setup) {
        if (this.state.isRunningAllTests) {
            throw new IllegalStateException("Cannot call 'beforeEach' within a test or hook runnable.");
        }
        // add hook to current level
        this.state.currentNode.beforeEachHooks.add(new ExtendedRunnable(description, setup));
    }

    /**
     * Define a block of code to run after each test
     * <p>
     * This only runs once for each "it" test in the current node.
     * Multiple before code blocks can be defined. When there are multiple
     * blocks defined, they will execute in the order that they are defined.
     *
     * @param description description for the cleanup
     * @param cleanup code for the cleanup
     */
    public void afterEach(String description, Runnable cleanup) {
        if (this.state.isRunningAllTests) {
            throw new IllegalStateException("Cannot call 'afterEach' within a test or hook runnable.");
        }
        // add hook to current level
        this.state.currentNode.afterEachHooks.add(new ExtendedRunnable(description, cleanup));
    }

    /**
     * Loops through all hooks in a linked list and executes them
     *
     * @param LinkedList<ExtendedRunnable> list of hooks
     * @throws Throwable any error that occured in a hook
     */
    private void runHooks(LinkedList<ExtendedRunnable> hooks) {
        for (ExtendedRunnable hook : hooks) {
            System.out.printf("%s%s\n", this.getFullIdentation(1), hook.description);
            Throwable throwable = this.executeWithFilteredStackTrace(hook.runnable);
            if (throwable != null) {
                // throwing the error, otherwise it won't show up in test results
                Error error = new Error(throwable);
                this.filterStackTrace(error);
                throw error;
            }
        }
    }

    /**
     * Runs all tests that have been defined
     * <p>
     * Runs deepest level tests first.
     *
     * @throws Throwable any error that occured to stop tests from running or
     * throws a RuntimeException after all tests have been run if one of them failed
     */
    public void run() {
        // log information about current node (description)
        if (!this.isRoot()) System.out.println();
        System.out.println(this.toString());

        // run children first before executing tests
        for (GreenTeaLatte child : this.children) child.run();
        if (this.children.size() > 0) System.out.println();

        this.state.isRunningAllTests = true;

        // run all before hooks
        this.runHooks(this.beforeHooks);

        // run each test
        for (ExtendedRunnable test : this.tests) {
            // run all individual before hooks
            this.runHooks(this.beforeEachHooks);

            // running the test

            // resetting state related to state
            this.state.wasAssertTestCalled = false;
            this.state.hasFailedTest       = false;

            // call the test
            this.state.currentNode   = this;
            this.state.isRunningTest = true;
            Throwable throwable = this.executeWithFilteredStackTrace(test.runnable);
            this.state.isRunningTest = false;

            // report the test and check for successful, pending, or failed
            String symbolForTest = "";
            if (this.state.hasFailedTest) {
                this.incrementFailedTests();
                symbolForTest = SYMBOL_FAILED;
            } else if (!this.state.wasAssertTestCalled) {
                this.incrementPendingTests();
                symbolForTest = SYMBOL_WARNING;
            } else {
                this.incrementSuccessfulTests();
                symbolForTest = SYMBOL_SUCCESSFUL;
            }
            System.out.printf("%s%s %s\n", this.getFullIdentation(1), symbolForTest, test.description);
            if (throwable != null) {
                this.printIndentedStackTrace(throwable);
            }

            // run all individual after hooks
            this.runHooks(this.afterEachHooks);
        }

        // run all after hooks
        this.runHooks(this.afterHooks);

        // report information on amount of tests successful, pending, and failed
        if (this.isRoot()) System.out.println("Test Summary:");
        else System.out.println();

        System.out.printf("%s%d passed %s\n", this.getFullIdentation(1), this.successfulTests, SYMBOL_SUCCESSFUL);
        if (this.pendingTests > 0) {
            System.out.printf("%s%d pending %s\n", this.getFullIdentation(1), this.pendingTests, SYMBOL_WARNING);
        }
        System.out.printf("%s%d failed %s\n", this.getFullIdentation(1), this.failedTests, SYMBOL_FAILED);

        if (this.isRoot()) {
            System.out.println();
            if (this.pendingTests > 0) System.out.printf("%s %d pending tests!\n", SYMBOL_WARNING, this.pendingTests);
            if (this.failedTests <= 0) System.out.printf("%s All tests passed!\n", SYMBOL_SUCCESSFUL);
            else System.out.printf("%s Tests have failed!\n", SYMBOL_FAILED);
        }

        this.state.isRunningAllTests = false;

        // throwing runtime exception if at least one test failed on the full tree
        if (this.isRoot() && this.failedTests > 0) {
            throw new Error("Did not pass all tests");
        }
    } /* run */

    /**
     * Create a human readable string of current node
     *
     * @return description with indentation
     */
    public String toString() {
        return String.format("%s%s", this.getFullIdentation(0), this.description);
    }
}
