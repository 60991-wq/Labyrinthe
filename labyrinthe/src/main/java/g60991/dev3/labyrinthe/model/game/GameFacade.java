package g60991.dev3.labyrinthe.model.game;

import g60991.dev3.labyrinthe.model.board.*;
import g60991.dev3.labyrinthe.model.command.*;
import g60991.dev3.labyrinthe.model.strategy.AIStrategy;
import g60991.dev3.labyrinthe.model.util.LabyrintheException;
import g60991.dev3.labyrinthe.model.util.Observable;
import g60991.dev3.labyrinthe.model.util.Observer;

import java.util.*;

/**
 * Main facade for the Labyrinth game.
 * Single entry point for all game operations.
 * Coordinates board, players, commands, and AI strategies.
 * Implements Facade and Observable patterns.
 */
public class GameFacade implements Observable {

    private Board board;
    private Player[] players;
    private int currentPlayerIndex;
    private GameState state;
    private final CommandManager commandManager = new CommandManager();
    private InsertPoint lastInsertionPoint;
    private final Set<Observer> observers = new HashSet<>();
    private Map<PlayerColor, AIStrategy> playerStrategies;

    public void start(int playerCount, Map<PlayerColor, AIStrategy> strategies) {
        if (playerCount < 2 || playerCount > 4) {
            throw new LabyrintheException("The number of players must be between 2 and 4");
        }
        this.board = new Board();
        this.players = new Player[playerCount];
        this.currentPlayerIndex = 0;
        this.state = GameState.INSERT;
        this.playerStrategies = strategies;

        initializePlayers(playerCount);
        notifyObservers();

    }

    /**
     * Initializes players with shuffled objectives.
     *
     * @param playerCount number of players
     */
    private void initializePlayers(int playerCount) {
        List<Treasure> shuffledTreasures =
                new ArrayList<>(Arrays.asList(Treasure.values()));
        Collections.shuffle(shuffledTreasures);

        int objectivesPerPlayer = BoardConfig.OBJECTIVES_PER_PLAYER;
        int treasureIndex = 0;

        for (int i = 0; i < playerCount; i++) {

            List<Treasure> playerObjectives = new ArrayList<>();

            for (int j = 0; j < objectivesPerPlayer; j++) {
                playerObjectives.add(shuffledTreasures.get(treasureIndex));
                treasureIndex++;
            }

            PlayerColor color = PlayerColor.values()[i];
            players[i] = new Player(color, playerObjectives);
        }
    }


    /**
     * Sets the current game state.
     *
     * @param newState new state
     */
    private void setState(GameState newState) {
        this.state = newState;
    }

    /**
     * Sets the last insertion point.
     *
     * @param point insertion point
     */
    private void setLastInsertionPoint(InsertPoint point) {
        this.lastInsertionPoint = point;
    }


    /**
     * Inserts a tile at specified point.
     *
     * @param point insertion point
     * @throws LabyrintheException if point is null, state is not INSERT,
     *                             or insertion at opposite of last point
     */
    public void insert(InsertPoint point) {
        if (point == null) {
            throw new LabyrintheException("Insert point cannot be null");
        }
        if (state != GameState.INSERT) {
            throw new LabyrintheException(
                    "Cannot insert in " + state + " state. Current state must be INSERT"
            );
        }

        if (lastInsertionPoint != null && point.equals(lastInsertionPoint.getOpposite())) {
            throw new LabyrintheException("Cannot insert at the opposite of the last insertion point.");
        }


        board.insert(point);
        this.setLastInsertionPoint(point);
        this.setState(GameState.MOVE);


        InsertCommand cmd = new InsertCommand(board, point);
        commandManager.doIt(cmd);

        notifyObservers();
    }

    /**
     * Gets the last insertion point.
     *
     * @return last insertion point
     */
    public InsertPoint getLastInsertPoint() {
        return board.getLastInsertPoint();
    }


    /**
     * Gets a player's current position.
     *
     * @param color player color
     * @return player position
     */
    public Position getPlayerPosition(PlayerColor color) {
        return board.getPlayerPosition(color);
    }


    /**
     * Moves current player to new position.
     *
     * @param newPosition target position
     * @return true if objective was found during move
     * @throws LabyrintheException if position is null, state is not MOVE,
     *                             game is over, or no valid path
     */
    public boolean move(Position newPosition) {
        if (newPosition == null) {
            throw new LabyrintheException("Position cannot be null");
        }
        if (state != GameState.MOVE) {
            throw new LabyrintheException(
                    "Cannot move in " + state + " state. Current state must be MOVE"
            );
        }
        if (isOver()) {
            throw new LabyrintheException("Game is already over");
        }

        Player currentPlayer = getInternalCurrentPlayer();
        Position currentPosition = board.getPlayerPosition(currentPlayer.getColor());

        if (!board.hasPathBetween(currentPosition, newPosition)) {
            throw new LabyrintheException(
                    "No valid path from " + currentPosition + " to " + newPosition
            );
        }

        Treasure currentObjective = currentPlayer.getCurrentObjective();
        Treasure treasureAtPosition = board.getTreasureAt(newPosition);

        boolean foundObjective = currentObjective != null && currentObjective.equals(treasureAtPosition);

        MoveCommand cmd = new MoveCommand(board, currentPlayer, newPosition);
        cmd.setObjectiveFound(foundObjective);

        // Utiliser la commande comme seul moyen de déplacer le joueur
        commandManager.doIt(cmd);

        this.nextPlayer();
        this.setState(GameState.INSERT);

        notifyObservers();

        return cmd.wasObjectiveFound();
    }


