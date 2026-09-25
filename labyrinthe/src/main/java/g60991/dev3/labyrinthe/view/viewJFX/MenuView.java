package g60991.dev3.labyrinthe.view.viewJFX;

import g60991.dev3.labyrinthe.controller.LabyrintheController;
import g60991.dev3.labyrinthe.model.board.Rotation;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * Menu bar with game control buttons.
 * Provides undo, redo, rotation, and exit functionality.
 */
public class MenuView extends HBox {

    private LabyrintheController controller;

    /**
     * Creates the menu view with all control buttons.
     */
    public MenuView() {
        this.setSpacing(10);
        this.setStyle("-fx-background-color: #34495e; -fx-padding: 10;");

        Button undoButton = new Button("Undo");
        Button redoButton = new Button("Redo");
        Button rotateLeftButton = new Button("Rotate ⟲");
        Button rotateRightButton = new Button("Rotate ⟳");
        Button exitButton = new Button("Exit");

        undoButton.setOnAction(e -> {
            if (controller.askIsGameOver()) {
                return;
            }
            controller.undo();
        });

        redoButton.setOnAction(e -> {
            if (controller.askIsGameOver()) {
                return;
            }
            controller.redo();
        });

        rotateLeftButton.setOnAction(e -> {
            if (controller.askIsGameOver()) {
                return;
            }
            controller.rotateSideTile(Rotation.COUNTERCLOCKWISE);
        });

        rotateRightButton.setOnAction(e -> {
            if (controller.askIsGameOver()) {
                return;
            }
            controller.rotateSideTile(Rotation.CLOCKWISE);
        });

        exitButton.setOnAction(e -> showExitConfirmation());

        this.getChildren().addAll(undoButton, redoButton, rotateLeftButton, rotateRightButton, exitButton);
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
     * Shows exit confirmation dialog.
     * Exits application if user confirms.
     */
    private void showExitConfirmation() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("The current game will be lost.");

        alert.showAndWait().ifPresent(response -> {
            if (response.getButtonData().isDefaultButton()) {
                System.exit(0);
            }
        });
    }
}