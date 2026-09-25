package g60991.dev3.labyrinthe.model.board;

/**
 * Corner tile with two perpendicular exits.
 */
public class TileL extends Tile {

    /**
     * Creates an L-shaped tile.
     *
     * @param orientation tile orientation
     * @param treasure    treasure on tile or null
     */
    public TileL(Orientation orientation, Treasure treasure) {
        super(orientation, treasure);
    }

    @Override
    protected boolean[] getBaseExits() {
        return new boolean[]{true, true, false, false};
    }

    @Override
    public Shape getShape() {
        return Shape.L;
    }
}