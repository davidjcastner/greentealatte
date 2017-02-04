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
    ExtendedRunnable(String runnableDescription, Runnable runnableCode) {
        this.description = runnableDescription;
        this.code        = runnableCode;
    }

    /**
     * gets the description
     *
     * @return the description
     */
    String getDescription() {
        return this.description;
    }

    /**
     * runs the stored runnable
     */
    void run() {
        this.code.run();
    }
}
