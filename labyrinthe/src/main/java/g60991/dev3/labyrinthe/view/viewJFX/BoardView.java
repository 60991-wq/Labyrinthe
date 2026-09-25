package g60991.dev3.labyrinthe.view.viewJFX;

import g60991.dev3.labyrinthe.controller.LabyrintheController;
import g60991.dev3.labyrinthe.model.board.InsertPoint;
import g60991.dev3.labyrinthe.model.board.Position;
import g60991.dev3.labyrinthe.model.board.TileInfo;
import g60991.dev3.labyrinthe.model.game.GameState;
import g60991.dev3.labyrinthe.model.game.PlayerColor;
import g60991.dev3.labyrinthe.model.game.PlayerInfo;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * JavaFX view for the game board.
 * Displays tiles, players, and insertion buttons.
 */
public class BoardView extends GridPane {

    private LabyrintheController controller;

    /**
     * Creates the board view.
     */
    public BoardView() {
        super();
        this.setHgap(5);
        this.setVgap(5);
        this.setAlignment(Pos.CENTER);
    }

    /**
     * Sets the controller.
     *
     * @param controller game controller
     */
    public void setController(LabyrintheController controller) {
        this.controller = controller;
    }

    /**
     * Updates board display.
     * Called by controller when game state changes.
     */
    public void update() {
        if (controller == null) return;

        getChildren().clear();

        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                Position pos = new Position(row, col);
                add(createCell(pos), col + 1, row + 1);
            }
        }

        addInsertButtons();
    }

    /**
     * Adds insertion buttons around the board edges.
     */
    private void addInsertButtons() {
        for (InsertPoint point : InsertPoint.ALL_POINTS) {
            Button button = createInsertButton(point);

            int row = point.position().row();
            int col = point.position().col();

            if (row == 0) {
                this.add(button, col + 1, 0);
            } else if (row == 6) {
                this.add(button, col + 1, 8);
            } else if (col == 0) {
                this.add(button, 0, row + 1);
            } else if (col == 6) {
                this.add(button, 8, row + 1);
            }
        }
    }

    /**
     * Creates insertion button for specific point.
     *
     * @param point insertion point
     * @return configured button
     */
    private Button createInsertButton(InsertPoint point) {
        ImageView arrowIcon = ImageManager.createArrowIcon(point.direction());

        Button button = new Button();
        button.setGraphic(arrowIcon);
        button.setPrefSize(30, 30);
        button.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");

        button.setOnAction(e -> {
            if (controller.askIsGameOver()) {
                return;
            }

            try {
                controller.insert(point);
            } catch (Exception ex) {
                System.out.println("Erreur d'insertion inattendue : " + ex.getMessage());
            }
        });

        button.setOnMouseEntered(e -> {
            if (!controller.askIsGameOver() && controller.askGameState() == GameState.INSERT) {
                button.setStyle("-fx-background-color: lightgreen; -fx-border-radius: 5;");
            }
        });

        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        });

        return button;
    }

    /**
     * Creates cell display for board position.
     *
     * @param pos board position
     * @return configured cell
     */
    private StackPane createCell(Position pos) {
        StackPane cell = new StackPane();

        TileInfo tile = controller.askTileAt(pos);
        if (tile != null) {
            TileView tileView = new TileView(
                    tile.shape(),
                    tile.orientation(),
                    tile.treasure(),
                    controller.askAllFoundTreasures()
            );
            cell.getChildren().add(tileView);
        }

        addPlayerPawns(cell, pos);
        markLastInsertion(cell, pos);
        cell.setOnMouseClicked(e -> handleCellClick(pos));

        cell.setOnMouseEntered(e -> {
            if (canMove(pos) && tile != null && !cell.getChildren().isEmpty()) {
                if (cell.getChildren().get(0) instanceof TileView tileView) {
                    tileView.setHoverEffect(true);
                }
            }
        });

        cell.setOnMouseExited(e -> {
            if (tile != null && !cell.getChildren().isEmpty()) {
                if (cell.getChildren().get(0) instanceof TileView tileView) {
                    tileView.setHoverEffect(false);
                }
            }
        });

        return cell;
    }

    /**
     * Adds player pawns to cell if present.
     *
     * @param cell cell to add pawns to
     * @param pos  cell position
     */
    private void addPlayerPawns(StackPane cell, Position pos) {
        for (PlayerInfo player : controller.askPlayers()) {
            Position playerPos = controller.askPlayerPosition(player.color());

            if (playerPos != null && playerPos.equals(pos)) {
                Circle pawn = new Circle(8);
                pawn.setFill(getColor(player.color()));
                pawn.setStroke(Color.BLACK);
                pawn.setStrokeWidth(1);

                cell.getChildren().add(pawn);
            }
        }
    }

    /**
     * Handles cell click for player movement.
     *
     * @param pos clicked position
     */
    private void handleCellClick(Position pos) {
        if (controller.askIsGameOver()) {
            return;
        }

        try {
            if (controller.askGameState() == GameState.MOVE) {
                controller.move(pos);
            }
        } catch (Exception e) {
            System.out.println("ERREUR DANS LE CLIC (MOUVEMENT) : " + e.getMessage());
        }
    }

    /**
     * Checks if position is reachable for current player.
     *
     * @param pos position to check
     * @return true if reachable
     */
    private boolean canMove(Position pos) {
        if (controller.askIsGameOver()) {
            return false;
        }

        if (controller.askGameState() == GameState.MOVE) {
            Position currentPos = controller.askCurrentPlayerPosition();
            return controller.askReachablePositions(currentPos).contains(pos);
        }
        return false;
    }

    /**
     * Converts player color to JavaFX color.
     *
     * @param color player color
     * @return JavaFX color
     */
    private Color getColor(PlayerColor color) {
        return switch (color) {
            case RED -> Color.RED;
            case BLUE -> Color.BLUE;
            case GREEN -> Color.GREEN;
            case YELLOW -> Color.YELLOW;
        };
    }

    /**
     * Marks last insertion position with orange indicator.
     *
     * @param cell cell to mark
     * @param pos  cell position
     */
    private void markLastInsertion(StackPane cell, Position pos) {
        Position lastInserted = controller.askLastInsertedPosition();

        if (pos.equals(lastInserted)) {
            Circle marker = new Circle(5);
            marker.setFill(Color.ORANGE);
            marker.setStroke(Color.DARKORANGE);
            marker.setStrokeWidth(1);
            marker.setTranslateX(20);
            marker.setTranslateY(-20);
            marker.setMouseTransparent(true);

            cell.getChildren().add(marker);
        }
    }
}