package g60991.dev3.labyrinthe.model.strategy;

import g60991.dev3.labyrinthe.model.board.InsertPoint;
import g60991.dev3.labyrinthe.model.board.Rotation;

/**
 * Represents an AI's tile insertion choice.
 *
 * @param point    insertion point
 * @param rotation side tile rotation
 */
public record InsertionChoice(InsertPoint point, Rotation rotation) {
}