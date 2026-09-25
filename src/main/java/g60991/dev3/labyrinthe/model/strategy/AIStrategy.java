package g60991.dev3.labyrinthe.model.strategy;

import g60991.dev3.labyrinthe.model.board.Position;
import g60991.dev3.labyrinthe.model.game.GameFacade;

/**
 * Strategy interface for AI player decisions.
 */
public interface AIStrategy {

    /**
     * Chooses insertion point and rotation for side tile.
     *
     * @param model game facade
     * @return insertion choice
     */
    InsertionChoice chooseInsertion(GameFacade model);

    /**
     * Chooses where to move the player.
     *
     * @param model game facade
     * @return target position
     */
    Position chooseMove(GameFacade model);
}