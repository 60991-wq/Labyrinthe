package g60991.dev3.labyrinthe.model.game;

import g60991.dev3.labyrinthe.model.board.BoardConfig;
import g60991.dev3.labyrinthe.model.board.Position;
import g60991.dev3.labyrinthe.model.board.Treasure;
import g60991.dev3.labyrinthe.model.util.LabyrintheException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Represents a player in the labyrinth game.
 * Manages objectives, color, and game progression.
 */
public class Player {
    private final PlayerColor playerColor;
    private final Position startPosition;
    private final Stack<Treasure> objectives;
    private final List<Treasure> foundObjectives;

    /**
     * Creates a player with objectives.
     *
     * @param playerColor player's color
     * @param objectives  list of treasures to find
     * @throws LabyrintheException if color is null or objectives are empty
     */
    public Player(PlayerColor playerColor, List<Treasure> objectives) {
        if (playerColor == null) {
            throw new LabyrintheException("Player color cannot be null");
        }
        if (objectives == null || objectives.isEmpty()) {
            throw new LabyrintheException("Player must have at least one objective");
        }

        this.playerColor = playerColor;
        this.startPosition = getStartPositionFromColor(playerColor);
        this.foundObjectives = new ArrayList<>();

        this.objectives = new Stack<>();
        List<Treasure> reversed = new ArrayList<>(objectives);
        Collections.reverse(reversed);
        this.objectives.addAll(reversed);
    }

    /**
     * Gets starting position based on player color.
     *
     * @param color player color
     * @return starting position
     */
    private static Position getStartPositionFromColor(PlayerColor color) {
        return switch (color) {
            case RED -> new Position(0, 0);
            case BLUE -> new Position(0, BoardConfig.LAST_INDEX);
            case GREEN -> new Position(BoardConfig.LAST_INDEX, BoardConfig.LAST_INDEX);
            case YELLOW -> new Position(BoardConfig.LAST_INDEX, 0);
        };
    }

    /**
     * Gets the current objective to find.
     *
     * @return current objective or null if all found
     */
    public Treasure getCurrentObjective() {
        return objectives.isEmpty() ? null : objectives.peek();
    }

    /**
     * Marks current objective as found.
     */
    public void objectiveFound() {
        if (!objectives.isEmpty()) {
            Treasure found = objectives.pop();
            foundObjectives.add(found);
        }
    }

    /**
     * Checks if all objectives have been found.
     *
     * @return true if no objectives remain
     */
    public boolean hasFoundAllObjectives() {
        return objectives.isEmpty();
    }

    /**
     * Checks if player has won the game.
     * Must have found all objectives and returned to start.
     *
     * @param currentPosition current player position
     * @return true if player has won
     */
    public boolean hasWon(Position currentPosition) {
        return hasFoundAllObjectives() && currentPosition.equals(this.startPosition);
    }

    /**
     * Gets player color.
     *
     * @return player color
     */
    public PlayerColor getColor() {
        return playerColor;
    }

    /**
     * Gets starting position.
     *
     * @return start position
     */
    public Position getStartPosition() {
        return startPosition;
    }

    /**
     * Gets number of remaining objectives.
     *
     * @return count of objectives not yet found
     */
    public int getRemainingObjectives() {
        return objectives.size();
    }

    /**
     * Gets number of found objectives.
     *
     * @return count of objectives found
     */
    public int getFoundObjectivesCount() {
        return foundObjectives.size();
    }

    public List<Treasure> getFoundTreasures() {
        return foundObjectives;
    }


    /**
     * Undoes the last objective found (for undo command).
     */
    public void undoLastObjective() {
        if (!foundObjectives.isEmpty()) {
            Treasure lastFound = foundObjectives.remove(foundObjectives.size() - 1);
            objectives.push(lastFound);
        }
    }

}