package g60991.dev3.labyrinthe.model.board;

/**
 * Represents a position on the game board.
 *
 * @param row the row (vertical coordinate)
 * @param col the column (horizontal coordinate)
 */
public record Position(int row, int col) {

    /**
     * Checks if this position is within board bounds.
     *
     * @param boardSize the board size
     * @return true if position is valid
     */
    public boolean isInBounds(int boardSize) {
        return row >= 0 && row < boardSize && col >= 0 && col < boardSize;
    }

    /**
     * Returns the neighbor position in a given direction.
     *
     * @param direction the neighbor direction
     * @return the new position
     */
    public Position getNeighbor(Orientation direction) {
        return switch (direction) {
            case NORTH -> new Position(row - 1, col);
            case SOUTH -> new Position(row + 1, col);
            case EAST -> new Position(row, col + 1);
            case WEST -> new Position(row, col - 1);
        };
    }
}