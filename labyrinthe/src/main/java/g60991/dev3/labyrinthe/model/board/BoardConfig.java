package g60991.dev3.labyrinthe.model.board;

import java.util.List;

/**
 * Configuration constants for the Labyrinth game board.
 * Defines board dimensions, tile counts, and fixed tile positions.
 */
public final class BoardConfig {

    /**
     * Size of the game board (7x7 grid).
     */
    public static final int BOARD_SIZE = 7;

    /**
     * Last valid index on the board (6 for 7x7 grid).
     */
    public static final int LAST_INDEX = BOARD_SIZE - 1;

    /**
     * Number of objectives per player.
     */
    public static final int OBJECTIVES_PER_PLAYER = 6;

    /**
     * Number of mobile T-shaped tiles with treasures.
     */
    public static final int MOBILE_T_WITH_TREASURE_COUNT = 6;

    /**
     * Number of mobile L-shaped tiles with treasures.
     */
    public static final int MOBILE_L_WITH_TREASURE_COUNT = 6;

    /**
     * Number of mobile L-shaped tiles without treasures.
     */
    public static final int MOBILE_L_WITHOUT_TREASURE_COUNT = 10;

    /**
     * Number of mobile I-shaped tiles.
     */
    public static final int MOBILE_I_COUNT = 12;

    /**
     * Information about a fixed tile's position and orientation.
     *
     * @param row         row index
     * @param col         column index
     * @param orientation tile orientation
     */
    public record FixedTileInfo(int row, int col, Orientation orientation) {
    }

    /**
     * Fixed L-shaped tiles at the four corners.
     * Corners are always fixed and cannot be moved.
     */
    public static final List<FixedTileInfo> FIXED_L_CORNERS = List.of(
            new FixedTileInfo(0, 0, Orientation.EAST),
            new FixedTileInfo(0, LAST_INDEX, Orientation.SOUTH),
            new FixedTileInfo(LAST_INDEX, 0, Orientation.NORTH),
            new FixedTileInfo(LAST_INDEX, LAST_INDEX, Orientation.WEST)
    );

    /**
     * Fixed T-shaped tiles with treasures.
     * These tiles are placed at specific positions and hold treasures.
     */
    public static final List<FixedTileInfo> FIXED_T_WITH_TREASURE = List.of(
            new FixedTileInfo(0, 2, Orientation.SOUTH),
            new FixedTileInfo(0, 4, Orientation.SOUTH),

            new FixedTileInfo(LAST_INDEX, 2, Orientation.NORTH),
            new FixedTileInfo(LAST_INDEX, 4, Orientation.NORTH),

            new FixedTileInfo(2, 0, Orientation.EAST),
            new FixedTileInfo(4, 0, Orientation.EAST),

            new FixedTileInfo(2, LAST_INDEX, Orientation.WEST),
            new FixedTileInfo(4, LAST_INDEX, Orientation.WEST),

            new FixedTileInfo(2, 2, Orientation.WEST),
            new FixedTileInfo(2, 4, Orientation.NORTH),
            new FixedTileInfo(4, 2, Orientation.SOUTH),
            new FixedTileInfo(4, 4, Orientation.WEST)
    );


}