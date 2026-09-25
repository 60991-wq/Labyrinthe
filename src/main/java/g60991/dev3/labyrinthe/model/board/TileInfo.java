package g60991.dev3.labyrinthe.model.board;

import java.util.Set;

/**
 * Holds immutable tile information for display.
 *
 * @param shape       tile shape
 * @param orientation current orientation
 * @param exits       set of exit directions
 * @param treasure    treasure on tile (or null)
 */
public record TileInfo(
        Shape shape,
        Orientation orientation,
        Set<Orientation> exits,
        Treasure treasure
) {

    /**
     * Creates TileInfo from a Tile.
     *
     * @param tile the tile to extract info from
     */
    public TileInfo(Tile tile) {
        this(
                tile.getShape(),
                tile.getOrientation(),
                tile.getExits(),
                tile.getTreasure()
        );
    }
}