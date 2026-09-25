package g60991.dev3.labyrinthe;

import g60991.dev3.labyrinthe.controller.LabyrintheController;
import g60991.dev3.labyrinthe.view.ConsoleView;

public class Main {
    public static void main(String[] args) {
        ConsoleView consoleView = new ConsoleView();
        LabyrintheController labyrintheController = new LabyrintheController(consoleView);
        labyrintheController.start();

    }
}
