package g60991.dev3.labyrinthe.view.viewJFX;

import g60991.dev3.labyrinthe.controller.LabyrintheController;
import g60991.dev3.labyrinthe.model.game.GameFacade;
import g60991.dev3.labyrinthe.model.game.PlayerColor;
import g60991.dev3.labyrinthe.model.strategy.AIStrategy;
import g60991.dev3.labyrinthe.model.strategy.RandomStrategy;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * Game configuration view for player setup.
 * Allows selecting number of players and player types (Human/Robot).
 */
public class GameConfigView extends VBox {

    private final ComboBox<Integer> playerCountBox;
    private final VBox playersConfigBox;
    private final Button startButton;

    /**
     * Creates the game configuration view.
     */
    public GameConfigView() {
        super(15);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: #2c3e50;");

        Label title = new Label("LABYRINTH");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label label = new Label("Number of players:");
        label.setStyle("-fx-text-fill: white;");

        playerCountBox = new ComboBox<>();
        playerCountBox.getItems().addAll(2, 3, 4);
        playerCountBox.setValue(2);

        playersConfigBox = new VBox(10);
        playersConfigBox.setAlignment(Pos.CENTER);

        playerCountBox.setOnAction(e -> updatePlayerConfigs());
        updatePlayerConfigs();

        startButton = new Button("Start Game");
        startButton.setStyle("-fx-font-size: 14px; -fx-padding: 10 30;");

        this.getChildren().addAll(title, label, playerCountBox, playersConfigBox, startButton);
    }

    /**
     * Updates player configuration UI based on selected player count.
     */
    private void updatePlayerConfigs() {
        playersConfigBox.getChildren().clear();

        int count = playerCountBox.getValue();
        PlayerColor[] colors = PlayerColor.values();

        for (int i = 0; i < count; i++) {
            HBox playerBox = createPlayerConfig(colors[i]);
            playersConfigBox.getChildren().add(playerBox);
        }
    }

    /**
     * Creates configuration UI for a single player.
     *
     * @param color player color
     * @return player configuration box
     */
    private HBox createPlayerConfig(PlayerColor color) {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER);

        Label colorLabel = new Label(color.name() + ":");
        colorLabel.setStyle("-fx-text-fill: white; -fx-min-width: 80;");

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Human", "Robot");
        typeBox.setValue("Human");
        typeBox.setUserData(color);

        box.getChildren().addAll(colorLabel, typeBox);
        return box;
    }

    /**
     * Sets up game initialization when start button is clicked.
     * Creates game, controller, and view, then switches to game scene.
     *
     * @param stage primary stage
     */
    public void setupGame(Stage stage) {
        startButton.setOnAction(e -> {
            int playerCount = playerCountBox.getValue();
            Map<PlayerColor, AIStrategy> strategies = collectPlayerStrategies(playerCount);

            GameFacade game = new GameFacade();
            LabyrintheController controller = new LabyrintheController();
            LabyrintheView gameView = new LabyrintheView();

            gameView.setController(controller);
            controller.initialize(game, gameView);

            game.start(playerCount, strategies);

            controller.checkAndPlayAI();


            Scene gameScene = new Scene(gameView, 1000, 900);
            stage.setScene(gameScene);
        });
    }

    /**
     * Collects player strategies from UI configuration.
     *
     * @param playerCount number of players
     * @return map of player colors to AI strategies
     */
    private Map<PlayerColor, AIStrategy> collectPlayerStrategies(int playerCount) {
        Map<PlayerColor, AIStrategy> strategies = new HashMap<>();

        for (int i = 0; i < playerCount; i++) {
            HBox playerBox = (HBox) playersConfigBox.getChildren().get(i);
            ComboBox<String> typeBox = (ComboBox<String>) playerBox.getChildren().get(1);
            PlayerColor color = (PlayerColor) typeBox.getUserData();

            if ("Robot".equals(typeBox.getValue())) {
                strategies.put(color, new RandomStrategy());
            }
        }

        return strategies;
    }

}