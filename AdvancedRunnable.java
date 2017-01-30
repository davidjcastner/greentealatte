package greentealatte;

public class AdvancedRunnable implements AdvancedRunnableInterface {
    protected String runnableDescription;
    protected Runnable runnableCode;

    public void setDescription(String description) {
        // sets the description that goes along with the runnable
        this.runnableDescription = description;
    }

    public String getDescription() {
        // returns the description set for the runnable
        return this.runnableDescription;
    }

    public void setRunnable(Runnable code) {
        // sets the code to run for the runnable
        this.runnableCode = code;
    }

    public void run() {
        // runs the code
        this.runnableCode.run();
    }
}
