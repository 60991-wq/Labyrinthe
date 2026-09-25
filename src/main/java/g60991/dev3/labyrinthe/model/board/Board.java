package g60991.dev3.labyrinthe.model.board;

import g60991.dev3.labyrinthe.model.game.PlayerColor;

import java.util.*;

/**
 * Represents the game board for the Labyrinth game.
 * Manages tiles, player positions, and board operations.
 *
 * @author Abla
 */
public class Board {

    private final Tile[][] board;
    private Tile sideTile;
    private final Map<PlayerColor, Position> playerPositions;
    private InsertPoint lastInsertPoint;
    private Position lastInsertionPos;

    /**
     * Creates a new board and initializes it.
     */
    public Board() {
        this.board = new Tile[BoardConfig.BOARD_SIZE][BoardConfig.BOARD_SIZE];
        this.playerPositions = new HashMap<>();
        initialize();
    }

    /**
     * Initializes the board with fixed and mobile tiles.
     */
    private void initialize() {
        initializePlayerStartPositions();

        List<Treasure> fixedTreasures = new ArrayList<>();
        List<Treasure> mobileTreasuresPool = new ArrayList<>();

        for (Treasure treasure : Treasure.values()) {
            if (treasure.isFixedTreasure()) {
                fixedTreasures.add(treasure);
            } else {
                mobileTreasuresPool.add(treasure);
            }
        }


        Collections.shuffle(mobileTreasuresPool);

        placeFixedCornerTiles();
        placeFixedTTilesWithFixedTreasures(fixedTreasures);

        int mobileTreasureIndex = 0;
        List<Tile> mobileTiles = createMobileTiles(mobileTreasuresPool, mobileTreasureIndex);

        placeMobileTiles(mobileTiles);

    }

    /**
     * Sets initial positions for all players.
     */
    private void initializePlayerStartPositions() {
        playerPositions.put(PlayerColor.RED, new Position(0, 0));
        playerPositions.put(PlayerColor.BLUE, new Position(0, BoardConfig.LAST_INDEX));
        playerPositions.put(PlayerColor.GREEN, new Position(BoardConfig.LAST_INDEX, BoardConfig.LAST_INDEX));
        playerPositions.put(PlayerColor.YELLOW, new Position(BoardConfig.LAST_INDEX, 0));
    }

    /**
     * Places fixed L-shaped corner tiles.
     */
    private void placeFixedCornerTiles() {
        for (BoardConfig.FixedTileInfo info : BoardConfig.FIXED_L_CORNERS) {
            board[info.row()][info.col()] = new TileL(info.orientation(), null);
        }
    }


    /**
     * Places the 12 fixed T tiles with their 12 fixed treasures.
     *
     * @param fixedTreasures the list of the 12 fixed treasures
     */
    private void placeFixedTTilesWithFixedTreasures(List<Treasure> fixedTreasures) {
        int fixedTreasureIndex = 0;
        for (BoardConfig.FixedTileInfo info : BoardConfig.FIXED_T_WITH_TREASURE) {
            board[info.row()][info.col()] = new TileT(
                    info.orientation(),
                    fixedTreasures.get(fixedTreasureIndex++)
            );
        }
    }

    /**
     * Creates the list of mobile tiles.
     *
     * @param treasures     list of treasures to assign
     * @param treasureIndex starting index in treasure list
     * @return shuffled list of mobile tiles
     */
    private List<Tile> createMobileTiles(List<Treasure> treasures, int treasureIndex) {
        List<Tile> mobile = new ArrayList<>();

        for (int i = 0; i < BoardConfig.MOBILE_T_WITH_TREASURE_COUNT; i++) {
            mobile.add(new TileT(getRandomOrientation(), treasures.get(treasureIndex++)));
        }

        for (int i = 0; i < BoardConfig.MOBILE_L_WITH_TREASURE_COUNT; i++) {
            mobile.add(new TileL(getRandomOrientation(), treasures.get(treasureIndex++)));
        }

        for (int i = 0; i < BoardConfig.MOBILE_L_WITHOUT_TREASURE_COUNT; i++) {
            mobile.add(new TileL(getRandomOrientation(), null));
        }

        for (int i = 0; i < BoardConfig.MOBILE_I_COUNT; i++) {
            mobile.add(new TileI(getRandomOrientation(), null));
        }

        Collections.shuffle(mobile);
        return mobile;
    }


