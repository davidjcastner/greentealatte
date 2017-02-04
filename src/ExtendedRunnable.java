package src;

/**
 * extension of a runnable which attaches a description to each runnable
 */
class ExtendedRunnable {
    private String description;
    private Runnable code;

    /**
     * creates an ExtendedRunnable
     *
     * @param runnableDescription description to attach to the runnable
     * @param runnableCode the actual runnable
     */
    protected ExtendedRunnable(String runnableDescription, Runnable runnableCode) {
        this.description = runnableDescription;
        this.code        = runnableCode;
    }

    /**
     * gets the description
     *
     * @return the description
     */
    protected String getDescription() {
        return this.description;
    }

    /**
     * runs the stored runnable
     */
    protected void run() {
        this.code.run();
    }
}
