package g60991.dev3.labyrinthe.view.viewJFX;

import g60991.dev3.labyrinthe.controller.LabyrintheController;
import g60991.dev3.labyrinthe.model.board.Treasure;
import g60991.dev3.labyrinthe.model.game.PlayerColor;
import g60991.dev3.labyrinthe.model.game.PlayerInfo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Information panel displaying current player and game status.
 * Shows current player turn, objective, and all players' progress.
 */
public class InfoView extends VBox {

    private LabyrintheController controller;
    private final Text currentPlayerText;
    private final Label objectiveText;
    private final VBox playersInfoBox;

    /**
     * Creates the info view panel.
     */
    public InfoView() {
        super(15);
        this.setPadding(new Insets(15));
        this.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; " +
                "-fx-border-radius: 10; -fx-background-radius: 10;");
        this.setAlignment(Pos.TOP_CENTER);

        Text title = new Text("Game Info");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setFill(Color.web("#2c3e50"));

        currentPlayerText = new Text();
        currentPlayerText.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        objectiveText = new Label();
        objectiveText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        objectiveText.setAlignment(Pos.CENTER);

        playersInfoBox = new VBox(10);
        playersInfoBox.setAlignment(Pos.CENTER_LEFT);

        this.getChildren().addAll(title, currentPlayerText, objectiveText, playersInfoBox);
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
     * Updates information display.
     * Shows current player, their objective, and all players' progress.
     */
    public void update() {
        PlayerInfo current = controller.askCurrentPlayer();

        currentPlayerText.setText("Current: " + current.color());
        currentPlayerText.setFill(getColorForPlayer(current.color()));

        Treasure objective = current.currentObjective();

        objectiveText.setText(null);
        objectiveText.setGraphic(null);

        if (objective != null) {
            objectiveText.setText(null);
            objectiveText.setGraphic(ImageManager.createIcon(objective.name()));
        } else {
            objectiveText.setText("Target: Return to start!");
            objectiveText.setTextFill(Color.web("#27ae60"));
        }

        playersInfoBox.getChildren().clear();
        for (PlayerInfo player : controller.askPlayers()) {
            HBox playerBox = createPlayerInfoBox(player);
            playersInfoBox.getChildren().add(playerBox);
        }
    }

    /**
     * Creates info box for a single player.
     *
     * @param player player information
     * @return configured player info box
     */
    private HBox createPlayerInfoBox(PlayerInfo player) {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);

        Text colorText = new Text(player.color().name() + ": ");
        colorText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        colorText.setFill(getColorForPlayer(player.color()));

        Text objectivesText = new Text(
                player.foundObjectivesCount() + "/" +
                        (player.foundObjectivesCount() + player.remainingObjectives()) + " objectives"
        );
        objectivesText.setFont(Font.font("Arial", 12));
        objectivesText.setFill(Color.web("#34495e"));

        box.getChildren().addAll(colorText, objectivesText);
        return box;
    }

    /**
     * Converts player color to JavaFX color.
     *
     * @param color player color
     * @return JavaFX color
     */
    private Color getColorForPlayer(PlayerColor color) {
        return switch (color) {
            case RED -> Color.RED;
            case BLUE -> Color.BLUE;
            case GREEN -> Color.GREEN;
            case YELLOW -> Color.GOLD;
        };
    }
}