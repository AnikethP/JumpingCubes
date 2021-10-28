package jump61;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Formatter;

import java.util.function.Consumer;

import static jump61.Side.*;
import static jump61.Square.square;

/** Represents the state of a Jump61 game.  Squares are indexed either by
 *  row and column (between 1 and size()), or by square number, numbering
 *  squares by rows, with squares in row 1 numbered from 0 to size()-1, in
 *  row 2 numbered from size() to 2*size() - 1, etc. (i.e., row-major order).
 *
 *  A Board may be given a notifier---a Consumer<Board> whose
 *  .accept method is called whenever the Board's contents are changed.
 *
 *  @author Jeffrey Zhou
 */
class Board {

    /** An uninitialized Board.  Only for use by subtypes. */
    protected Board() {
        _notifier = NOP;
    }

    /** An N x N board in initial configuration. */
    Board(int N) {
        for (int i = 0; i < N * N; i++) {
            _squares.add(Square.square(WHITE, 1));
        }
        _size = N;
        _notifier = NOP;
        _numMoves = 0;
        _readonlyBoard = new ConstantBoard(this);
    }

    /** A board whose initial contents are copied from BOARD0, but whose
     *  undo history is clear, and whose notifier does nothing. */
    Board(Board board0) {
        this(board0.size());
        this._squares.clear();
        copy(board0);
        _readonlyBoard = new ConstantBoard(this);
    }

    /** Returns a readonly version of this board. */
    Board readonlyBoard() {
        return _readonlyBoard;
    }

    /** (Re)initialize me to a cleared board with N squares on a side. Clears
     *  the undo history and sets the number of moves to 0. */
    void clear(int N) {
        this._squares = new Board(N)._squares;
        this._size = N;
        _history.clear();
        _numMoves = 0;
        announce();
    }

    /** Copy the contents of BOARD into me. */
    void copy(Board board) {
        this._squares = new Board(board.size())._squares;
        internalCopy(board);
        _history.clear();
        _numMoves = 0;
    }

    /** Copy the contents of BOARD into me, without modifying my undo
     *  history. Assumes BOARD and I have the same size. */
    private void internalCopy(Board board) {
        assert size() == board.size();
        this._squares.clear();
        for (int i = 0; i < size() * size(); i++) {
            this._squares.add(board.get(i));
        }
    }

    /** Return the number of rows and of columns of THIS. */
    int size() {
        return _size;
    }

    /** Returns the contents of the square at row R, column C
     *  1 <= R, C <= size (). */
    Square get(int r, int c) {
        return get(sqNum(r, c));
    }

    /** Returns the contents of square #N, numbering squares by rows, with
     *  squares in row 1 number 0 - size()-1, in row 2 numbered
     *  size() - 2*size() - 1, etc. */
    Square get(int n) {
        return _squares.get(n);
    }

    /** Returns the total number of spots on the board. */
    int numPieces() {
        int count = 0;
        for (Square s: _squares) {
            count += s.getSpots();
        }
        return count;
    }

    /** Returns the Side of the player who would be next to move.  If the
     *  game is won, this will return the loser (assuming legal position). */
    Side whoseMove() {
        return ((numPieces() + size()) & 1) == 0 ? RED : BLUE;
    }

    /** Return true iff row R and column C denotes a valid square. */
    final boolean exists(int r, int c) {
        return 1 <= r && r <= size() && 1 <= c && c <= size();
    }

    /** Return true iff S is a valid square number. */
    final boolean exists(int s) {
        int N = size();
        return 0 <= s && s < N * N;
    }

    /** Return the row number for square #N. */
    final int row(int n) {
        return n / size() + 1;
    }

    /** Return the column number for square #N. */
    final int col(int n) {
        return n % size() + 1;
    }

    /** Return the square number of row R, column C. */
    final int sqNum(int r, int c) {
        return (c - 1) + (r - 1) * size();
    }

    /** Return a string denoting move (ROW, COL)N. */
    String moveString(int row, int col) {
        return String.format("%d %d", row, col);
    }

    /** Return a string denoting move N. */
    String moveString(int n) {
        return String.format("%d %d", row(n), col(n));
    }

    /** Returns true iff it would currently be legal for PLAYER to add a spot
     to square at row R, column C. */
    boolean isLegal(Side player, int r, int c) {
        return isLegal(player, sqNum(r, c));
    }

    /** Returns true iff it would currently be legal for PLAYER to add a spot
     *  to square #N. */
    boolean isLegal(Side player, int n) {
        Square curr = get(n);
        if (curr.getSide().equals(player) || curr.getSide().equals(WHITE)) {
            return true;
        }
        return false;
    }

    /** Returns true iff PLAYER is allowed to move at this point. */
    boolean isLegal(Side player) {
        return whoseMove().equals(player);
    }

