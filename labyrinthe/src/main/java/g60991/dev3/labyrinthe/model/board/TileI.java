package g60991.dev3.labyrinthe.model.board;

/**
 * Straight corridor tile with two opposite exits.
 */
public class TileI extends Tile {

    /**
     * Creates an I-shaped tile.
     *
     * @param orientation tile orientation
     * @param treasure    treasure on tile or null
     */
    public TileI(Orientation orientation, Treasure treasure) {
        super(orientation, treasure);
    }

    @Override
    protected boolean[] getBaseExits() {
        return new boolean[]{true, false, true, false};
    }

    @Override
    public Shape getShape() {
        return Shape.I;
    }
}