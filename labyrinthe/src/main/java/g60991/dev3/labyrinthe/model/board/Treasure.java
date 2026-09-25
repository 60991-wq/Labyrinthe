package g60991.dev3.labyrinthe.model.board;

/**
 * Represents all treasures in the game.
 * 12 mobile treasures and 12 fixed treasures.
 */

public enum Treasure {

    GHOST(true),
    DRAGON(true),
    FAIRY(true),
    CROWN(true),
    MAP(true),
    EMERALD(true),
    RING(true),
    SWORD(true),
    CANDLE(true),
    HELMET(true),
    TREASURE_CHEST(true),
    GNOME(true),


    BAT(false),
    GENIE(false),
    OWL(false),
    BEETLE(false),
    RAT(false),
    SPIDER(false),
    butterfly(false),
    LIZARD(false),
    GRIMOIRE(false),
    GOLD_PURSE(false),
    KEYS(false),
    BONES(false);

    private final boolean isFixed;

    Treasure(boolean isFixed) {
        this.isFixed = isFixed;
    }

    public boolean isFixedTreasure() {
        return isFixed;
    }
}