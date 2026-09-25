package g60991.dev3.labyrinthe.view;

import g60991.dev3.labyrinthe.controller.LabyrintheController;
import g60991.dev3.labyrinthe.model.board.*;
import g60991.dev3.labyrinthe.model.game.PlayerColor;
import g60991.dev3.labyrinthe.model.game.PlayerInfo;

import java.util.Scanner;

/**
 * Console view for the Labyrinth game.
 * Handles all console display and user input.
 */
public class ConsoleView {

    private LabyrintheController controller;
    private final Scanner scanner;

    /**
     * Creates a console view.
     */
    public ConsoleView() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Sets the controller.
     *
     * @param controller game controller
     */
    public void setController(LabyrintheController controller) {
        this.controller = controller;
    }

    // --- GAME LIFECYCLE CALLBACKS ---

    /**
     * Called when game starts.
     */
    public void onGameStart() {
        System.out.println("=== LABYRINTH GAME ===");
        System.out.println("Type 'help' to see available commands");
    }

    /**
     * Called when game is initialized with players.
     *
     * @param playerCount number of players
     */
    public void onGameInitialized(int playerCount) {
        System.out.println("Game started with " + playerCount + " player(s)");
    }

    /**
     * Called when game is over.
     *
     * @param winner winning player info
     */
    public void onGameOver(PlayerInfo winner) {
        System.out.println("=== " + winner.color() + " WINS! ===");
    }

    /**
     * Called when exiting game.
     */
    public void onExit() {
        System.out.println("Goodbye!");
    }


    /**
     * Prompts for player count.
     *
     * @return user input
     */
    public String promptPlayerCount() {
        System.out.print("Number of players (2-4): ");
        return scanner.nextLine().trim();
    }

    /**
     * Prompts for player type configuration.
     *
     * @param color player color
     * @return user input (h/r)
     */
    public String promptPlayerType(PlayerColor color) {
        System.out.println("Configuration for player " + color + ":");
        System.out.print("Is player " + color + " Human (H) or Robot (R)? ");
        return scanner.nextLine().trim().toLowerCase();
    }

    /**
     * Prompts for command input.
     *
     * @param currentPlayer current player color
     * @return user input
     */
    public String promptCommand(PlayerColor currentPlayer) {
        System.out.print(currentPlayer + "> ");
        return scanner.nextLine();
    }

    // --- PLAYER CONFIGURATION CALLBACKS ---

    /**
     * Called when player is configured.
     *
     * @param color   player color
     * @param isRobot true if robot, false if human
     */
    public void onPlayerConfigured(PlayerColor color, boolean isRobot) {
        if (isRobot) {
            System.out.println("=> Player " + color + " will be Robot (RandomStrategy).");
        } else {
            System.out.println("=> Player " + color + " will be Human.");
        }
    }

    /**
     * Called on invalid player type input.
     */
    public void onInvalidPlayerType() {
        System.out.println("ERROR: Invalid input. Please answer 'H' or 'R'.");
    }

    /**
     * Called on invalid player count.
     */
    public void onInvalidPlayerCount() {
        System.out.println("ERROR: Please enter a number between 2 and 4");
    }

    /**
     * Called on invalid number format.
     */
    public void onInvalidNumber() {
        System.out.println("ERROR: Please enter a valid number");
    }

    // --- AI TURN CALLBACKS ---

    /**
     * Called when AI turn starts.
     *
     * @param color        AI player color
     * @param strategyName strategy class name
     */
    public void onAITurnStart(PlayerColor color, String strategyName) {
        System.out.println("\n--- AI Turn: " + color + " (" + strategyName + ") ---");
    }

    // --- COMMAND ERROR CALLBACKS ---

    /**
     * Called on unknown command.
     */
    public void onUnknownCommand() {
        System.out.println("ERROR: Unknown command. Type 'help' to see available commands.");
    }

    /**
     * Called on invalid insert format.
     */
    public void onInvalidInsertFormat() {
        System.out.println("ERROR: Invalid format. Usage: insert <row> <col>");
    }

    /**
     * Called on invalid insert position.
     */
    public void onInvalidInsertPosition() {
        System.out.println("ERROR: Invalid position for insertion. Valid positions: rows/columns 1, 3, 5 only");
    }

    /**
     * Called when insert fails.
     *
     * @param e exception
     */
    public void onInsertFailed(Exception e) {
        System.out.println("ERROR: Insertion failed - " + e.getMessage());
    }

    /**
     * Called on invalid rotate format.
     */
    public void onInvalidRotateFormat() {
        System.out.println("ERROR: Invalid format. Usage: rotate <cw|ccw>");
    }

    /**
     * Called when rotate fails.
     *
     * @param e exception
     */
    public void onRotateFailed(Exception e) {
        System.out.println("ERROR: Rotation failed - " + e.getMessage());
    }

    /**
     * Called on invalid move format.
     */
    public void onInvalidMoveFormat() {
        System.out.println("ERROR: Invalid format. Usage: move <row> <col>");
    }

