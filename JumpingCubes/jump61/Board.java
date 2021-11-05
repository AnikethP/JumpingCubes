package jump61;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.function.Consumer;

import static jump61.Side.*;

/**
 * Represents the state of a Jump61 game.  Squares are indexed either by
 * row and column (between 1 and size()), or by square number, numbering
 * squares by rows, with squares in row 1 numbered from 0 to size()-1, in
 * row 2 numbered from size() to 2*size() - 1, etc. (i.e., row-major order).
 * <p>
 * A Board may be given a notifier---a Consumer<Board> whose
 * .accept method is called whenever the Board's contents are changed.
 *
 * @author Aniketh Prasad
 */
class Board {

    /**
     * A notifier that does nothing.
     */
    private static final Consumer<Board> NOP = (s) -> {
    };
    /**
     * Used in jump to keep track of squares needing processing.  Allocated
     * here to cut down on allocations.
     */
    private final ArrayDeque<Integer> _workQueue = new ArrayDeque<>();
    /**
     * Keeps track of visited nodes.
     */
    private final ArrayList<Integer> _visited = new ArrayList<>();
    /**
     * stores undo history.
     */
    private ArrayList<Square[][]> history = new ArrayList<>();
    /**
     * A read-only version of this Board.
     */
    private ConstantBoard _readonlyBoard;
    /**
     * Use _notifier.accept(B) to announce changes to this board.
     */
    private Consumer<Board> _notifier;
    /**
     * stores the contents of the board in a 2D array.
     */
    private Square[][] board;

    /**
     * An uninitialized Board.  Only for use by subtypes.
     */
    protected Board() {
        _notifier = NOP;
    }

