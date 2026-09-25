package g60991.dev3.labyrinthe.model.board;

/**
 * Represents an insertion point on the board edge.
 * Defines where and in which direction a tile can be inserted.
 *
 * @param number point identifier (1-12)
 * @param position edge position
 * @param direction push direction
 */

import java.util.List;

public record InsertPoint(int number, Position position, Orientation direction) {

    // Haut (ligne 0) - pousse vers le Sud
    public static final InsertPoint POINT_1 = new InsertPoint(1, new Position(0, 1), Orientation.SOUTH);
    public static final InsertPoint POINT_2 = new InsertPoint(2, new Position(0, 3), Orientation.SOUTH);
    public static final InsertPoint POINT_3 = new InsertPoint(3, new Position(0, 5), Orientation.SOUTH);


    public static final InsertPoint POINT_4 = new InsertPoint(4, new Position(1, BoardConfig.LAST_INDEX), Orientation.WEST);
    public static final InsertPoint POINT_5 = new InsertPoint(5, new Position(3, BoardConfig.LAST_INDEX), Orientation.WEST);
    public static final InsertPoint POINT_6 = new InsertPoint(6, new Position(5, BoardConfig.LAST_INDEX), Orientation.WEST);

    // Bas (ligne 6) - pousse vers le Nord
    public static final InsertPoint POINT_7 = new InsertPoint(7, new Position(BoardConfig.LAST_INDEX, 5), Orientation.NORTH);
    public static final InsertPoint POINT_8 = new InsertPoint(8, new Position(BoardConfig.LAST_INDEX, 3), Orientation.NORTH);
    public static final InsertPoint POINT_9 = new InsertPoint(9, new Position(BoardConfig.LAST_INDEX, 1), Orientation.NORTH);

    // Gauche (colonne 0) - pousse vers l'Est
    public static final InsertPoint POINT_10 = new InsertPoint(10, new Position(5, 0), Orientation.EAST);
    public static final InsertPoint POINT_11 = new InsertPoint(11, new Position(3, 0), Orientation.EAST);
    public static final InsertPoint POINT_12 = new InsertPoint(12, new Position(1, 0), Orientation.EAST);

    public static final List<InsertPoint> ALL_POINTS = List.of(
            POINT_1, POINT_2, POINT_3, POINT_4, POINT_5, POINT_6,
            POINT_7, POINT_8, POINT_9, POINT_10, POINT_11, POINT_12
    );


    /**
     * Finds the opposite insertion point.
     *
     * @return opposite point or null if not found
     */
    public InsertPoint getOpposite() {
        Orientation oppositeDir = this.direction.opposite();

        for (InsertPoint point : ALL_POINTS) {
            if (point.direction == oppositeDir) {

                boolean sameRow = (this.direction == Orientation.EAST || this.direction == Orientation.WEST)
                        && this.position.row() == point.position.row();
                boolean sameCol = (this.direction == Orientation.NORTH || this.direction == Orientation.SOUTH)
                        && this.position.col() == point.position.col();

                if (sameRow || sameCol) {
                    return point;
                }
            }
        }
        return null;
    }

    /**
     * Finds an insertion point by position.
     *
     * @param pos position to search
     * @return matching point or null
     */
    public static InsertPoint findByPosition(Position pos) {
        for (InsertPoint point : ALL_POINTS) {
            if (point.position().equals(pos)) {
                return point;
            }
        }
        return null;
    }

}