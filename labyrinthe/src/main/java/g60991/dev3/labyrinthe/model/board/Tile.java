package g60991.dev3.labyrinthe.model.board;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract base class for all tile types.
 * Handles orientation, rotation, and exit calculation.
 */
public abstract class Tile {

    protected Orientation orientation;
    protected final Treasure treasure;
    private Set<Orientation> currentExits;

    /**
     * Creates a tile.
     *
     * @param orientation initial orientation
     * @param treasure    treasure on tile or null
     */
    protected Tile(Orientation orientation, Treasure treasure) {
        this.orientation = orientation;
        this.treasure = treasure;
        recalculateExits();
    }

    /**
     * Gets base exits before rotation.
     * Index 0=NORTH, 1=EAST, 2=SOUTH, 3=WEST.
     *
     * @return array of base exits
     */
    protected abstract boolean[] getBaseExits();

    /**
     * Recalculates exits based on current orientation.
     */
    private void recalculateExits() {
        Set<Orientation> newExits = new HashSet<>();
        boolean[] baseExits = getBaseExits();

        if (baseExits[0]) {
            newExits.add(rotateDirection(Orientation.NORTH));
        }
        if (baseExits[1]) {
            newExits.add(rotateDirection(Orientation.EAST));
        }
        if (baseExits[2]) {
            newExits.add(rotateDirection(Orientation.SOUTH));
        }
        if (baseExits[3]) {
            newExits.add(rotateDirection(Orientation.WEST));
        }

        this.currentExits = Collections.unmodifiableSet(newExits);
    }

    /**
     * Rotates a direction based on tile orientation.
     *
     * @param baseDirection original direction
     * @return rotated direction
     */
    private Orientation rotateDirection(Orientation baseDirection) {
        int rotations = this.orientation.ordinal();

        Orientation result = baseDirection;
        for (int i = 0; i < rotations; i++) {
            result = rotateOnce(result);
        }
        return result;
    }

    /**
     * Rotates a direction 90 degrees clockwise.
     *
     * @param dir direction to rotate
     * @return rotated direction
     */
    private Orientation rotateOnce(Orientation dir) {
        return switch (dir) {
            case NORTH -> Orientation.EAST;
            case EAST -> Orientation.SOUTH;
            case SOUTH -> Orientation.WEST;
            case WEST -> Orientation.NORTH;
        };
    }

    /**
     * Checks if tile has exit in direction.
     *
     * @param direction direction to check
     * @return true if exit exists
     */
    public boolean hasExit(Orientation direction) {
        return currentExits.contains(direction);
    }

    /**
     * Rotates the tile.
     *
     * @param rotation rotation direction
     * @return true (always successful)
     */
    public boolean rotate(Rotation rotation) {
        orientation = switch (rotation) {
            case CLOCKWISE -> switch (orientation) {
                case NORTH -> Orientation.EAST;
                case EAST -> Orientation.SOUTH;
                case SOUTH -> Orientation.WEST;
                case WEST -> Orientation.NORTH;
            };
            case COUNTERCLOCKWISE -> switch (orientation) {
                case NORTH -> Orientation.WEST;
                case WEST -> Orientation.SOUTH;
                case SOUTH -> Orientation.EAST;
                case EAST -> Orientation.NORTH;
            };
        };

        recalculateExits();
        return true;
    }

    /**
     * Gets all exits.
     *
     * @return unmodifiable set of exits
     */
    public Set<Orientation> getExits() {
        return currentExits;
    }

    /**
     * Gets tile shape.
     *
     * @return tile shape
     */
    public abstract Shape getShape();

    /**
     * Gets current orientation.
     *
     * @return current orientation
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Gets treasure on tile.
     *
     * @return treasure or null
     */
    public Treasure getTreasure() {
        return treasure;
    }
}