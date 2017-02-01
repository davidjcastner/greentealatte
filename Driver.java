import java.util.LinkedList;

public class Driver {
    protected Driver parent;
    protected LinkedList<Driver> children = new LinkedList<Driver>();
    protected String description;
    protected Driver currentBranch;

    protected LinkedList<ExtendedRunnable> tests           = new LinkedList<ExtendedRunnable>();
    protected LinkedList<ExtendedRunnable> beforeHooks     = new LinkedList<ExtendedRunnable>();
    protected LinkedList<ExtendedRunnable> beforeEachHooks = new LinkedList<ExtendedRunnable>();
    protected LinkedList<ExtendedRunnable> afterEachHooks  = new LinkedList<ExtendedRunnable>();
    protected LinkedList<ExtendedRunnable> afterHooks      = new LinkedList<ExtendedRunnable>();

    protected int depth() {
        if (this.parent == null) return 0;
        else return this.parent.depth() + 1;
    }

    protected void setCurrentBranch(Driver branch) {
        if (this.parent == null) this.currentBranch = branch;
        else this.parent.setCurrentBranch(branch);
    }

    protected Driver(Driver parent, String branchDescription) {
        this.parent      = parent;
        this.description = branchDescription;
    }

    public Driver() {
        this.parent = null;
    }

    public void describe(String branchDescription, Runnable branchCode) {
        // creating the new child with appropriate parent, and add to tree
        Driver child;

        if (this.currentBranch == null) {
            // root of driver
            child = new Driver(this, branchDescription);
            this.children.add(child);
        } else {
            child = new Driver(currentBranch, branchDescription);
            currentBranch.children.add(child);
        }

        // update current branch
        this.setCurrentBranch(child);

        // process child code
        branchCode.run();

        // child code complete, reset current branch
        this.setCurrentBranch(child.parent);
    }

    public void it(String testDescription, Runnable testCode) {
        // add test to current level
    }

    public void assertTest(Boolean testResults) {}

    public String toString() {
        String padding = "";

        for (int i = 0; i < this.depth(); i++) padding += "    ";
        String description = (this.description == null) ? "root" : this.description;
        return String.format("%sDepth %d: %s", padding, this.depth(), description);
    }

    public void run() {
        // temporary, testing tree structure
        System.out.println(this.toString());
        for (Driver child : this.children) child.run();
    }
}
