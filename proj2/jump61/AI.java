package jump61;


import java.util.ArrayList;
import java.util.Random;

import static jump61.Side.*;

/** An automated Player.
 *  @author P. N. Hilfinger
 */
class AI extends Player {

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
            value = minMax(work, 1, true,
                    1, Integer.MIN_VALUE, Integer.MAX_VALUE);
        } else {
            value = minMax(work, 1, true,
                    -1, Integer.MIN_VALUE, Integer.MAX_VALUE);
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
                       int sense, int alpha, int beta) {
        if (depth == 0 || board.getWinner() != null) {
            return staticEval(board, Integer.MAX_VALUE);
        }
        ArrayList<ArrayList> validMoves = board.getValidMoves(getSide());
        if (sense == 1) {
            int maxeval = Integer.MIN_VALUE;
            for (int i = 0; i < validMoves.size(); i++) {
                int eval = minMax((Board) validMoves.get(i).get(1),
                        depth - 1, false, -1, alpha, beta);
                maxeval = max(eval, maxeval);
                if (eval == max(eval, maxeval) && saveMove) {
                    _foundMove = (int) validMoves.get(i).get(0);
                }
                alpha = max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxeval;
        }

        if (sense == -1) {
            int mineval = Integer.MAX_VALUE;
            for (int i = 0; i < validMoves.size(); i++) {
                int eval = minMax((Board) validMoves.get(i).get(1),
                        depth - 1, false, 1, alpha, beta);
                mineval = min(eval, mineval);
                if (eval == min(eval, mineval) && saveMove) {
                    _foundMove = (int) validMoves.get(i).get(0);
                }
                beta = min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return mineval;
        }
        return 0;
    }

    /**
     * maximum of two inputs.
     *
     * @param a and
     * @param b are ints to be compared
     * @return maxmimum value
     */
    private int max(int a, int b) {
        if (a >= b) {
            return a;
        }
        return b;
    }

    /**
     * minimum of two inputs.
     *
     * @param a and
     * @param b are ints to be compared
     * @return minimum value
     */
    private int min(int a, int b) {
        if (a <= b) {
            return a;
        }
        return b;
    }

    /**
     * Return a heuristic estimate of the value of board position B.
     * Use WINNINGVALUE to indicate a win for Red and -WINNINGVALUE to
     * indicate a win for Blue.
     */
    private int staticEval(Board b, int winningValue) {
        if (b.getWinner() == BLUE) {
            return -winningValue;
        } else if (b.getWinner() == RED) {
            return winningValue;
        }
        Side mySide = getSide();
        Side opponent = mySide.opposite();

        int mySquares = b.getSidesquares(mySide);
        int oppSquares = b.getSidesquares(opponent);

        return mySquares - oppSquares;
    }

    /**
     * A random-number generator used for move selection.
     */
    private Random _random;

    /**
     * Used to convey moves discovered by minMax.
     */
    private int _foundMove;
}