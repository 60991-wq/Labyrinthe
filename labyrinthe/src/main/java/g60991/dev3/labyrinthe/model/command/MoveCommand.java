package g60991.dev3.labyrinthe.model.command;

import g60991.dev3.labyrinthe.model.board.Board;
import g60991.dev3.labyrinthe.model.board.Position;
import g60991.dev3.labyrinthe.model.game.Player;


/**
 * Command to move a player to a new position.
 */
public class MoveCommand implements Command {

    private final Board board;
    private final Player player;
    private final Position newPosition;

    private Position oldPosition;
    private boolean objectiveFound = false;


    /**
     * Creates a move command.
     *
     * @param board       game board
     * @param player      player to move
     * @param newPosition target position
     */
    public MoveCommand(Board board, Player player, Position newPosition) {
        this.board = board;
        this.player = player;
        this.newPosition = newPosition;
    }

    /**
     * Executes the player movement.
     */
    @Override
    public void execute() {

        oldPosition = board.getPlayerPosition(player.getColor());

        board.movePlayer(newPosition, player.getColor());

        if (objectiveFound) {
            player.objectiveFound();
        }
    }

    /**
     * Reverses the movement and objective collection if needed.
     */
    @Override
    public void unexecute() {

        if (objectiveFound) {
            player.undoLastObjective();
        }

        System.out.println(oldPosition);
        board.movePlayer(oldPosition, player.getColor());

    }

    /**
     * Checks if an objective was found during this move.
     *
     * @return true if objective was collected
     */
    public boolean wasObjectiveFound() {
        return objectiveFound;
    }

    public void setObjectiveFound(boolean objectiveFound) {
        this.objectiveFound = objectiveFound;
    }

}