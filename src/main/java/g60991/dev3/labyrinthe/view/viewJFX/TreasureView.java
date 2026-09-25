package g60991.dev3.labyrinthe.view.viewJFX;

import g60991.dev3.labyrinthe.model.board.Treasure;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * Visual representation of a treasure icon.
 * Displays treasure image on tile.
 */
public class TreasureView extends StackPane {

    /**
     * Creates a treasure view.
     *
     * @param treasure treasure to display
     */
    public TreasureView(Treasure treasure) {
        String treasureConstantName = treasure.name();
        ImageView treasureIcon = ImageManager.createIcon(treasureConstantName);

        this.getChildren().add(treasureIcon);
    }
}