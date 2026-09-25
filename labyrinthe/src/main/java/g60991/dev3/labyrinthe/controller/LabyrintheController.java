package g60991.dev3.labyrinthe.controller;

import g60991.dev3.labyrinthe.model.board.Treasure;
import g60991.dev3.labyrinthe.model.board.InsertPoint;
import g60991.dev3.labyrinthe.model.board.Position;
import g60991.dev3.labyrinthe.model.board.Rotation;
import g60991.dev3.labyrinthe.model.board.TileInfo;
import g60991.dev3.labyrinthe.model.game.GameFacade;
import g60991.dev3.labyrinthe.model.game.GameState;
import g60991.dev3.labyrinthe.model.game.PlayerColor;
import g60991.dev3.labyrinthe.model.game.PlayerInfo;
import g60991.dev3.labyrinthe.model.strategy.AIStrategy;
import g60991.dev3.labyrinthe.model.strategy.InsertionChoice;
import g60991.dev3.labyrinthe.model.strategy.RandomStrategy;
import g60991.dev3.labyrinthe.model.util.LabyrintheException;
import g60991.dev3.labyrinthe.model.util.Observer;
import g60991.dev3.labyrinthe.view.ConsoleView;
import g60991.dev3.labyrinthe.view.viewJFX.LabyrintheView;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main controller for the Labyrinth game.
 * Handles both console and JavaFX interfaces.
 * Implements Observer pattern to update views when model changes.
 */
public class LabyrintheController implements Observer {

    private GameFacade game;
    private final ConsoleView consoleView;
    private final Scanner scanner;
    private LabyrintheView jfxView;

    /**
     * Creates controller for console mode.
     *
     * @param view console view
     */
    public LabyrintheController(ConsoleView view) {
        this.consoleView = view;
        this.scanner = new Scanner(System.in);
        this.consoleView.setController(this);
    }

    /**
     * Creates controller for JavaFX mode.
     */
    public LabyrintheController() {
        this.consoleView = null;
        this.scanner = null;
    }

    /**
     * Initializes controller with game and JavaFX view.
     *
     * @param game game facade
     * @param jfxView main JavaFX view
     */
    public void initialize(GameFacade game, LabyrintheView jfxView) {
        this.game = game;
        this.jfxView = jfxView;
        game.registerObserver(this);
    }

    /**
     * Sets the game facade.
     *
     * @param game game facade
     */
    public void setGame(GameFacade game) {
        this.game = game;
    }

    /**
     * Starts the console game loop.
     */
    public void start() {
        consoleView.onGameStart();

        int playerCount = askPlayerCount();
        Map<PlayerColor, AIStrategy> playerStrategies = configurePlayerStrategies(playerCount);

        this.game = new GameFacade();
        consoleView.onGameInitialized(playerCount);
        game.registerObserver(this);

        game.start(playerCount, playerStrategies);

        runGameLoop();

        if (game.isOver()) {
            consoleView.onGameOver(game.getWinner());
        }
        consoleView.onExit();
    }

    /**
     * Configures AI strategies for each player.
     *
     * @param playerCount number of players
     * @return map of player colors to AI strategies
     */
    private Map<PlayerColor, AIStrategy> configurePlayerStrategies(int playerCount) {
        PlayerColor[] colors = PlayerColor.values();
        Map<PlayerColor, AIStrategy> strategies = new HashMap<>();

        for (int i = 0; i < playerCount; i++) {
            PlayerColor color = colors[i];
            AIStrategy strategy = askPlayerType(color);
            if (strategy != null) {
                strategies.put(color, strategy);
            }
        }
        return strategies;
    }

    /**
     * Main game loop for console mode.
     */
    private void runGameLoop() {
        while (!game.isOver()) {
            try {
                AIStrategy currentStrategy = game.getCurrentAIStrategy();

                if (currentStrategy != null) {
                    handleAITurn(currentStrategy);
                } else {
                    consoleView.displayCurrentObjective();
                    if (!handleHumanTurn()) {
                        break;
                    }
                }
            } catch (LabyrintheException e) {
                consoleView.onError(e.getMessage());
            } catch (Exception e) {
                consoleView.onUnexpectedError(e);
            }
        }
    }

