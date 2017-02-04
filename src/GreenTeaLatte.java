package src;

import java.util.LinkedList;

/**
 * A behavoir driven development testing framework
 */
public class GreenTeaLatte {
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
     * Creates a new node (branch) in the testing code with a description of it
     * <p>
     * {@code GreenTeaLatte deliciousLatte = new GreenTeaLatte();
     * deliciousLatte.describe("testing className", () -> {
     *     deliciousLatte.describe("testing methodName", () -> {
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
     * creates and attaches a test to the current node
     * <p>
     * meant to be called with the runnable of a describe method, but can be be
     * called on the root node
     *
     * @param testDescription description of the test
     * @param testCode code for the test
     */
    public void it(String testDescription, Runnable testCode) {
        // add test to current level
        this.tests.add(new ExtendedRunnable(testDescription, testCode));
    }

    /**
     * evaluates the result from a test expression
     * <p>
     * must be called within a test (inside a runnable passed to the `it` method)
     * used to determine if a test failed or passed
     *
     * @param resultFromTestExpression boolean from a user defined test expression
     */
    public void assertTest(Boolean resultFromTestExpression) {}

    /**
     * runs all tests that have been defined
     */
    public void run() {
        // temporary, testing tree structure
        System.out.println(this.toString());
        for (GreenTeaLatte child : this.children) child.run();
    }

    /**
     * converts the testing tree to a human readable string
     *
     * @return human readable string representing testing tree
     */
    public String toString() {
        String padding = "";

        for (int i = 0; i < this.depth(); i++) padding += "    ";
        String description = (this.description == null) ? "root" : this.description;
        return String.format("%sDepth %d: %s", padding, this.depth(), description);
    }
}
