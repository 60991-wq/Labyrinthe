package g60991.dev3.labyrinthe.model.util;

/**
 * Observable interface for the Observer pattern.
 */

public interface Observable {
    /**
     * Registers an observer.
     *
     * @param o observer to register
     */
    void registerObserver(Observer o);


}