    /**
     * Handles AI turn in console mode.
     *
     * @param strategy AI strategy to use
     */
    private void handleAITurn(AIStrategy strategy) {
        consoleView.onAITurnStart(game.getCurrentPlayerColor(), strategy.getClass().getSimpleName());

        AIController aiController = new AIController(game, consoleView, strategy);
        boolean objectiveFound = aiController.playAITurn();

        if (objectiveFound) {
            consoleView.displayObjectiveFound();
        }
    }

    /**
     * Handles human player turn.
     *
     * @return false if player wants to quit
     */
    private boolean handleHumanTurn() {
        String input = consoleView.promptCommand(game.getCurrentPlayerColor());
        String command = normalizeInput(input);

        if (command.equals("quit") || command.equals("exit")) {
            return false;
        }

        if (!command.isEmpty()) {
            handleCommand(command);
        }
        return true;
    }

    /**
     * Normalizes user input by converting to lowercase and removing extra spaces.
     *
     * @param input raw user input
     * @return normalized input
     */
    private String normalizeInput(String input) {
        if (input == null) return "";
        return input.toLowerCase().replaceAll("\\s+", " ").trim();
    }

    /**
     * Routes command to appropriate handler.
     *
     * @param input normalized command string
     */
    private void handleCommand(String input) {
         if (input.startsWith("insert ")) {
            handleInsert(input);
        } else if (input.startsWith("rotate ")) {
            handleRotate(input);
        } else if (input.startsWith("move ")) {
            handleMove(input);
        } else if (input.equals("undo")) {
            game.undo();
        } else if (input.equals("redo")) {
            game.redo();
        } else if (input.equals("help")) {
            consoleView.showHelp();
        } else {
            consoleView.onUnknownCommand();
        }
    }

    /**
     * Handles insert command from console.
     *
     * @param input command string
     */
    private void handleInsert(String input) {
        Pattern pattern = Pattern.compile("^insert (\\d+) (\\d+)$");
        Matcher matcher = pattern.matcher(input);

        if (!matcher.matches()) {
            consoleView.onInvalidInsertFormat();
            return;
        }

        try {
            int row = Integer.parseInt(matcher.group(1));
            int col = Integer.parseInt(matcher.group(2));
            Position pos = new Position(row, col);
            InsertPoint point = InsertPoint.findByPosition(pos);

            if (point == null) {
                consoleView.onInvalidInsertPosition();
                return;
            }

            game.insert(point);
        } catch (LabyrintheException e) {
            consoleView.onInsertFailed(e);
        } catch (NumberFormatException e) {
            consoleView.onInvalidCoordinates();
        }
    }

    /**
     * Handles rotate command from console.
     *
     * @param input command string
     */
    private void handleRotate(String input) {
        Pattern pattern = Pattern.compile("^rotate (cw|ccw)$");
        Matcher matcher = pattern.matcher(input);

        if (!matcher.matches()) {
            consoleView.onInvalidRotateFormat();
            return;
        }

        try {
            String direction = matcher.group(1);
            Rotation rotation = direction.equals("cw") ? Rotation.CLOCKWISE : Rotation.COUNTERCLOCKWISE;
            game.rotateSideTile(rotation);
        } catch (LabyrintheException e) {
            consoleView.onRotateFailed(e);
        }
    }

    /**
     * Handles move command from console.
     *
     * @param input command string
     */
    private void handleMove(String input) {
        Pattern pattern = Pattern.compile("^move (\\d+) (\\d+)$");
        Matcher matcher = pattern.matcher(input);

        if (!matcher.matches()) {
            consoleView.onInvalidMoveFormat();
            return;
        }

        try {
            int row = Integer.parseInt(matcher.group(1));
            int col = Integer.parseInt(matcher.group(2));
            Position target = new Position(row, col);

            boolean objectiveFound = game.move(target);

            if (objectiveFound) {
                consoleView.displayObjectiveFound();
            }
        } catch (LabyrintheException e) {
            consoleView.onMoveFailed(e);
        } catch (NumberFormatException e) {
            consoleView.onInvalidCoordinates();
        }
    }

