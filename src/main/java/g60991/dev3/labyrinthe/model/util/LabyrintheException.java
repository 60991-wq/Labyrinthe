package g60991.dev3.labyrinthe.model.util;

/**
 * Custom exception for Labyrinth game errors.
 */
public class LabyrintheException extends RuntimeException {

    /**
     * Creates an exception with a message.
     *
     * @param message error message
     */

    public LabyrintheException(String message) {
        super(message);
    }
}