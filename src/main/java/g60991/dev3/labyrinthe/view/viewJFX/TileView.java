package g60991.dev3.labyrinthe.view.viewJFX;

import g60991.dev3.labyrinthe.model.board.Orientation;
import g60991.dev3.labyrinthe.model.board.Shape;
import g60991.dev3.labyrinthe.model.board.Treasure;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Set;

/**
 * Visual representation of a single tile.
 * Draws tile shape, orientation, and treasure icon.
 */
public class TileView extends StackPane {

    static final int SIZE = 60;

    private final Shape shape;
    private final Orientation orientation;
    private final Treasure treasure;
    private final Set<Treasure> foundTreasures;
    private Rectangle hoverHighlight;

    /**
     * Creates a tile view.
     *
     * @param shape          tile shape
     * @param orientation    tile orientation
     * @param treasure       treasure on tile or null
     * @param foundTreasures set of already found treasures
     */
    public TileView(Shape shape, Orientation orientation, Treasure treasure, Set<Treasure> foundTreasures) {
        this.shape = shape;
        this.orientation = orientation;
        this.treasure = treasure;
        this.foundTreasures = foundTreasures;
        setup();
    }

    /**
     * Sets hover effect on tile.
     * Shows yellow highlight when hovering.
     *
     * @param hover true to show hover effect
     */
    public void setHoverEffect(boolean hover) {
        if (hover) {
            setCursor(Cursor.HAND);

            if (hoverHighlight == null) {
                hoverHighlight = new Rectangle(SIZE, SIZE);
                hoverHighlight.setFill(Color.YELLOW.deriveColor(0, 1, 1, 0.4));
                hoverHighlight.setMouseTransparent(true);
            }

            if (!getChildren().contains(hoverHighlight)) {
                getChildren().add(0, hoverHighlight);
            }
        } else {
            setCursor(Cursor.DEFAULT);
            getChildren().remove(hoverHighlight);
        }
    }

    /**
     * Initializes tile display.
     * Creates canvas and draws tile with treasure if present.
     */
    private void setup() {
        this.setMinSize(SIZE, SIZE);
        this.setMaxSize(SIZE, SIZE);

        Canvas canvas = new Canvas(SIZE, SIZE);
        drawTile(canvas.getGraphicsContext2D());
        this.getChildren().add(canvas);

        if (treasure != null) {
            TreasureView treasureView = new TreasureView(treasure);
            this.getChildren().add(treasureView);
        }
    }

    /**
     * Draws tile on canvas.
     * Applies rotation based on orientation and draws shape.
     *
     * @param gc graphics context
     */
    private void drawTile(GraphicsContext gc) {
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, SIZE, SIZE);

        gc.setStroke(Color.BEIGE);
        gc.setLineWidth(20);

        int mid = SIZE / 2;

        gc.save();
        gc.translate(mid, mid);

        int rotationDegrees = orientation.ordinal() * 90;
        gc.rotate(rotationDegrees);

        switch (shape) {
            case I -> drawITile(gc, mid);
            case L -> drawLTile(gc, mid);
            case T -> drawTTile(gc, mid);
        }

        gc.restore();
        gc.setStroke(Color.SADDLEBROWN);
        gc.setLineWidth(2);
        gc.strokeRect(1, 1, SIZE - 2, SIZE - 2);
    }

    /**
     * Draws I-shaped tile (straight corridor).
     * Exits: north and south.
     *
     * @param gc  graphics context
     * @param mid center position
     */
    private void drawITile(GraphicsContext gc, int mid) {
        gc.strokeLine(0, 0, 0, -mid);
        gc.strokeLine(0, 0, 0, mid);
    }

    /**
     * Draws L-shaped tile (corner).
     * Exits: north and east.
     *
     * @param gc  graphics context
     * @param mid center position
     */
    private void drawLTile(GraphicsContext gc, int mid) {
        gc.strokeLine(0, 0, 0, -mid);
        gc.strokeLine(0, 0, mid, 0);
    }

    /**
     * Draws T-shaped tile (T-junction).
     * Exits: north, west, and east.
     *
     * @param gc  graphics context
     * @param mid center position
     */
    private void drawTTile(GraphicsContext gc, int mid) {
        gc.strokeLine(0, 0, 0, -mid);
        gc.strokeLine(0, 0, -mid, 0);
        gc.strokeLine(0, 0, mid, 0);
    }
}