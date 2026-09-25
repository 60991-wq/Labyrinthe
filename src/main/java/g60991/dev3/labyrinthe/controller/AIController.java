package g60991.dev3.labyrinthe.controller;

import g60991.dev3.labyrinthe.model.strategy.InsertionChoice;
import g60991.dev3.labyrinthe.model.game.GameFacade;
import g60991.dev3.labyrinthe.model.board.Position;
import g60991.dev3.labyrinthe.model.strategy.AIStrategy;
import g60991.dev3.labyrinthe.view.ConsoleView;

/**
 * Handles AI player turn execution.
 * Serves as intermediary to call AI logic (Strategy) and execute actions via game facade.
 */
public class AIController {

    private final GameFacade game;
    private final ConsoleView view;
    private final AIStrategy strategy;

    /**
     * Creates an AI controller with console view.
     *
     * @param game game facade
     * @param view console view for AI messages
     * @param strategy AI strategy to execute
     */
    public AIController(GameFacade game, ConsoleView view, AIStrategy strategy) {
        this.game = game;
        this.view = view;
        this.strategy = strategy;
    }

    /**
     * Creates an AI controller without console view (for JavaFX).
     *
     * @param game game facade
     * @param strategy AI strategy to execute
     */
    public AIController(GameFacade game, AIStrategy strategy) {
        this(game, null, strategy);
    }

    /**
     * Executes complete AI turn (rotation, insertion, and movement).
     *
     * @return true if AI found its objective
     */
    public boolean playAITurn() {
        try {
            notifyAIThinking();

            InsertionChoice choice = strategy.chooseInsertion(game);

            game.rotateSideTile(choice.rotation());
            game.insert(choice.point());

            Position targetPos = strategy.chooseMove(game);
            boolean objectiveFound = game.move(targetPos);

            return objectiveFound;

        } catch (Exception e) {
            notifyAIError(e);
            return false;
        }
    }

    /**
     * Notifies view that AI is thinking.
     */
    private void notifyAIThinking() {
        if (view != null) {
            view.onAIThinking(game.getCurrentPlayerColor());
        }
    }

    /**
     * Notifies view of AI error.
     *
     * @param e exception that occurred
     */
    private void notifyAIError(Exception e) {
        if (view != null) {
            view.onAIError(e);
        }
    }
}