package src;

import java.util.LinkedList;

/**
 * A behavoir driven development testing framework
 */
public class GreenTeaLatte implements GreenTeaLatteInterface {
    // indentation information, used in output
    private boolean useSpaces   = false;
    private int indentationSize = 0;

    // GreenTeaLatte forms a tree of testing nodes
    // standard variables for tracking a tree structure
    private GreenTeaLatte parent;
    private LinkedList<GreenTeaLatte> children = new LinkedList<GreenTeaLatte>();
    private String description;        // description for current node
    private GreenTeaLatte currentNode; // tracks the current node in the root instance

    // storage for all tests and hooks in the current node
    // preserves order from they are defined
    private LinkedList<ExtendedRunnable> tests           = new LinkedList<ExtendedRunnable>();
    private LinkedList<ExtendedRunnable> beforeHooks     = new LinkedList<ExtendedRunnable>();
    private LinkedList<ExtendedRunnable> beforeEachHooks = new LinkedList<ExtendedRunnable>();
    private LinkedList<ExtendedRunnable> afterEachHooks  = new LinkedList<ExtendedRunnable>();
    private LinkedList<ExtendedRunnable> afterHooks      = new LinkedList<ExtendedRunnable>();

    // testing statistics for current node and all descendants
    private int successfulTests;
    private int pendingTests;
    private int failedTests;

    /**
     * Private method for finding the depth of the current node in the testing tree
     *
     * @return depth level with 0 being the root elements
     */
    private int depth() {
        // finds depth depth cursively
        if (this.parent == null) return 0;  // root node
        else return this.parent.depth() + 1;
    }

    /**
     * Updates the tree to track the node that is running code
     * <p>
     * Only keeps the current node state in the root node
     */
    private void setCurrentBranch(GreenTeaLatte branch) {
        if (this.parent == null) this.currentNode = branch;  // root node
        else this.parent.setCurrentBranch(branch);
    }

    /**
     * Creates a string that represents a single indentation
     *
     * @return single indentation string
     */
    private String getSingleIdentationString() {
        // only the root store information about the padding
        // so it searches for root first
        if (this.parent == null) { // root node
            if (this.useSpaces) {
                String indent = "";
                for (int i = 0; i < this.indentationSize; i++) indent += " ";
                return indent;
            } else { return "\t"; }
        } else { return this.parent.getSingleIdentationString(); }
    };

    /**
     * Creates a string that represents the full indentation
     *
     * @param additionalIndents allows for adding additional indentation
     * @return full indentation
     */
    private String getFullIdentationString(int additionalIndents) {
        int indentAmount         = this.depth();
        String fullIndentation   = "";
        String singleIndentation = this.getSingleIdentationString();

        indentAmount += additionalIndents;
        for (int i = 0; i < indentAmount; i++) fullIndentation += singleIndentation;
        return fullIndentation;
    };

    /**
     * Create a child node
     *
     * @param parent parent of node being created
     * @param nodeDescription description for the node
     */
    private GreenTeaLatte(GreenTeaLatte parent, String nodeDescription) {
        this.parent      = parent;
        this.description = nodeDescription;
    }

    /**
     * Creates a GreenTeaLatte instance
     */
    public GreenTeaLatte() {
        this.parent = null;
    }

    /**
     * Sets the output indentation to the desired amount of spaces
     *
     * @param amountOfSpaces amount of spaces
     */
    public void setIndentationToSpaces(int amountOfSpaces) {
        // indentation information only kept on the root node
        if (this.parent == null) { // root node
            this.useSpaces       = true;
            this.indentationSize = amountOfSpaces;
        } else { this.parent.setIndentationToSpaces(amountOfSpaces); }
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
        // creating the new child with appropriate parent, and add to tree
        GreenTeaLatte child;

        if (this.currentNode == null) {
            // root of driver
            child = new GreenTeaLatte(this, nodeDescription);
            this.children.add(child);
        } else {
            child = new GreenTeaLatte(currentNode, nodeDescription);
            currentNode.children.add(child);
        }

        // update current branch
        this.setCurrentBranch(child);

        // process child code
        nodeCode.run();

        // child code complete, reset current branch
        this.setCurrentBranch(child.parent);
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
        // add test to current level
        this.currentNode.tests.add(new ExtendedRunnable(testDescription, () -> {}));
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
        // add test to current level
        this.currentNode.tests.add(new ExtendedRunnable(testDescription, testCode));
    }

    /**
     * Evaluates the result from a test expression
     * <p>
     * This must be called within a test (inside a runnable passed to the `it` method)
     * used to determine if a test failed or passed
     *
     * @param resultFromTestExpression boolean from a user defined test expression
     */
    public void assertTest(Boolean resultFromTestExpression) {}

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
        // add hook to current level
        this.currentNode.beforeHooks.add(new ExtendedRunnable(description, setup));
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
        // add hook to current level
        this.currentNode.afterHooks.add(new ExtendedRunnable(description, cleanup));
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
        // add hook to current level
        this.currentNode.beforeEachHooks.add(new ExtendedRunnable(description, setup));
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
        // add hook to current level
        this.currentNode.afterEachHooks.add(new ExtendedRunnable(description, cleanup));
    }

    /**
     * Runs all tests that have been defined
     * <p>
     * Runs deepest level tests first.
     */
    public void run() {
        // log information about current node (description)
        if (this.description != null) System.out.println(this.toString());

        // run children first before executing tests
        for (GreenTeaLatte child : this.children) child.run();

        // run all before hooks
        for (ExtendedRunnable hook : this.beforeHooks) {
            System.out.printf("%s%s\n", this.getFullIdentationString(1), hook.getDescription());
            hook.run();
        }

        // run each test
        for (ExtendedRunnable test : this.tests) {
            // run all individual before hooks
            for (ExtendedRunnable hook : this.beforeEachHooks) {
                System.out.printf("%s%s\n", this.getFullIdentationString(1), hook.getDescription());
                hook.run();
            }

            // run the test
            System.out.printf("%s%s\n", this.getFullIdentationString(1), test.getDescription());
            test.run();

            // run all individual after hooks
            for (ExtendedRunnable hook : this.afterEachHooks) {
                System.out.printf("%s%s\n", this.getFullIdentationString(1), hook.getDescription());
                hook.run();
            }
        }

        // run all after hooks
        for (ExtendedRunnable hook : this.afterHooks) {
            System.out.printf("%s%s\n", this.getFullIdentationString(1), hook.getDescription());
            hook.run();
        }

        // TODO: report information on amount of tests passed and failed

        // TODO: throw error if current node is the root and if tests have failed
    } /* run */

    /**
     * Create a human readable string of testing structure
     *
     * @return human readable string representing testing tree
     */
    public String toString() {
        String description = (this.description == null) ? "" : this.description;

        return String.format("%s%s", this.getFullIdentationString(0), description);
    }
}
