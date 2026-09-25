package g60991.dev3.labyrinthe.view.viewJFX;

import g60991.dev3.labyrinthe.controller.LabyrintheController;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Main JavaFX view containing all game components.
 * Serves as central container for board, menu, info panel, side tile, and messages.
 */
public class LabyrintheView extends BorderPane {

    private final BoardView boardView;
    private final MenuView menuView;
    private final InfoView infoView;
    private final SideTileView sideTileView;
    private final MessageDisplay messageDisplay;

    /**
     * Creates the main game view with all components.
     */
    public LabyrintheView() {
        boardView = new BoardView();
        menuView = new MenuView();
        infoView = new InfoView();
        sideTileView = new SideTileView();
        messageDisplay = new MessageDisplay();

        layoutViews();
    }

    /**
     * Sets controller for all sub-views.
     * Distributes controller reference to each component.
     *
     * @param controller game controller
     */
    public void setController(LabyrintheController controller) {
        boardView.setController(controller);
        menuView.setController(controller);
        infoView.setController(controller);
        sideTileView.setController(controller);
        messageDisplay.setController(controller);
    }

    /**
     * Initializes the layout of all components.
     * Top: menu, Center: side tile and board, Right: info, Bottom: messages.
     */
    private void layoutViews() {
        setTop(menuView);

        VBox centerPanel = new VBox(10);
        centerPanel.setAlignment(Pos.CENTER);
        centerPanel.getChildren().addAll(sideTileView, boardView);
        setCenter(centerPanel);

        setRight(infoView);
        setBottom(messageDisplay);
    }

    /**
     * Updates all sub-views.
     * Called by controller when game state changes.
     */
    public void update() {
        boardView.update();
        infoView.update();
        sideTileView.update();
        messageDisplay.update();
    }

    /**
     * Gets the message display component.
     *
     * @return message display
     */
    public MessageDisplay getMessageDisplay() {
        return messageDisplay;
    }
}