    /**
     * Called when move fails.
     *
     * @param e exception
     */
    public void onMoveFailed(Exception e) {
        System.out.println("ERROR: Move failed - " + e.getMessage());
    }

    /**
     * Called on invalid coordinates.
     */
    public void onInvalidCoordinates() {
        System.out.println("ERROR: Invalid coordinate format. Please enter valid numbers.");
    }

    /**
     * Called on generic error.
     *
     * @param message error message
     */
    public void onError(String message) {
        System.out.println("ERROR: " + message);
    }

    /**
     * Called on unexpected error.
     *
     * @param e exception
     */
    public void onUnexpectedError(Exception e) {
        System.out.println("ERROR: Unexpected error - " + e.getMessage());
    }

    // --- DISPLAY METHODS ---

    /**
     * Displays all game information.
     */
    public void displayAll() {
        displayBoard();
        displayGameState();
    }

    /**
     * Displays the game board.
     */
    public void displayBoard() {
        System.out.println("=== BOARD ===");
        printColumnHeaders();
        printBorder();

        for (int row = 0; row < BoardConfig.BOARD_SIZE; row++) {
            printRow(row);
            printBorder();
        }

        System.out.println();
        displaySideTile();
        displayPlayerPositions();
    }

    /**
     * Displays the current objective.
     */
    public void displayCurrentObjective() {
        PlayerInfo player = controller.askCurrentPlayer();
        Treasure objective = player.currentObjective();
        Position currentPos = controller.askCurrentPlayerPosition();

        System.out.println("--- TURN OF " + player.color() + " ---");
        System.out.println("Current position: (" + currentPos.row() + ", " + currentPos.col() + ")");

        if (objective != null) {
            Position objectivePos = controller.askObjectivePosition(objective);
            System.out.println("Treasure to reach: " + objective +
                    " at (" + objectivePos.row() + ", " + objectivePos.col() + ")");
        } else {
            System.out.println("Objective: Return to starting position");
        }
    }

    /**
     * Displays objective found message.
     */
    public void displayObjectiveFound() {
        PlayerInfo player = controller.askCurrentPlayer();
        String color = getColorCode(player.color());
        System.out.println(color + "Objective found! " + player.remainingObjectives() + " remaining" + BoardColor.RESET);
    }

    /**
     * Displays help message.
     */
    public void showHelp() {
        System.out.println("""
                
                === AVAILABLE COMMANDS ===
                insert <row> <col>   Insert tile at position
                rotate <cw|ccw>      Rotate side tile (cw=clockwise, ccw=counterclockwise)
                move <row> <col>     Move your player
                undo                 Undo last action
                redo                 Redo undone action
                show                 Display board
                help                 Display this help
                quit                 Quit game
                """);
    }


    private void printRow(int row) {
        printRowTop(row);
        printRowMiddle(row);
        printRowBottom(row);
    }

    private void printRowTop(int row) {
        System.out.print("   |");
        for (int col = 0; col < BoardConfig.BOARD_SIZE; col++) {
            TileInfo tile = controller.askTileAt(new Position(row, col));
            boolean hasNorth = tile.exits().contains(Orientation.NORTH);
            System.out.print(hasNorth ? "  |  |" : "     |");
        }
        System.out.println();
    }

    private void printRowMiddle(int row) {
        System.out.printf(" %d |", row);
        for (int col = 0; col < BoardConfig.BOARD_SIZE; col++) {
            Position pos = new Position(row, col);
            TileInfo tile = controller.askTileAt(pos);

            String left = tile.exits().contains(Orientation.WEST) ? "-" : " ";
            String right = tile.exits().contains(Orientation.EAST) ? "-" : " ";
            String center = getCenterContent(pos, tile);

            System.out.print(left + " " + center + " " + right + "|");
        }
        System.out.println();
    }

    private void printRowBottom(int row) {
        System.out.print("   |");
        for (int col = 0; col < BoardConfig.BOARD_SIZE; col++) {
            TileInfo tile = controller.askTileAt(new Position(row, col));
            boolean hasSouth = tile.exits().contains(Orientation.SOUTH);
            System.out.print(hasSouth ? "  |  |" : "     |");
        }
        System.out.println();
    }

    private String getCenterContent(Position pos, TileInfo tile) {
        String player = getPlayerAt(pos);
        if (player != null) return player;

        String treasure = getTreasureDisplay(pos, tile);
        if (treasure != null) return treasure;

        return getTileDisplay(pos, tile);
    }

    private String getPlayerAt(Position pos) {
        StringBuilder players = new StringBuilder();
        PlayerColor firstColor = null;

        for (PlayerInfo player : controller.askPlayers()) {
            if (pos.equals(controller.askPlayerPosition(player.color()))) {
                if (firstColor == null) firstColor = player.color();
                players.append(player.color().name().charAt(0));
            }
        }

        if (!players.isEmpty()) {
            String colorCode = players.length() == 1 && firstColor != null ?
                    getColorCode(firstColor) : BoardColor.RESET;
            return colorCode + players + BoardColor.RESET;
        }

        return null;
    }