    /** Returns the winner of the current position, if the game is over,
     *  and otherwise null. */
    final Side getWinner() {
        if (numOfSide(BLUE) == _squares.size()) {
            return BLUE;
        } else if (numOfSide(RED) == _squares.size()) {
            return RED;
        }
        return null;
    }

    /** Return the number of squares of given SIDE. */
    int numOfSide(Side side) {
        int sidecount = 0;
        for (Square s : _squares) {
            if (s.getSide().equals(side)) {
                sidecount += 1;
            }
        }
        return sidecount;
    }

    /** Add a spot from PLAYER at row R, column C.  Assumes
     *  isLegal(PLAYER, R, C). */
    void addSpot(Side player, int r, int c) {
        addSpot(player, sqNum(r, c));
    }

    /** Add a spot from PLAYER at square #N.  Assumes isLegal(PLAYER, N). */
    void addSpot(Side player, int n) {
        Square curr = get(n);
        internalSet(n, curr.getSpots() + 1, player);
        if (neighbors(n) < get(n).getSpots() && getWinner() == null) {
            jump(n);
        }
        markUndo();
    }

    /** I hate myself very much for writing this too.
     * Add a spot from PLAYER at square #N.  Assumes isLegal(PLAYER, N)*/
    void internaladdSpot(Side player, int n) {
        Square curr = get(n);
        internalSet(n, curr.getSpots() + 1, player);
        if (neighbors(n) < get(n).getSpots() && getWinner() == null) {
            jump(n);
        }
    }

    /** Set the square at row R, column C to NUM spots (0 <= NUM), and give
     *  it color PLAYER if NUM > 0 (otherwise, white). */
    void set(int r, int c, int num, Side player) {
        internalSet(r, c, num, player);
        announce();
    }

    /** Set the square at row R, column C to NUM spots (0 <= NUM), and give
     *  it color PLAYER if NUM > 0 (otherwise, white).  Does not announce
     *  changes. */
    private void internalSet(int r, int c, int num, Side player) {
        internalSet(sqNum(r, c), num, player);
    }

    /** Set the square #N to NUM spots (0 <= NUM), and give it color PLAYER
     *  if NUM > 0 (otherwise, white). Does not announce changes. */
    private void internalSet(int n, int num, Side player) {
        Square tochange = Square.square(player, num);
        _squares.set(n, tochange);
    }

    /** Undo the effects of one move (that is, one addSpot command).  One
     *  can only undo back to the last point at which the undo history
     *  was cleared, or the construction of this Board. */
    void undo() {
        if (numMoves() == 1) {
            Board revert = new Board(size());
            internalCopy(revert);
        } else if (numMoves() < 0) {
            throw new GameException("cannot undo anymore");
        } else {
            Board revert = histget(historyLength() - 2);
            internalCopy(revert);
            _history.remove(historyLength() - 1);
            _numMoves -= 1;
        }
    }

    /** Record the beginning of a move in the undo history. */
    private void markUndo() {
        _history.add(new Board(this));
        _numMoves += 1;
    }

    /** Add DELTASPOTS spots of side PLAYER to row R, column C,
     *  updating counts of numbers of squares of each color. */
    private void simpleAdd(Side player, int r, int c, int deltaSpots) {
        internalSet(r, c, deltaSpots + get(r, c).getSpots(), player);
    }

    /** Add DELTASPOTS spots of color PLAYER to square #N,
     *  updating counts of numbers of squares of each color. */
    private void simpleAdd(Side player, int n, int deltaSpots) {
        internalSet(n, deltaSpots + get(n).getSpots(), player);
    }

    /** Used in jump to keep track of squares needing processing.  Allocated
     *  here to cut down on allocations. */
    private final ArrayDeque<Integer> _workQueue = new ArrayDeque<>();

    /** Do all jumping on this board, assuming that initially, S is the only
     *  square that might be over-full. */
    private void jump(int S) {
        Square curr = get(S);
        internalSet(S, 1, curr.getSide());
        for (int i : getNeighbour(S)) {
            internaladdSpot(curr.getSide(), i);
        }
    }