    /**
     * Returns a random orientation.
     *
     * @return random orientation
     */
    private Orientation getRandomOrientation() {
        Orientation[] orientations = Orientation.values();
        return orientations[new Random().nextInt(orientations.length)];
    }

    /**
     * Places mobile tiles on empty board positions.
     *
     * @param mobileTiles list of mobile tiles
     */
    private void placeMobileTiles(List<Tile> mobileTiles) {
        int index = 0;

        for (int row = 0; row < BoardConfig.BOARD_SIZE; row++) {
            for (int col = 0; col < BoardConfig.BOARD_SIZE; col++) {
                if (board[row][col] == null) {
                    board[row][col] = mobileTiles.get(index++);
                }
            }
        }
        sideTile = mobileTiles.get(index);
    }

    /**
     * Inserts the side tile at the specified point.
     *
     * @param insertPoint the insertion point and direction
     * @throws IllegalArgumentException if insert point is null
     */
    public void insert(InsertPoint insertPoint) {
        if (insertPoint == null) {
            throw new IllegalArgumentException("Insert point cannot be null");
        }

        Orientation direction = insertPoint.direction();
        int index = getAffectedIndex(insertPoint.position(), direction);

        shiftPlayers(direction, index);
        shiftTiles(direction, index);
        lastInsertionPos = insertPoint.position();

        lastInsertPoint = insertPoint;
    }


    /**
     * Shifts tiles based on direction and index.
     *
     * @param direction direction of shift
     * @param index     row or column index
     */
    private void shiftTiles(Orientation direction, int index) {
        switch (direction) {
            case NORTH -> shiftColumnUp(index);
            case SOUTH -> shiftColumnDown(index);
            case WEST -> shiftRowLeft(index);
            case EAST -> shiftRowRight(index);
        }
    }

    /**
     * Shifts a column upward.
     *
     * @param col column index
     */
    private void shiftColumnUp(int col) {
        Tile expelled = board[0][col];

        for (int r = 0; r < BoardConfig.LAST_INDEX; r++) {
            board[r][col] = board[r + 1][col];
        }
        board[BoardConfig.LAST_INDEX][col] = sideTile;

        sideTile = expelled;
    }

    /**
     * Shifts a column downward.
     *
     * @param col column index
     */
    private void shiftColumnDown(int col) {
        Tile expelled = board[BoardConfig.LAST_INDEX][col];

        for (int r = BoardConfig.LAST_INDEX; r > 0; r--) {
            board[r][col] = board[r - 1][col];
        }
        board[0][col] = sideTile;

        sideTile = expelled;
    }

    /**
     * Shifts a row to the left.
     *
     * @param row row index
     */
    private void shiftRowLeft(int row) {
        Tile expelled = board[row][0];

        for (int c = 0; c < BoardConfig.LAST_INDEX; c++) {
            board[row][c] = board[row][c + 1];
        }
        board[row][BoardConfig.LAST_INDEX] = sideTile;

        sideTile = expelled;
    }

    /**
     * Shifts a row to the right.
     *
     * @param row row index
     */
    private void shiftRowRight(int row) {
        Tile expelled = board[row][BoardConfig.LAST_INDEX];

        for (int c = BoardConfig.LAST_INDEX; c > 0; c--) {
            board[row][c] = board[row][c - 1];
        }
        board[row][0] = sideTile;

        sideTile = expelled;
    }

    /**
     * Checks if movement to neighbor is possible.
     *
     * @param pos current position
     * @param dir direction to move
     * @return true if movement is allowed
     */
    /**
     * Checks if movement to neighbor is possible.
     *
     * @param pos current position
     * @param dir direction to move
     * @return true if movement is allowed
     */
    public boolean canMoveToNeighbor(Position pos, Orientation dir) {
        if (!isValidCurrentTile(pos)) {
            return false;
        }

        if (!currentTileAllowsExit(pos, dir)) {
            return false;
        }

        Position next = pos.getNeighbor(dir);

        if (!isValidNeighborTile(next)) {
            return false;
        }

        return neighborAllowsEntry(next, dir);
    }