    /**
     * Checks if game is over.
     *
     * @return true if there is a winner
     */
    public boolean isOver() {
        return getWinner() != null;
    }

    /**
     * Advances to next player.
     */
    public void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
    }

    /**
     * Gets the winner if any.
     *
     * @return winner info or null if no winner yet
     */
    public PlayerInfo getWinner() {
        for (Player player : players) {
            Position currentPosition = board.getPlayerPosition(player.getColor());
            if (player.hasWon(currentPosition)) {
                return new PlayerInfo(player);
            }
        }
        return null;
    }

    /**
     * Gets current player.
     *
     * @return current player
     */
    private Player getInternalCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    /**
     * Gets current player info view.
     *
     * @return current player info
     */
    public PlayerInfo getCurrentPlayerInfo() {
        return new PlayerInfo(getInternalCurrentPlayer());
    }


    /**
     * Gets current player position.
     *
     * @return current player position
     */
    public Position getCurrentPlayerPosition() {
        return board.getPlayerPosition(getInternalCurrentPlayer().getColor());
    }

    /**
     * Gets all players info.
     *
     * @return list of player info
     */
    public List<PlayerInfo> getPlayersView() {
        List<PlayerInfo> playersView = new ArrayList<>();
        for (Player player : players) {
            playersView.add(new PlayerInfo(player));
        }
        return playersView;
    }


    /**
     * Gets current game state.
     *
     * @return current state
     */
    public GameState getState() {
        return state;
    }


    /**
     * Gets side tile info.
     *
     * @return side tile info
     */
    public TileInfo getSideTileView() {
        return new TileInfo(board.getSideTile());
    }


    /**
     * Gets tile info at position.
     *
     * @param pos position
     * @return tile info
     */
    public TileInfo getTileViewAt(Position pos) {
        return new TileInfo(board.getTileAt(pos));
    }


    /**
     * Gets position of a treasure.
     *
     * @param treasure treasure to find
     * @return treasure position
     */
    public Position getObjectivePosition(Treasure treasure) {
        return board.getTreasurePosition(treasure);
    }


    /**
     * Gets all positions reachable from start.
     *
     * @param startPos starting position
     * @return list of reachable positions
     */
    public List<Position> getReachablePositions(Position startPos) {
        return List.copyOf(board.findReachablePositions(startPos));
    }


    /**
     * Rotates the side tile.
     *
     * @param rotation rotation direction
     * @throws LabyrintheException if rotation is null or state is not INSERT
     */
    public void rotateSideTile(Rotation rotation) {
        if (rotation == null) {
            throw new LabyrintheException("Rotation cannot be null");
        }
        if (state != GameState.INSERT) {
            throw new LabyrintheException(
                    "Cannot rotate side tile in " + state +
                            " state. Rotation only allowed in INSERT phase"
            );
        }
        commandManager.doIt(new RotateSideTileCommand(board, rotation));
        notifyObservers();
    }


    /**
     * Gets current player color.
     *
     * @return current player color
     */
    public PlayerColor getCurrentPlayerColor() {
        return getInternalCurrentPlayer().getColor();
    }


    /**
     * Gets last insertion position.
     *
     * @return last insertion position
     */
    public Position getLastInsertedPosition() {
        return board.getLastInsertionPos();
    }


    private void previousPlayer() {
        currentPlayerIndex = (currentPlayerIndex - 1 + players.length) % players.length;
    }

    /**
     * Undoes the last command.
     */
    public void undo() {
        Command command = commandManager.undo();
        if (command != null) {
            if (command instanceof MoveCommand) {
                state = GameState.MOVE;
                previousPlayer();

            } else if (command instanceof InsertCommand) {
                state = GameState.INSERT;
            } else if (command instanceof RotateSideTileCommand) {
                state = GameState.INSERT;
            }
        }
        notifyObservers();
    }


    /**
     * Redoes the last undone command.
     */
    public void redo() {
        Command command = commandManager.redo();
        if (command != null) {
            if (command instanceof MoveCommand) {

                state = GameState.INSERT;
                nextPlayer();

            } else if (command instanceof InsertCommand) {
                state = GameState.MOVE;

            } else if (command instanceof RotateSideTileCommand) {
                state = GameState.INSERT;
            }
        }
        notifyObservers();
    }

    public Set<Treasure> getFoundTreasures() {
        Set<Treasure> allFoundTreasures = new HashSet<>();
        for (Player player : players) {
            allFoundTreasures.addAll(player.getFoundTreasures());
        }

        return allFoundTreasures;
    }


    /**
     * Gets AI strategy for current player.
     *
     * @return current player's AI strategy
     */
    public AIStrategy getCurrentAIStrategy() {
        return playerStrategies.get(getCurrentPlayerColor());
    }

    /**
     * Registers an observer.
     *
     * @param o observer to register
     */

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    /**
     * Notifies all observers of game state change.
     */
    private void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }


    }


}