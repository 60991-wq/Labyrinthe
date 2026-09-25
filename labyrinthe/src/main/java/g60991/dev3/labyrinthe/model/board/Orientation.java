package g60991.dev3.labyrinthe.model.board;

/**
 * Represents the four cardinal directions.
 */
public enum Orientation {

    NORTH,

    EAST,

    SOUTH,

    WEST;


    /**
     * Returns the opposite direction.
     *
     * @return opposite orientation
     */
    public Orientation opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
        };
    }
}