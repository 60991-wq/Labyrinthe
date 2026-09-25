package g60991.dev3.labyrinthe.model.game;

import g60991.dev3.labyrinthe.model.board.Position;
import g60991.dev3.labyrinthe.model.board.Treasure;

/**
 * Holds immutable player information.
 *
 * @param color                 player color
 * @param startPosition         starting position
 * @param currentObjective      current objective to find
 * @param remainingObjectives   count of objectives not yet found
 * @param foundObjectivesCount  count of objectives found
 * @param hasFoundAllObjectives true if all objectives found
 */
public record PlayerInfo(
        PlayerColor color,
        Position startPosition,
        Treasure currentObjective,
        int remainingObjectives,
        int foundObjectivesCount,
        boolean hasFoundAllObjectives
) {

    /**
     * Creates PlayerInfo from a Player.
     *
     * @param player the source player
     */
    public PlayerInfo(Player player) {
        this(
                player.getColor(),
                player.getStartPosition(),
                player.getCurrentObjective(),
                player.getRemainingObjectives(),
                player.getFoundObjectivesCount(),
                player.hasFoundAllObjectives()
        );
    }
}