    /**
     * Checks if current position has a valid tile.
     *
     * @param pos position to check
     * @return true if valid
     */
    private boolean isValidCurrentTile(Position pos) {
        if (!pos.isInBounds(BoardConfig.BOARD_SIZE)) {
            return false;
        }
        return board[pos.row()][pos.col()] != null;
    }

    /**
     * Checks if current tile allows exit in direction.
     *
     * @param pos position
     * @param dir direction
     * @return true if exit allowed
     */
    private boolean currentTileAllowsExit(Position pos, Orientation dir) {
        return board[pos.row()][pos.col()].hasExit(dir);
    }

    /**
     * Checks if neighbor position has a valid tile.
     *
     * @param next neighbor position
     * @return true if valid
     */
    private boolean isValidNeighborTile(Position next) {
        if (!next.isInBounds(BoardConfig.BOARD_SIZE)) {
            return false;
        }
        return board[next.row()][next.col()] != null;
    }

    /**
     * Checks if neighbor tile allows entry from direction.
     *
     * @param next neighbor position
     * @param dir  direction of entry
     * @return true if entry allowed
     */
    private boolean neighborAllowsEntry(Position next, Orientation dir) {
        Tile neighbor = board[next.row()][next.col()];
        return neighbor.hasExit(dir.opposite());
    }

    /**
     * Gets all valid neighbor positions.
     *
     * @param pos current position
     * @return list of reachable neighbors
     */
    private List<Position> getValidNeighbors(Position pos) {
        List<Position> list = new ArrayList<>();

        for (Orientation dir : Orientation.values()) {
            if (canMoveToNeighbor(pos, dir)) {
                list.add(pos.getNeighbor(dir));
            }
        }
        return list;
    }

