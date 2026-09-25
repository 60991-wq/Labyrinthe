package g60991.dev3.labyrinthe.model.command;

import java.util.Stack;

/**
 * Manages command execution with undo/redo functionality.
 */
public class CommandManager {

    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    /**
     * Executes a command and adds it to undo history.
     * Clears redo history.
     *
     * @param command command to execute
     */
    public void doIt(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }

    /**
     * Undoes the last command.
     *
     * @return undone command or null if nothing to undo
     */
    public Command undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.unexecute();
            redoStack.push(command);
            return command;
        }
        return null;
    }

    /**
     * Redoes the last undone command.
     *
     * @return redone command or null if nothing to redo
     */
    public Command redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
            return command;
        }
        return null;
    }
}