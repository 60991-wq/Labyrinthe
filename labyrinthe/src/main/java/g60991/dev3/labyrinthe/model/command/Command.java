package g60991.dev3.labyrinthe.model.command;

/**
 * Represents an executable and reversible command.
 */
public interface Command {
    /**
     * Executes the command.
     */
    void execute();

    /**
     * Reverses the command execution.
     */
    void unexecute();
}