    /**
     * Explores reachable positions using BFS.
     *
     * @param start starting position
     * @return set of reachable positions
     */
    private Set<Position> bfsExplore(Position start) {
        Set<Position> visited = new HashSet<>();
        Queue<Position> queue = new LinkedList<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Position current = queue.poll();

            for (Position neighbor : getValidNeighbors(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return visited;
    }

    /**
     * Checks if a path exists between two positions.
     *
     * @param start start position
     * @param end   end position
     * @return true if path exists
     */
    public boolean hasPathBetween(Position start, Position end) {

        if (start == null || end == null) {
            return false;
        }

        if (!start.isInBounds(BoardConfig.BOARD_SIZE)) {
            return false;
        }

        if (!end.isInBounds(BoardConfig.BOARD_SIZE)) {
            return false;
        }

        if (start.equals(end)) {
            return true;
        }

        Set<Position> reachable = bfsExplore(start);

        return reachable.contains(end);
    }

    /**
     * Finds all positions reachable from start.
     *
     * @param start starting position
     * @return list of reachable positions
     */
    public List<Position> findReachablePositions(Position start) {
        if (start == null || !start.isInBounds(BoardConfig.BOARD_SIZE)) {
            return new ArrayList<>();
        }

        return new ArrayList<>(bfsExplore(start));
    }

    /**
     * Shifts players affected by tile insertion.
     *
     * @param dir   direction of shift
     * @param index row or column index
     */
    private void shiftPlayers(Orientation dir, int index) {
        for (Map.Entry<PlayerColor, Position> entry : playerPositions.entrySet()) {
            PlayerColor color = entry.getKey();
            Position currentPos = entry.getValue();
            Position newPos = shiftPlayerIfOnLine(currentPos, dir, index);
            playerPositions.put(color, newPos);
        }
    }

    /**
     * Shifts player position if on affected line.
     *
     * @param p     current position
     * @param dir   shift direction
     * @param index line index
     * @return new position
     */
    private Position shiftPlayerIfOnLine(Position p, Orientation dir, int index) {
        boolean isOnLine = (dir == Orientation.NORTH || dir == Orientation.SOUTH)
                ? p.col() == index
                : p.row() == index;
        if (!isOnLine) {
            return p;
        }

        int dx = 0;
        int dy = 0;
        switch (dir) {
            case SOUTH -> dx = 1;
            case NORTH -> dx = -1;
            case EAST -> dy = 1;
            case WEST -> dy = -1;
        }

        int newRow = wrap(p.row() + dx);
        int newCol = wrap(p.col() + dy);

        return new Position(newRow, newCol);
    }

    /**
     * Wraps coordinate to stay within board bounds.
     *
     * @param coordinate coordinate value
     * @return wrapped coordinate
     */
    private int wrap(int coordinate) {
        if (coordinate > BoardConfig.LAST_INDEX) return 0;
        if (coordinate < 0) return BoardConfig.LAST_INDEX;
        return coordinate;
    }

    /**
     * Gets affected row or column index from insertion.
     *
     * @param insertionPos insertion position
     * @param dir          insertion direction
     * @return affected index
     */
    private int getAffectedIndex(Position insertionPos, Orientation dir) {
        return switch (dir) {
            case NORTH, SOUTH -> insertionPos.col();
            case EAST, WEST -> insertionPos.row();
        };
    }

    /**
     * Moves a player to a new position.
     *
     * @param newPosition new position
     * @param color       player color
     */
    public void movePlayer(Position newPosition, PlayerColor color) {
        playerPositions.put(color, newPosition);

    }


    /**
     * Gets a player's current position.
     *
     * @param color player color
     * @return player position
     */
    public Position getPlayerPosition(PlayerColor color) {
        return playerPositions.get(color);
    }


    /**
     * Gets treasure at a position.
     *
     * @param pos position
     * @return treasure or null
     */
    public Treasure getTreasureAt(Position pos) {
        if (!pos.isInBounds(BoardConfig.BOARD_SIZE)) {
            return null;
        }
        Tile t = board[pos.row()][pos.col()];
        return t != null ? t.getTreasure() : null;
    }


    /**
     * Finds position of a specific treasure.
     *
     * @param treasure treasure to find
     * @return position or null
     */
    public Position getTreasurePosition(Treasure treasure) {
        for (int r = 0; r < BoardConfig.BOARD_SIZE; r++) {
            for (int c = 0; c < BoardConfig.BOARD_SIZE; c++) {
                Tile tile = board[r][c];
                if (tile != null && treasure.equals(tile.getTreasure())) {
                    return new Position(r, c);
                }
            }
        }
        return null;
    }

    /**
     * Gets tile at a position.
     *
     * @param pos position
     * @return tile or null
     */
    public Tile getTileAt(Position pos) {
        if (!pos.isInBounds(BoardConfig.BOARD_SIZE)) {
            return null;
        }
        return board[pos.row()][pos.col()];
    }

    /**
     * Gets last insertion position.
     *
     * @return last insertion position
     */
    public Position getLastInsertionPos() {
        return lastInsertionPos;
    }


    /**
     * Gets the board array.
     *
     * @return 2D tile array
     */
    public Tile[][] getBoard() {
        return board;
    }

    /**
     * Gets the current side tile.
     *
     * @return side tile
     */
    public Tile getSideTile() {
        return sideTile;
    }


    /**
     * Rotates the side tile.
     *
     * @param rotation rotation to apply
     * @throws IllegalStateException if side tile is null
     */
    public void rotateSideTile(Rotation rotation) {
        if (sideTile == null) {
            throw new IllegalStateException("Side tile is missing.");
        }
        sideTile.rotate(rotation);
    }


    /**
     * Sets the last insert point.
     *
     * @param p insert point
     */
    public void setLastInsertPoint(InsertPoint p) {
        this.lastInsertPoint = p;
    }


    /**
     * Gets the last insert point.
     *
     * @return last insert point
     */
    public InsertPoint getLastInsertPoint() {
        return lastInsertPoint;
    }


}