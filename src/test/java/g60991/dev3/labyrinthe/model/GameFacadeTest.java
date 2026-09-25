package g60991.dev3.labyrinthe.model;

import g60991.dev3.labyrinthe.model.board.InsertPoint;
import g60991.dev3.labyrinthe.model.board.Position;
import g60991.dev3.labyrinthe.model.board.Rotation;
import g60991.dev3.labyrinthe.model.game.GameFacade;
import g60991.dev3.labyrinthe.model.game.GameState;
import g60991.dev3.labyrinthe.model.game.PlayerColor;
import g60991.dev3.labyrinthe.model.util.LabyrintheException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class GameFacadeTest {
    private GameFacade game;
    @BeforeEach
    void setUp() {
     game = new GameFacade();
     game.start(2, new HashMap<>());
    }
    @Test
    void gameInitialization() {
        assertNotNull(game);
        assertEquals(GameState.INSERT, game.getState());
        assertEquals(PlayerColor.RED, game.getCurrentPlayerColor());
    }

    @Test
    void insertChangesStateToMove() {
        game.insert(InsertPoint.POINT_1);
        assertEquals(GameState.MOVE, game.getState());
    }


    @Test
    void cannotMoveInInsertPhase() {
        assertThrows(LabyrintheException.class, () ->
                game.move(new Position(1, 1))
        );
    }

    @Test
    void moveChangesPlayer() {
        PlayerColor firstPlayer = game.getCurrentPlayerColor();

        game.insert(InsertPoint.POINT_1);
        game.move(game.getCurrentPlayerPosition());

        assertNotEquals(firstPlayer, game.getCurrentPlayerColor());
    }

    @Test
    void rotateSideTileInInsertPhase() {
        assertDoesNotThrow(() -> game.rotateSideTile(Rotation.CLOCKWISE));
    }
    @Test
    void cannotRotateInMovePhase() {
        game.insert(InsertPoint.POINT_1);
        assertThrows(LabyrintheException.class, () ->
                game.rotateSideTile(Rotation.CLOCKWISE)
        );
    }

    @Test
    void gameNotOverAtStart() {
        assertFalse(game.isOver());
        assertNull(game.getWinner());
    }

    @Test
    void undoInsert() {
        game.insert(InsertPoint.POINT_1);
        assertEquals(GameState.MOVE, game.getState());

        game.undo();
        assertEquals(GameState.INSERT, game.getState());
    }
    @Test
    void undoMoveReturnsToMoveState() {
        game.insert(InsertPoint.POINT_1);
        game.move(game.getCurrentPlayerPosition());
        game.undo();

        assertEquals(GameState.MOVE, game.getState());
    }


    @Test
    void redoInsert() {
        game.insert(InsertPoint.POINT_1);
        game.undo();
        game.redo();

        assertEquals(GameState.MOVE, game.getState());
    }
    @Test
    void multipleRedoWorks() {
        game.insert(InsertPoint.POINT_1);
        game.move(game.getCurrentPlayerPosition());

        game.undo();
        game.undo();
        game.redo();
        game.redo();

        assertEquals(GameState.INSERT, game.getState());
    }
    @Test
    void newActionClearsRedoStack() {
        game.insert(InsertPoint.POINT_1);
        game.undo();

        game.insert(InsertPoint.POINT_2);
        game.redo();

        assertEquals(GameState.MOVE, game.getState());
    }
    @Test
    void undoRotateWorks() {
        game.rotateSideTile(Rotation.CLOCKWISE);
        game.undo();

        assertEquals(GameState.INSERT, game.getState());
    }

}