    /**
     * Asks player type configuration.
     *
     * @param color player color
     * @return AI strategy if robot, null if human
     */
    private AIStrategy askPlayerType(PlayerColor color) {
        while (true) {
            String input = consoleView.promptPlayerType(color);

            if (input.equals("h")) {
                consoleView.onPlayerConfigured(color, false);
                return null;
            } else if (input.equals("r")) {
                consoleView.onPlayerConfigured(color, true);
                return new RandomStrategy();
            } else {
                consoleView.onInvalidPlayerType();
            }
        }
    }

    /**
     * Asks and validates player count.
     *
     * @return player count (2-4)
     */
    private int askPlayerCount() {
        while (true) {
            String input = consoleView.promptPlayerCount();

            try {
                int count = Integer.parseInt(input);
                if (count >= 2 && count <= 4) {
                    return count;
                }
                consoleView.onInvalidPlayerCount();
            } catch (NumberFormatException e) {
                consoleView.onInvalidNumber();
            }
        }
    }

    /**
     * Gets tile info at position.
     *
     * @param pos position
     * @return tile info
     */
    public TileInfo askTileAt(Position pos) {
        return game.getTileViewAt(pos);
    }

    /**
     * Gets all players info.
     *
     * @return list of player info
     */
    public List<PlayerInfo> askPlayers() {
        return game.getPlayersView();
    }

    /**
     * Gets player position.
     *
     * @param color player color
     * @return player position
     */
    public Position askPlayerPosition(PlayerColor color) {
        return game.getPlayerPosition(color);
    }

    /**
     * Gets side tile info.
     *
     * @return side tile info
     */
    public TileInfo askSideTile() {
        return game.getSideTileView();
    }

    /**
     * Gets current game state.
     *
     * @return current state
     */
    public GameState askGameState() {
        return game.getState();
    }

    /**
     * Gets current player info.
     *
     * @return current player info
     */
    public PlayerInfo askCurrentPlayer() {
        return game.getCurrentPlayerInfo();
    }

    /**
     * Gets current player position.
     *
     * @return current player position
     */
    public Position askCurrentPlayerPosition() {
        return game.getCurrentPlayerPosition();
    }

    /**
     * Gets treasure position.
     *
     * @param treasure treasure to find
     * @return treasure position
     */
    public Position askObjectivePosition(Treasure treasure) {
        return game.getObjectivePosition(treasure);
    }

    /**
     * Gets last insertion position.
     *
     * @return last insertion position
     */
    public Position askLastInsertedPosition() {
        return game.getLastInsertedPosition();
    }

    /**
     * Gets all found treasures.
     *
     * @return set of found treasures
     */
    public Set<Treasure> askAllFoundTreasures() {
        return game.getFoundTreasures();
    }

    /**
     * Gets reachable positions from start.
     *
     * @param from starting position
     * @return list of reachable positions
     */
    public List<Position> askReachablePositions(Position from) {
        return game.getReachablePositions(from);
    }

    /**
     * Checks if game is over.
     *
     * @return true if game is over
     */
    public boolean askIsGameOver() {
        return game.isOver();
    }

    /**
     * Gets winner if any.
     *
     * @return winner info or null
     */
    public PlayerInfo askWinner() {
        return game.getWinner();
    }

    /**
     * Inserts tile at specified point.
     *
     * @param point insertion point
     */
    public void insert(InsertPoint point) {
        try {
            game.insert(point);
        } catch (LabyrintheException e) {
            if (jfxView != null) {
                jfxView.getMessageDisplay().showError("Insertion failed: " + e.getMessage());
            }
        }
    }

    /**
     * Rotates the side tile.
     *
     * @param rotation rotation direction
     */
    public void rotateSideTile(Rotation rotation) {
        try {
            game.rotateSideTile(rotation);
        } catch (LabyrintheException e) {
            if (jfxView != null) {
                jfxView.getMessageDisplay().showError("Rotation failed: " + e.getMessage());
            }
        }
    }

