package g60991.dev3.labyrinthe.model.board;

/**
 * T-junction tile with three exits.
 */
public class TileT extends Tile {

    /**
     * Creates a T-shaped tile.
     *
     * @param orientation tile orientation
     * @param treasure    treasure on tile or null
     */
    public TileT(Orientation orientation, Treasure treasure) {
        super(orientation, treasure);
    }

    @Override
    protected boolean[] getBaseExits() {
        return new boolean[]{true, true, false, true};
    }

    @Override
    public Shape getShape() {
        return Shape.T;
    }
}