    private String getTreasureDisplay(Position pos, TileInfo tile) {
        PlayerInfo current = controller.askCurrentPlayer();
        Treasure objective = current.currentObjective();

        if (objective != null && objective.equals(tile.treasure())) {
            Position treasurePos = controller.askObjectivePosition(objective);
            if (pos.equals(treasurePos)) {
                return getColorCode(current.color()) + "*" + BoardColor.RESET;
            }
        }
        return null;
    }

    private String getTileDisplay(Position pos, TileInfo tile) {
        String color = getTileColor(pos, tile);
        String type = tile.shape().name().substring(0, 1);
        return color + type + BoardColor.RESET;
    }

    /**
     * Gets color for tile display based on position and state.
     *
     * @param pos  tile position
     * @param tile tile information
     * @return ANSI color code
     */
    private String getTileColor(Position pos, TileInfo tile) {
        Position lastInserted = controller.askLastInsertedPosition();
        if (pos.equals(lastInserted)) {
            return BoardColor.INSERTED;
        }
        return isFixedPosition(pos) ? BoardColor.FIXED_TILE : BoardColor.MOBILE_TILE;
    }

    /**
     * Checks if position contains a fixed tile.
     *
     * @param pos position to check
     * @return true if tile at position is fixed
     */
    private boolean isFixedPosition(Position pos) {
        for (BoardConfig.FixedTileInfo info : BoardConfig.FIXED_L_CORNERS) {
            if (pos.equals(new Position(info.row(), info.col()))) {
                return true;
            }
        }

        for (BoardConfig.FixedTileInfo info : BoardConfig.FIXED_T_WITH_TREASURE) {
            if (pos.equals(new Position(info.row(), info.col()))) {
                return true;
            }
        }

        return false;
    }

    private String getColorCode(PlayerColor color) {
        return switch (color) {
            case RED -> BoardColor.RED;
            case BLUE -> BoardColor.BLUE;
            case GREEN -> BoardColor.GREEN;
            case YELLOW -> BoardColor.YELLOW;
        };
    }

    private void printColumnHeaders() {
        System.out.print("      ");
        for (int col = 0; col < BoardConfig.BOARD_SIZE; col++) {
            System.out.printf("%d     ", col);
        }
        System.out.println();
    }

    private void printBorder() {
        System.out.print("   +");
        for (int i = 0; i < BoardConfig.BOARD_SIZE; i++) {
            System.out.print("-----+");
        }
        System.out.println();
    }

    private void displaySideTile() {
        TileInfo tile = controller.askSideTile();
        System.out.println("\n--- Available Tile ---");
        printSideTileVisual(tile);
        printSideTileInfo(tile);
    }

    private void printSideTileVisual(TileInfo tile) {
        String north = tile.exits().contains(Orientation.NORTH) ? " | " : "   ";
        String west = tile.exits().contains(Orientation.WEST) ? "-" : " ";
        String east = tile.exits().contains(Orientation.EAST) ? "-" : " ";
        String south = tile.exits().contains(Orientation.SOUTH) ? " | " : "   ";

        String center = tile.shape().name().substring(0, 1);
        if (tile.treasure() != null) center += "*";

        System.out.println("   " + north);
        System.out.println("  " + west + center + east);
        System.out.println("   " + south);
    }

    private void printSideTileInfo(TileInfo tile) {
        System.out.print("Direction: " + getDirectionSymbol(tile.orientation()));
        if (tile.treasure() != null) {
            System.out.print("  |  Treasure: " + tile.treasure());
        }
        System.out.println();
    }

    private String getDirectionSymbol(Orientation orientation) {
        return switch (orientation) {
            case NORTH -> "^";
            case SOUTH -> "v";
            case EAST -> ">";
            case WEST -> "<";
        };
    }

    private void displayPlayerPositions() {
        System.out.println("Players:");
        for (PlayerInfo player : controller.askPlayers()) {
            Position pos = controller.askPlayerPosition(player.color());
            String objective = player.currentObjective() == null ?
                    "Return to start" : player.currentObjective().toString();

            System.out.printf("   %s (%d,%d) - objective: %s (%d remaining)\n",
                    player.color(), pos.row(), pos.col(),
                    objective, player.remainingObjectives());
        }
        System.out.println();
    }

    private void displayGameState() {
        PlayerInfo current = controller.askCurrentPlayer();
        System.out.println("\n--- TURN OF "
                + getColorCode(current.color()) + current.color()
                + BoardColor.RESET + " ---");

        System.out.println("Phase: " + controller.askGameState());
    }


    /**
     * Called when AI is thinking.
     *
     * @param color AI player color
     */
    public void onAIThinking(PlayerColor color) {
        System.out.println("AI " + color + " is choosing its move...");
    }

    /**
     * Called when AI encounters an error.
     *
     * @param e exception
     */
    public void onAIError(Exception e) {
        System.out.println("ERROR: AI turn failed - " + e.getMessage());
    }
}