    /**
     * Moves current player to target position.
     *
     * @param target target position
     * @return true if objective was found
     */
    public boolean move(Position target) {
        try {
            boolean objectiveFound = game.move(target);

            checkAndPlayAI();

            return objectiveFound;
        } catch (LabyrintheException e) {
            if (jfxView != null) {
                jfxView.getMessageDisplay().showError("Move failed: " + e.getMessage());
            }
            return false;
        }
    }

    /**
     * Undoes last command.
     */
    public void undo() {
        try {
            game.undo();
        } catch (LabyrintheException e) {
            if (jfxView != null) {
                jfxView.getMessageDisplay().showError("Undo failed: " + e.getMessage());
            }
        }
    }

    /**
     * Redoes last undone command.
     */
    public void redo() {
        try {
            game.redo();
        } catch (LabyrintheException e) {
            if (jfxView != null) {
                jfxView.getMessageDisplay().showError("Redo failed: " + e.getMessage());
            }
        }
    }

    /**
     * Checks and plays AI turn if current player is AI.
     * Adds delay in JavaFX mode for visual feedback.
     */
    public void checkAndPlayAI() {
        if (game.isOver()) {
            return;
        }
        AIStrategy currentStrategy = game.getCurrentAIStrategy();

        if (currentStrategy != null) {
            if (jfxView != null) {
                jfxView.getMessageDisplay().showInfo("AI " + game.getCurrentPlayerColor() + " is thinking...");

                PauseTransition pause = new PauseTransition(Duration.millis(1000));
                pause.setOnFinished(e -> executeAITurn());
                pause.play();
            } else {
                executeAITurn();
            }
        }
    }

    /**
     * Executes AI turn step by step with visual delays.
     * Steps: rotate side tile, insert tile, move player.
     */
    private void executeAITurn() {
        if (game.isOver()) {
            return;
        }
        AIStrategy strategy = game.getCurrentAIStrategy();
        if (strategy == null) return;

        InsertionChoice choice = strategy.chooseInsertion(game);

        if (jfxView != null) {
            jfxView.getMessageDisplay().showInfo("AI rotating side tile...");
        }

        game.rotateSideTile(choice.rotation());

        PauseTransition pauseAfterRotate = new PauseTransition(Duration.millis(800));
        pauseAfterRotate.setOnFinished(e1 -> {

            if (jfxView != null) {
                jfxView.getMessageDisplay().showInfo("AI inserting tile...");
            }

            game.insert(choice.point());

            PauseTransition pauseAfterInsert = new PauseTransition(Duration.millis(800));
            pauseAfterInsert.setOnFinished(e2 -> {

                Position targetPos = strategy.chooseMove(game);

                if (jfxView != null) {
                    jfxView.getMessageDisplay().showInfo("AI moving player...");
                }

                try {
                    boolean objectiveFound = game.move(targetPos);

                    if (objectiveFound && jfxView != null) {
                        jfxView.getMessageDisplay().showSuccess("AI found objective!");
                    }
                } catch (Exception ex) {
                    if (jfxView != null) {
                        jfxView.getMessageDisplay().showError("AI move failed: " + ex.getMessage());
                    }
                }

                PauseTransition pauseBeforeNextAI = new PauseTransition(Duration.millis(500));
                pauseBeforeNextAI.setOnFinished(e3 -> checkAndPlayAI());
                pauseBeforeNextAI.play();
            });
            pauseAfterInsert.play();
        });
        pauseAfterRotate.play();
    }

    /**
     * Updates all registered views when game state changes.
     */
    @Override
    public void update() {
        if (game == null) return;

        if (jfxView != null) {
            jfxView.update();
        }

        if (consoleView != null) {
            consoleView.displayAll();
        }

        if (game.isOver()) {
            displayWinner();
        }
    }

    /**
     * Displays winner message with delay for visual feedback.
     */
    private void displayWinner() {
        PlayerInfo winner = game.getWinner();
        if (winner == null) return;

        if (jfxView != null) {
            jfxView.getMessageDisplay().showSuccess("🏆 " + winner.color().name() + " won the game!");
        } else if (consoleView != null) {
            consoleView.onGameOver(winner);
        }
    }
}