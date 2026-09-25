package g60991.dev3.labyrinthe.model.strategy;

import g60991.dev3.labyrinthe.model.board.InsertPoint;
import g60991.dev3.labyrinthe.model.board.Position;
import g60991.dev3.labyrinthe.model.board.Rotation;
import g60991.dev3.labyrinthe.model.game.GameFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * AI strategy that makes random valid decisions.
 */
public class RandomStrategy implements AIStrategy {

    private final Random random = new Random();

    @Override
    public InsertionChoice chooseInsertion(GameFacade model) {

        List<InsertPoint> validPoints = getValidInsertPoints(model);

        InsertPoint point = validPoints.get(random.nextInt(validPoints.size()));


        Rotation rotation = random.nextBoolean() ? Rotation.CLOCKWISE : Rotation.COUNTERCLOCKWISE;

        return new InsertionChoice(point, rotation);
    }

    /**
     * Chooses a random reachable position.
     *
     * @param model game facade
     * @return random target position
     */
    @Override
    public Position chooseMove(GameFacade model) {

        Position startPos = model.getCurrentPlayerPosition();

        List<Position> reachable = model.getReachablePositions(startPos);

        if (reachable.isEmpty()) {
            return startPos;
        }
        return reachable.get(random.nextInt(reachable.size()));
    }

    /**
     * Gets all valid insertion points (excludes opposite of last insertion).
     *
     * @param model game facade
     * @return list of valid insertion points
     */
    private List<InsertPoint> getValidInsertPoints(GameFacade model) {
        InsertPoint last = model.getLastInsertPoint();
        InsertPoint opposite = (last != null) ? last.getOpposite() : null;

        List<InsertPoint> validPoints = new ArrayList<>();
        for (InsertPoint point : InsertPoint.ALL_POINTS) {
            if (!point.equals(opposite)) {
                validPoints.add(point);
            }
        }
        return validPoints;
    }
}