package g60991.dev3.labyrinthe.view.viewJFX;

import g60991.dev3.labyrinthe.controller.LabyrintheController;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;


public class MessageDisplay extends Label {

    private LabyrintheController controller;

    public MessageDisplay() {
        super("Welcome to Labyrinth!");

        this.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-background-color: #2c3e50;");
        this.setTextFill(Color.WHITE);
    }

    public void setController(LabyrintheController controller) {
        this.controller = controller;
    }


    public void update() {

        String phase = controller.askGameState().name();
        String currentPlayer = controller.askCurrentPlayer().color().name();
        updateMessage("Phase: " + phase + " | Current Player: " + currentPlayer, Color.WHITE);
    }

    private void updateMessage(String message, Color color) {
        setText(message);
        setTextFill(color);

    }

    /**
     * Displays info message.
     *
     * @param message info message
     */

    public void showError(String message) {
        updateMessage("ERROR: " + message, Color.RED);
    }

    public void showSuccess(String message) {
        updateMessage("SUCCESS: " + message, Color.LIGHTGREEN);
    }

    public void showInfo(String message) {
        updateMessage(message, Color.WHITE);
    }
}