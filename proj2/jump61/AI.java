package jump61;

import java.util.ArrayList;
import java.util.Random;

import static jump61.Side.BLUE;
import static jump61.Side.RED;

/**
 * An automated Player.
 *
 * @author P. N. Hilfinger
 */
class AI extends Player {

    /**
     * A random-number generator used for move selection.
     */
    private final Random _random;
    /**
     * Used to convey moves discovered by minMax.
     */
    private int _foundMove;


    /**
     * A new player of GAME initially COLOR that chooses moves automatically.
     * SEED provides a random-number seed used for choosing moves.
     */
    AI(Game game, Side color, long seed) {
        super(game, color);
        _random = new Random(seed);
    }

    @Override
    String getMove() {

        Board board = getGame().getBoard();

        assert getSide() == board.whoseMove();
        int choice = searchForMove();
        getGame().reportMove(board.row(choice), board.col(choice));
        System.out.println(board.row(choice) + " " + board.col(choice));
        return String.format("%d %d", board.row(choice), board.col(choice));
    }

    /**
     * Return a move after searching the game tree to DEPTH>0 moves
     * from the current position. Assumes the game is not over.
     */
    private int searchForMove() {
        Board work = new Board(getBoard());
        int value;
        assert getSide() == work.whoseMove();
        _foundMove = -1;

        if (getSide() == RED) {
            minMax(work, 1, true, 1,
                    Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        } else {
            minMax(work, 1, true, -1,
                    Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        }
        return _foundMove;
    }

    /**
     * Find a move from position BOARD and return its value, recording
     * the move found in _foundMove iff SAVEMOVE. The move
     * should have maximal value or have value > BETA if SENSE==1,
     * and minimal value or value < ALPHA if SENSE==-1. Searches up to
     * DEPTH levels.  Searching at level 0 simply returns a static estimate
     * of the board value and does not set _foundMove. If the game is over
     * on BOARD, does not set _foundMove.
     */
    private int minMax(Board board, int depth, boolean saveMove,
                       int sense, double alpha, double beta) {
        int currBest = -1;
        if (depth == 0 || board.getWinner() != null) {
            return staticEval(board, board.size() * board.size() + 1);
        }
        ArrayList<Integer> allMoves = new ArrayList<>();
        for (int i = 0; i < board.size() * board.size(); i++) {
            if (board.isLegal(board.whoseMove(), i)) {
                allMoves.add(i);
            }
        }
        if (sense == 1) {
            double maximum = Double.NEGATIVE_INFINITY;
            for (int m : allMoves) {
                Board g = new Board(board);
                g.addSpot(g.whoseMove(), m);
                int eval = minMax(g, depth - 1, false, -1 * sense, alpha, beta);
                maximum = Math.max(maximum, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
                if (maximum == eval) {
                    currBest = m;
                }
            }
            if (!saveMove) {
                return (int) maximum;
            } else {
                _foundMove = currBest;
                return _foundMove;
            }

        } else {
            double minimum = Double.POSITIVE_INFINITY;
            for (int m : allMoves) {
                Board g = new Board(board);
                g.addSpot(g.whoseMove(), m);
                int eval = minMax(g, depth - 1, false, -1 * sense, alpha, beta);
                minimum = Math.min(minimum, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
                if (minimum == eval) {
                    currBest = m;
                }
            }
            if (!saveMove) {
                return (int) minimum;
            } else {
                _foundMove = currBest;
                return _foundMove;
            }
        }
    }

    /**
     * Return a heuristic estimate of the value of board position B.
     * Use WINNINGVALUE to indicate a win for Red and -WINNINGVALUE to
     * indicate a win for Blue.
     */
    private int staticEval(Board b, int winningValue) {
        int blueCount = b.numOfSide(BLUE), redCount = b.numOfSide(RED);
        if (b.getWinner() == BLUE) {
            return -winningValue;
        }
        if (b.getWinner() == RED) {
            return winningValue;
        }
        return blueCount - redCount;
    }
}