    /**
     * An N x N board in initial configuration.
     */
    Board(int N) {
        this();
        board = new Square[N][N];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = Square.INITIAL;
            }
        }
        markUndo();
    }

    /**
     * A board whose initial contents are copied from BOARD0, but whose
     * undo history is clear, and whose notifier does nothing.
     */
    Board(Board board0) {
        this(board0.size());
        for (int i = 0; i < board0.size(); i++) {
            for (int j = 0; j < board0.size(); j++) {
                board[i][j] = board0.get(sqNum(i + 1, j + 1));
            }
        }
        history = new ArrayList<>();
        _notifier = NOP;
        _readonlyBoard = new ConstantBoard(this);
    }

    /**
     * Returns a readonly version of this board.
     */
    Board readonlyBoard() {
        return _readonlyBoard;
    }

    /**
     * (Re)initialize me to a cleared board with N squares on a side. Clears
     * the undo history and sets the number of moves to 0.
     */
    void clear(int N) {
        board = new Square[N][N];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = Square.INITIAL;
            }
        }
        history = new ArrayList<>();
        announce();
    }

    /**
     * Copy the contents of BOARD into me.
     *
     * @param x Board to copy from.
     */
    void copy(Board x) {
        this.board = new Square[x.board.length][x.board.length];
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                this.board[i][j] = x.board[i][j];
            }
        }
    }

    /**
     * Copy the contents of BOARD into me, without modifying my undo
     * history. Assumes BOARD and I have the same size.
     *
     * @param x Board to copy from without modifying undo history.
     */
    private void internalCopy(Board x) {
        assert size() == x.size();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = x.board[i][j];
            }
        }
    }

    /**
     * Return the number of rows and of columns of THIS.
     */
    int size() {
        return board.length;
    }

    /**
     * Returns the contents of the square at row R, column C
     * 1 <= R, C <= size ().
     */
    Square get(int r, int c) {
        return get(sqNum(r, c));
    }

    /**
     * Returns the contents of square #N, numbering squares by rows, with
     * squares in row 1 number 0 - size()-1, in row 2 numbered
     * size() - 2*size() - 1, etc.
     */
    Square get(int n) {
        int row = row(n);
        int col = col(n);
        return board[row - 1][col - 1];
    }

    /**
     * Returns the total number of spots on the board.
     */
    int numPieces() {
        int count = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                count += board[i][j].getSpots();
            }
        }
        return count;
    }

    /**
     * Returns the Side of the player who would be next to move.  If the
     * game is won, this will return the loser (assuming legal position).
     */
    Side whoseMove() {
        return ((numPieces() + size()) & 1) == 0 ? RED : BLUE;
    }

    /**
     * Return true iff row R and column C denotes a valid square.
     */
    final boolean exists(int r, int c) {
        return 1 <= r && r <= size() && 1 <= c && c <= size();
    }

    /**
     * Return true iff S is a valid square number.
     */
    final boolean exists(int s) {
        int N = size();
        return 0 <= s && s < N * N;
    }

    /**
     * Return the row number for square #N.
     */
    final int row(int n) {
        return n / size() + 1;
    }

    /**
     * Return the column number for square #N.
     */
    final int col(int n) {
        return n % size() + 1;
    }

    /**
     * Return the square number of row R, column C.
     */
    final int sqNum(int r, int c) {
        return (c - 1) + (r - 1) * size();
    }

    /**
     * Return a string denoting move (ROW, COL)N.
     */
    String moveString(int row, int col) {
        return String.format("%d %d", row, col);
    }

    /**
     * Return a string denoting move N.
     */
    String moveString(int n) {
        return String.format("%d %d", row(n), col(n));
    }

    /**
     * Returns true iff it would currently be legal for PLAYER to add a spot
     * to square at row R, column C.
     */
    boolean isLegal(Side player, int r, int c) {
        return isLegal(player, sqNum(r, c));
    }

    /**
     * Returns true iff it would currently be legal for PLAYER to add a spot
     * to square #N.
     */
    boolean isLegal(Side player, int n) {
        return player == get(n).getSide() || get(n).getSide() == WHITE;
    }

    /**
     * Returns true iff it would currently be legal for PLAYER to add a spot.
     */
    boolean isLegal(Side player) {
        return player == whoseMove();
    }

    /**
     * Returns the winner of the current position, if the game is over,
     * and otherwise null.
     */
    final Side getWinner() {
        Side winning = board[0][0].getSide();
        if (winning == WHITE) {
            return null;
        }
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (board[i][j].getSide() != winning) {
                    return null;
                }
            }
        }
        return winning;
    }

    /**
     * Return the number of squares of given SIDE.
     */
    int numOfSide(Side side) {
        int count = 0;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (board[i][j].getSide() == side) {
                    count += 1;
                }
            }
        }
        return count;
    }

    /**
     * Add a spot from PLAYER at row R, column C.  Assumes
     * isLegal(PLAYER, R, C).
     */
    void addSpot(Side player, int r, int c) {
        addSpot(player, sqNum(r, c));
    }

    /**
     * Add a spot from PLAYER at square #N.  Assumes isLegal(PLAYER, N).
     */
    void addSpot(Side player, int n) {
        internalSet(n, get(n).getSpots() + 1, player);
        if (overfull(n) && getWinner() == null) {
            jump(n);
        }
        markUndo();

    }

    /**
     * Set the square at row R, column C to NUM spots (0 <= NUM), and give
     * it color PLAYER if NUM > 0 (otherwise, white).
     */
    void set(int r, int c, int num, Side player) {
        internalSet(r, c, num, player);
        announce();
    }

    /**
     * Set the square at row R, column C to NUM spots (0 <= NUM), and give
     * it color PLAYER if NUM > 0 (otherwise, white).  Does not announce
     * changes.
     */
    private void internalSet(int r, int c, int num, Side player) {
        internalSet(sqNum(r, c), num, player);

    }

    /**
     * Set the square #N to NUM spots (0 <= NUM), and give it color PLAYER
     * if NUM > 0 (otherwise, white). Does not announce changes.
     */
    private void internalSet(int n, int num, Side player) {
        int row = row(n);
        int col = col(n);

        board[row - 1][col - 1] = Square.square(player, num);


    }

    /**
     * Undo the effects of one move (that is, one addSpot command).  One
     * can only undo back to the last point at which the undo history
     * was cleared, or the construction of this Board.
     */
    void undo() {

        if (history.size() > 1) {
            history.remove(history.size() - 1);
            board = history.get(history.size() - 1);
        }
    }

    /**
     * Record the beginning of a move in the undo history.
     */
    private void markUndo() {
        Square[][] newArr = new Square[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                newArr[i][j] = board[i][j];
            }
        }
        history.add(newArr);
    }

    /**
     * Add DELTASPOTS spots of side PLAYER to row R, column C,
     * updating counts of numbers of squares of each color.
     */
    private void simpleAdd(Side player, int r, int c, int deltaSpots) {
        internalSet(r, c, deltaSpots + get(r, c).getSpots(), player);
    }

    /**
     * Add DELTASPOTS spots of color PLAYER to square #N,
     * updating counts of numbers of squares of each color.
     */
    private void simpleAdd(Side player, int n, int deltaSpots) {
        if (getWinner() != null) {
            return;
        }
        internalSet(n, deltaSpots + get(n).getSpots(), player);
    }

    /**
     * Do all jumping on this board, assuming that initially, S is the only
     * square that might be over-full.
     *
     * @param n      spot number
     * @param player color of player Side
     */
    private void addJumpSpot(int n, Side player) {
        internalSet(n, get(n).getSpots() + 1, player);
        if (overfull(n) && getWinner() == null) {
            jump(n);
        }
    }

    /**
     * Do all jumping on this board, assuming that initially, S is the only
     * square that might be over-full.
     */
    private void jump(int S) {
        internalSet(S, 1, get(S).getSide());
        ArrayList<Integer> neighbors = getAdjacent(S);
        for (int i = 0; i < neighbors.size(); i++) {
            addJumpSpot(neighbors.get(i), get(S).getSide());
        }
    }

    /**
     * Gets all the neighbors.
     *
     * @param s square to find neighbors for.
     * @return ArrayList<Integer> arraylist containingi all neighbors
     */
    private ArrayList<Integer> getAdjacent(int s) {
        ArrayList<Integer> all = new ArrayList<>();
        if (exists(row(s) + 1, col(s))) {
            all.add(sqNum(row(s) + 1, col(s)));
        }
        if (exists(row(s) - 1, col(s))) {
            all.add(sqNum(row(s) - 1, col(s)));
        }
        if (exists(row(s), col(s) + 1)) {
            all.add(sqNum(row(s), col(s) + 1));
        }
        if (exists(row(s), col(s) - 1)) {
            all.add(sqNum(row(s), col(s) - 1));
        }
        return all;

    }

    /**
     * Does the jumping for blue.
     *
     * @param S square which must be jumped
     */

    public void doRepeat(int S) {
        if (row(S) > 1) {
            if (getWinner() != null) {
                return;
            }
            simpleAdd(BLUE, sqNum(row(S), col(S)), -1);
            simpleAdd(BLUE, sqNum(row(S) - 1, col(S)), 1);
            _workQueue.add(sqNum(row(S) - 1, col(S)));
            if (getWinner() != null) {
                return;
            }
        }
        if (col(S) > 1) {
            if (getWinner() != null) {
                return;
            }
            simpleAdd(BLUE, sqNum(row(S), col(S)), -1);
            simpleAdd(BLUE, sqNum(row(S), col(S) - 1), 1);
            _workQueue.add(sqNum(row(S), col(S) - 1));
            if (getWinner() != null) {
                return;
            }
        }
        if (row(S) < size()) {
            if (getWinner() != null) {
                return;
            }
            simpleAdd(BLUE, sqNum(row(S), col(S)), -1);
            simpleAdd(BLUE, sqNum(row(S) + 1, col(S)), 1);
            _workQueue.add(sqNum(row(S) + 1, col(S)));
            if (getWinner() != null) {
                return;
            }
        }
        if (col(S) < size()) {
            if (getWinner() != null) {
                return;
            }
            simpleAdd(BLUE, sqNum(row(S), col(S)), -1);
            simpleAdd(BLUE, sqNum(row(S), col(S) + 1), 1);
            _workQueue.add(sqNum(row(S), col(S) + 1));
            if (getWinner() != null) {
                return;
            }
        }
    }

    /**
     * Checks if a square is overfull.
     *
     * @param S integer representing square
     * @return boolean representing if the square is overfull or not.
     */
    public boolean overfull(int S) {
        return get(S).getSpots() > neighbors(S);
    }

    /**
     * Returns my dumped representation.
     */
    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("===\n");
        for (int i = 0; i < board.length; i++) {
            out.format("   ");
            for (int j = 0; j < board[0].length; j++) {
                char code = board[i][j].getSide().toString().charAt(0);
                if (code == 'w') {
                    code = '-';
                }
                out.format(" " + board[i][j].getSpots() + code + "");
            }
            out.format("\n");
        }
        out.format("===");
        return out.toString();
    }

    /**
     * Returns an external rendition of me, suitable for human-readable
     * textual display, with row and column numbers.  This is distinct
     * from the dumped representation (returned by toString).
     */
    public String toDisplayString() {
        String[] lines = toString().trim().split("\\R");
        Formatter out = new Formatter();
        for (int i = 1; i + 1 < lines.length; i += 1) {
            out.format("%2d %s%n", i, lines[i].trim());
        }
        out.format("  ");
        for (int i = 1; i <= size(); i += 1) {
            out.format("%3d", i);
        }
        return out.toString();
    }

    /**
     * Returns the number of neighbors of the square at row R, column C.
     */
    int neighbors(int r, int c) {
        int size = size();
        int n;
        n = 0;
        if (r > 1) {
            n += 1;
        }
        if (c > 1) {
            n += 1;
        }
        if (r < size) {
            n += 1;
        }
        if (c < size) {
            n += 1;
        }
        return n;
    }

    /**
     * Returns the number of neighbors of square #N.
     */
    int neighbors(int n) {
        return neighbors(row(n), col(n));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Board)) {
            return false;
        } else {
            Board B = (Board) obj;
            if (B.board.length != board.length
                    || B.board[0].length != board[0].length) {
                return false;
            }
            for (int i = 0; i < B.board.length; i++) {
                for (int j = 0; j < B.board[0].length; j++) {
                    if (B.board[i][j] != board[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    @Override
    public int hashCode() {
        return numPieces();
    }

    /**
     * Set my notifier to NOTIFY.
     */
    public void setNotifier(Consumer<Board> notify) {
        _notifier = notify;
        announce();
    }

    /**
     * Take any action that has been set for a change in my state.
     */
    private void announce() {
        _notifier.accept(this);
    }
}
