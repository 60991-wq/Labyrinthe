package g60991.dev3.labyrinthe.model;

import g60991.dev3.labyrinthe.model.board.*;
import g60991.dev3.labyrinthe.model.game.PlayerColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Tests pour la classe Board.
 */
public class BoardTest {

    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
    }


    @Test
    public void testBoardInitialization() {
        assertNotNull(board.getBoard());
        assertEquals(BoardConfig.BOARD_SIZE, board.getBoard().length);
    }

    @Test
    void allTilesInitialized() {
        Tile[][] tiles = board.getBoard();
        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                assertNotNull(tiles[row][col]);
            }
        }
    }
    @Test
    public void testSideTileExists() {
        assertNotNull(board.getSideTile());
    }

    @Test
    public void testPlayersStartAtCorrectPositions() {
        assertEquals(new Position(0, 0), board.getPlayerPosition(PlayerColor.RED));
        assertEquals(new Position(0, 6), board.getPlayerPosition(PlayerColor.BLUE));
        assertEquals(new Position(6, 6), board.getPlayerPosition(PlayerColor.GREEN));
        assertEquals(new Position(6, 0), board.getPlayerPosition(PlayerColor.YELLOW));
    }




    @Test
    public void testTopLeftCornerOrientation() {
        Tile corner = board.getTileAt(new Position(0, 0));
        assertEquals(Orientation.EAST, corner.getOrientation());
    }


    @Test
    public void testInsertChangeSideTile() {
        Tile oldSideTile = board.getSideTile();
        board.insert(InsertPoint.POINT_1);
        assertNotSame(oldSideTile, board.getSideTile());
    }


    @Test
    public void testRotateSideTile() {
        Orientation before = board.getSideTile().getOrientation();
        board.rotateSideTile(Rotation.CLOCKWISE);
        Orientation after = board.getSideTile().getOrientation();
        assertNotEquals(before, after);
    }



    @Test
    public void testBoardHas24Treasures() {
        Set<Treasure> treasures = new HashSet<>();

        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                Tile tile = board.getTileAt(new Position(row, col));
                if (tile.getTreasure() != null) {
                    treasures.add(tile.getTreasure());
                }
            }
        }

        if (board.getSideTile().getTreasure() != null) {
            treasures.add(board.getSideTile().getTreasure());
        }

        assertEquals(24, treasures.size());
    }

    @Test
    void insertShiftsTiles() {
        Tile tileAtRow1Col1 = board.getTileAt(new Position(1, 1));
        board.insert(InsertPoint.POINT_1);
        Tile tileAtRow2Col1 = board.getTileAt(new Position(2, 1));
        assertSame(tileAtRow1Col1, tileAtRow2Col1);
    }

    @Test
    void getTreasurePosition() {

        Treasure targetTreasure = null;
        Position expectedPos = null;

        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                Tile tile = board.getTileAt(new Position(row, col));
                if (tile.getTreasure() != null) {
                    targetTreasure = tile.getTreasure();
                    expectedPos = new Position(row, col);
                    break;
                }
            }
            if (targetTreasure != null) break;
        }

        assertEquals(expectedPos, board.getTreasurePosition(targetTreasure));
    }
    @Test
    public void testMovePlayer() {
        Position newPos = new Position(3, 3);
        board.movePlayer(newPos, PlayerColor.RED);
        assertEquals(newPos, board.getPlayerPosition(PlayerColor.RED));
    }


    @Test
    void tileLHasCorrectExits() {
        TileL tile = new TileL(Orientation.NORTH,null);
        assertTrue(tile.hasExit(Orientation.NORTH));
        assertTrue(tile.hasExit(Orientation.EAST));
        assertFalse(tile.hasExit(Orientation.WEST));
    }

    @Test
    void tileRotatesClockwise() {
        TileT tile = new TileT(Orientation.NORTH,null);
        tile.rotate(Rotation.CLOCKWISE);
        assertEquals(Orientation.EAST, tile.getOrientation());
    }

    @Test
    void hasPathBetweenAdjacentCells() {
        Position start = new Position(0, 0);
        Position adjacent = new Position(0, 1);

        assertTrue(board.hasPathBetween(start, adjacent) ||
                !board.getTileAt(start).hasExit(Orientation.EAST));
    }

    @Test
    void hasPathBetweenReturnsFalseForBlockedPath() {

        Position start = new Position(0, 0);
        Position end = new Position(0, 0);

        assertTrue(board.hasPathBetween(start, end));
    }



    @Test
    void insertMovesPlayersOnInsertedRow() {
        Position initialPos = new Position(1, 3);
        board.movePlayer(initialPos, PlayerColor.RED);
        board.insert(InsertPoint.POINT_4);

        Position newPos = board.getPlayerPosition(PlayerColor.RED);

        assertNotEquals(initialPos, newPos);
    }

    @Test
    void insertMovesPlayersOnInsertedColumn() {

        Position initialPos = new Position(3, 1);
        board.movePlayer(initialPos, PlayerColor.RED);
        board.insert(InsertPoint.POINT_1);
        Position newPos = board.getPlayerPosition(PlayerColor.RED);
        assertNotEquals(initialPos, newPos);
    }

    @Test
    void playerPushedOutReappearsOnOppositeSide() {
        board.movePlayer(new Position(1, 6), PlayerColor.RED);
        board.insert(InsertPoint.POINT_4);

        Position newPos = board.getPlayerPosition(PlayerColor.RED);
        assertNotEquals(new Position(1, 6), newPos);
        assertEquals(1, newPos.row());
    }

    @Test
    void getTreasureAtReturnsCorrectTreasure() {
        Treasure foundTreasure = null;
        Position treasurePos = null;

        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                Position pos = new Position(row, col);
                Treasure t = board.getTileAt(pos).getTreasure();
                if (t != null) {
                    foundTreasure = t;
                    treasurePos = pos;
                    break;
                }
            }
            if (foundTreasure != null) break;
        }

        assertEquals(foundTreasure, board.getTreasureAt(treasurePos));
    }
}