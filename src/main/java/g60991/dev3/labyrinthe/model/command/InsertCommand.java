package g60991.dev3.labyrinthe.model.command;

import g60991.dev3.labyrinthe.model.board.Board;
import g60991.dev3.labyrinthe.model.board.InsertPoint;

/**
 * Command to insert a tile at a specific point on the board.
 */
public class InsertCommand implements Command {

    private final Board board;
    private final InsertPoint point;
    private InsertPoint oldPoint;

    /**
     * Creates an insert command.
     *
     * @param board game board
     * @param point insertion point
     */
    public InsertCommand(Board board, InsertPoint point) {
        this.board = board;
        this.point = point;
    }

    /**
     * Executes the tile insertion.
     */
    @Override
    public void execute() {
        boolean isRedo = oldPoint != null;
        oldPoint = board.getLastInsertPoint();
        if (isRedo) {
            board.insert(point);
        }
    }

    /**
     * Reverses the insertion by inserting at opposite point.
     */
    @Override
    public void unexecute() {
        board.insert(point.getOpposite());
        board.setLastInsertPoint(oldPoint);
    }
}