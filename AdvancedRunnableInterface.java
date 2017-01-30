package greentealatte;

public interface AdvancedRunnableInterface {
    void setDescription(String description);
    // sets the description that goes along with the runnable

    String getDescription();
    // returns the description set for the runnable

    void setRunnable(Runnable code);
    // sets the code to run for the runnable

    void run();
    // runs the code
}
