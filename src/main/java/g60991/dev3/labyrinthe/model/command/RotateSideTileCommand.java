package g60991.dev3.labyrinthe.model.command;

import g60991.dev3.labyrinthe.model.board.Board;
import g60991.dev3.labyrinthe.model.board.Rotation;

/**
 * Command to rotate the side tile.
 */
public class RotateSideTileCommand implements Command {

    private final Board board;
    private final Rotation rotation;

    /**
     * Creates a rotation command.
     *
     * @param board    game board
     * @param rotation rotation direction
     */
    public RotateSideTileCommand(Board board, Rotation rotation) {
        this.board = board;
        this.rotation = rotation;
    }

    /**
     * Executes the rotation.
     */
    @Override
    public void execute() {
        board.rotateSideTile(rotation);
    }

    /**
     * Reverses the rotation by applying opposite direction.
     */
    @Override
    public void unexecute() {

        Rotation opposite = (rotation == Rotation.CLOCKWISE)
                ? Rotation.COUNTERCLOCKWISE
                : Rotation.CLOCKWISE;

        board.rotateSideTile(opposite);
    }
}