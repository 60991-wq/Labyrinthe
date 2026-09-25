package g60991.dev3.labyrinthe.view.viewJFX;

import g60991.dev3.labyrinthe.controller.LabyrintheController;
import g60991.dev3.labyrinthe.model.board.TileInfo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * View displaying the available side tile.
 * Shows tile shape, orientation, and treasure information.
 */
public class SideTileView extends VBox {

    private LabyrintheController controller;
    private TileView tileView;
    private final Text treasureText;

    /**
     * Creates the side tile view.
     */
    public SideTileView() {
        super(15);
        this.setPadding(new Insets(15));
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-border-radius: 10; ");

        Text title = new Text("Available Tile");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        treasureText = new Text();
        treasureText.setFont(Font.font("Arial", 12));

        this.getChildren().addAll(title, treasureText);
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
     * Updates side tile display.
     * Shows current side tile shape, orientation, and treasure.
     */
    public void update() {
        TileInfo sideTile = controller.askSideTile();

        if (sideTile != null) {
            if (tileView != null) {
                this.getChildren().remove(tileView);
            }

            tileView = new TileView(
                    sideTile.shape(),
                    sideTile.orientation(),
                    sideTile.treasure(),
                    controller.askAllFoundTreasures()
            );

            this.getChildren().add(1, tileView);

            if (sideTile.treasure() != null) {
                treasureText.setText("Treasure: " + sideTile.treasure().name());
            } else {
                treasureText.setText("No treasure");
            }
        }
    }
}