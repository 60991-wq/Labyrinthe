package g60991.dev3.labyrinthe.view.viewJFX;

import g60991.dev3.labyrinthe.model.board.Orientation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Utility class for loading and creating image views.
 * Manages treasure icons and arrow icons for the game interface.
 */
public class ImageManager {

    private static final double ICON_SIZE = 30;
    private static final double ARROW_SIZE = 25;

    /**
     * Maps treasure names to image file names.
     *
     * @param treasureName treasure enum name
     * @return corresponding file name
     */
    private static String getFileNameForTreasure(String treasureName) {
        return switch (treasureName) {
            case "GHOST" -> "fantome";
            case "FAIRY" -> "fee";
            case "OWL" -> "hibou";
            case "SPIDER" -> "araignee";
            case "LIZARD" -> "lezard";
            case "GRIMOIRE" -> "livre";
            case "CROWN" -> "couronne";
            case "TREASURE_CHEST" -> "coffre";
            case "EMERALD" -> "diamant";
            case "SWORD" -> "epee";
            case "HELMET" -> "casque";
            case "RING" -> "bague";
            case "BONES" -> "os";
            case "BEETLE" -> "insecte";
            case "butterfly" -> "butterfly";
            case "CANDLE" -> "chandelier";
            case "GOLD_PURSE" -> "money";
            case "BAT" -> "chauve-souris";
            case "KEYS" -> "cle";
            case "MAP" -> "map";
            default -> treasureName.toLowerCase();
        };
    }

    /**
     * Creates icon image view for treasure.
     *
     * @param pieceName treasure name
     * @return configured image view
     */
    public static ImageView createIcon(String pieceName) {
        String fileName = getFileNameForTreasure(pieceName);
        String imagePath = "/images/iconImage/" + fileName + ".png";

        try {
            Image image = new Image(ImageManager.class.getResourceAsStream(imagePath));
            ImageView imageView = new ImageView(image);

            imageView.setFitWidth(ICON_SIZE);
            imageView.setFitHeight(ICON_SIZE);
            imageView.setPreserveRatio(true);

            return imageView;
        } catch (Exception e) {
            System.err.println("Failed to load piece icon: " + imagePath);
            return new ImageView();
        }
    }

    /**
     * Creates arrow icon for insertion direction.
     *
     * @param direction arrow direction
     * @return configured arrow image view
     */
    public static ImageView createArrowIcon(Orientation direction) {
        String imagePath = switch (direction) {
            case NORTH -> "/images/arrows/arrow_up.png";
            case SOUTH -> "/images/arrows/arrow_down.png";
            case EAST -> "/images/arrows/arrow_right.png";
            case WEST -> "/images/arrows/arrow_left.png";
        };

        try {
            Image image = new Image(ImageManager.class.getResourceAsStream(imagePath));
            ImageView imageView = new ImageView(image);

            imageView.setFitWidth(ARROW_SIZE);
            imageView.setFitHeight(ARROW_SIZE);
            imageView.setPreserveRatio(true);
            imageView.setStyle("-fx-opacity: 0.6;");

            return imageView;
        } catch (Exception e) {
            System.err.println("Failed to load arrow icon: " + imagePath);
            return new ImageView();
        }
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private ImageManager() {
        throw new AssertionError("Utility class should not be instantiated");
    }
}