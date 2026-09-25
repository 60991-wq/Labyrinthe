package g60991.dev3.labyrinthe;

import g60991.dev3.labyrinthe.view.viewJFX.GameConfigView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class for the Labyrinth game.
 * Launches JavaFX interface with game configuration.
 */
public class LabyrintheApp extends Application {

    /**
     * Starts the JavaFX application.
     * Displays game configuration screen.
     *
     * @param primaryStage primary application stage
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Labyrinth");

        GameConfigView configView = new GameConfigView();
        configView.setupGame(primaryStage);

        Scene scene = new Scene(configView, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Main entry point for the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}