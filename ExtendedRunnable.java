public class ExtendedRunnable {
    protected String description;
    protected Runnable code;

    public ExtendedRunnable(String runnableDescription, Runnable runnableCode) {
        this.description = runnableDescription;
        this.code        = runnableCode;
    }

    public String getDescription() {
        return this.description;
    }

    public void run() {
        this.code.run();
    }
}
