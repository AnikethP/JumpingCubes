package flood;


/**
 * Functions for solving a Flood puzzle.
 *
 * @author Aniketh Prasad
 */
class Solver {
    /**
     * Maximum search depth.
     */
    static final int SEARCH_DEPTH = 1;

    /**
     * Returns an estimate of the number of moves needed to solve the
     * puzzle depicted by MODEL. This estimate is always correct if it is
     * possible to solve the puzzle in SEARCH_DEPTH moves.
     */
    static int movesNeeded(Model model) {
        Model work = new Model(model);
        int num;
        num = 0;
        while (!work.solved()) {
            num += 1;
            work.setActiveRegionColor(chooseBestMove(work));

        }
        return num;
    }

    /**
     * Return a heuristic guess of the best next move (color) for MODEL.
     * Assumes that MODEL does not represent a solved puzzle.  If there is
     * a win requiring SEARCH_DEPTH or fewer moves, returns a move that will
     * lead to that win in the fewest moves.  Always returns a move that
     * increases the size of the active region, and can lead to the maximum
     * possible increase in its size in SEARCH_DEPTH moves.  When there are
     * multiple moves with this property, always chooses the numerically
     * smallest move.
     */
    static int chooseBestMove(Model model) {
        return chooser(model, SEARCH_DEPTH)[0];
    }


    /**
     * Return a pair (color, goodness), where goodness is the
     * maximum size of the active region of MODEL in DEPTH moves, and color
     * is a color that may be used as the first move to get to that size
     * in DEPTH moves.  When DEPTH is 0, color is undefined and goodness is
     * simply the current size of the active region.  Leaves MODEL with
     * the same contents as initially.  Assumes that MODEL is initially
     * unsolved. Always returns a move that increases the size of the active
     * region.  If goodness indicates a win (equals the total number of
     * cells on the board), returns a move that achieves it in the fewest
     * moves (which will necessarily be 1 if DEPTH is 1).
     */
    static int[] chooser(Model model, int depth) {
        Model test = new Model(model);
        int winningScore = test.width() * test.height();
        if (depth == 0) {
            return new int[]{-1, test.activeRegionSize()};
        }
        int bestMove, maxSize;
        bestMove = -1;
        maxSize = -1;
        int numOfColors = test.ncolors();
        for (int i = 0; i < numOfColors; i++) {

            if (test.adjacentCells(i).size() > 0) {

                test.setActiveRegionColor(i);

                if (test.activeRegionSize() > maxSize) {
                    maxSize = test.activeRegionSize();
                    bestMove = i;
                }
                test.undo();
            }
        }
        return new int[]{bestMove, maxSize};
    }

}