    /** Returns all neighbours of square index s.
     * @param s is the index of the square that we are getneighbouring
     * */
    private ArrayList<Integer> getNeighbour(int s) {
        int count = neighbors(s);
        ArrayList<Integer> allNeighbours = new ArrayList<>();
        int currrow = row(s);
        int currcol = col(s);
        if (count == 4) {
            allNeighbours.add(sqNum(currrow + 1, currcol));
            allNeighbours.add(sqNum(currrow - 1, currcol));
            allNeighbours.add(sqNum(currrow, currcol + 1));
            allNeighbours.add(sqNum(currrow, currcol - 1));
        } else if (count == 3) {
            if (exists(currrow + 1, currcol)) {
                allNeighbours.add(sqNum(currrow + 1, currcol));
            }
            if (exists(currrow - 1, currcol)) {
                allNeighbours.add(sqNum(currrow - 1, currcol));
            }
            if (exists(currrow, currcol + 1)) {
                allNeighbours.add(sqNum(currrow, currcol + 1));
            }
            if (exists(currrow, currcol - 1)) {
                allNeighbours.add(sqNum(currrow, currcol - 1));
            }
        } else if (count == 2) {
            if (exists(currrow + 1, currcol)) {
                allNeighbours.add(sqNum(currrow + 1, currcol));
            }
            if (exists(currrow - 1, currcol)) {
                allNeighbours.add(sqNum(currrow - 1, currcol));
            }
            if (exists(currrow, currcol + 1)) {
                allNeighbours.add(sqNum(currrow, currcol + 1));
            }
            if (exists(currrow, currcol - 1)) {
                allNeighbours.add(sqNum(currrow, currcol - 1));
            }
        }
        return allNeighbours;
    }

    /** Returns my dumped representation. */
    @Override
    public String toString() {
        Formatter out = new Formatter();
        int tracker = 0;
        out.format("===%n");
        out.format("    ");
        for (Square s: _squares) {
            if (tracker == size()) {
                tracker = 0;
                out.format("%n    ");
            }
            int spots = s.getSpots();
            Side player = s.getSide();
            String plshort = "-";
            if (player.equals(RED)) {
                plshort = "r";
            } else if (player.equals(BLUE)) {
                plshort = "b";
            }
            out.format("%d%s ", spots, plshort);
            tracker += 1;
        }
        out.format("%n===");

        return out.toString();
    }

    /** Returns an external rendition of me, suitable for human-readable
     *  textual display, with row and column numbers.  This is distinct
     *  from the dumped representation (returned by toString). */
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

    /** Returns the number of neighbors of the square at row R, column C. */
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

    /** Returns the number of neighbors of square #N. */
    int neighbors(int n) {
        return neighbors(row(n), col(n));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Board)) {
            return false;
        }
        Board B = (Board) obj;
        if (B.size() != this.size()) {
            return false;
        }

        for (int i = 0; i < B.size(); i++) {
            if (this._squares.get(i).getSpots()
                    != B._squares.get(i).getSpots()
                    || this._squares.get(i).getSide()
                    != B._squares.get(i).getSide()) {
                return false;
            }
        }
        return true;
    }
    /** returns the number of squares on a board that each side has.
     * @param s is the side that we are counting squares for
     * */
    public int getSidesquares(Side s) {
        int count = 0;
        for (Square square: _squares) {
            if (square.getSide().equals(s)) {
                count += square.getSpots();
            }
        }
        return count;
    }

    /** function for AI.
     * @param s is the side that we are looking for valid moves for
     * @return returns an arraylist of arraylists
     * */
    public ArrayList getValidMoves(Side s) {
        ArrayList<ArrayList> validMoves = new ArrayList<>();
        for (int i = 0; i < _squares.size(); i++) {
            if (isLegal(s, i)) {
                Board toAdd = new Board(this);
                toAdd.addSpot(s, i);
                ArrayList<Object> toAddlist = new ArrayList<>();
                toAddlist.add((Object) i);
                toAddlist.add(toAdd);
                validMoves.add(toAddlist);
            }
        }
        return validMoves;
    }


    @Override
    public int hashCode() {
        return numPieces();
    }

    /** Set my notifier to NOTIFY. */
    public void setNotifier(Consumer<Board> notify) {
        _notifier = notify;
        announce();
    }

    /** Take any action that has been set for a change in my state. */
    private void announce() {
        _notifier.accept(this);
    }

    /** A notifier that does nothing. */
    private static final Consumer<Board> NOP = (s) -> { };

    /** A read-only version of this Board. */
    private ConstantBoard _readonlyBoard;

    /** Use _notifier.accept(B) to announce changes to this board. */
    private Consumer<Board> _notifier;

    /** to be named later. */
    private ArrayList<Square> _squares = new ArrayList<>();

    /** to be named later.
     * @return arraylist of squares */
    public ArrayList<Square> getSquares() {
        return _squares;
    }
    /** to be named later. */
    private int _size;

    /** to be named later. */
    private ArrayList<Board> _history = new ArrayList<>();
    /** to be named later. */
    private int _numMoves;

    /** to be named later.
     * @return number of moves*/
    int numMoves() {
        return _numMoves;
    }

    /** to be named later.
     * @return size of history*/
    int historyLength() {
        return _history.size();
    }

    /** to be named later.
     * @param index is the index of the board
     * @return specific history board*/
    Board histget(int index) {
        return _history.get(index);
    }

}