package jump61;

import ucb.gui2.Pad;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.concurrent.ArrayBlockingQueue;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.BasicStroke;

import static jump61.Side.BLUE;
import static jump61.Side.RED;

/**
 * A GUI component that displays a Jump61 board, and converts mouse clicks
 * on that board to commands that are sent to the current Game.
 *
 * @author Aniketh Prasad
 */
class BoardWidget extends Pad {

    /**
     * Length of the side of one square in pixels.
     */
    private static final int SQUARE_SIZE = 50;
    /**
     * Width and height of a spot.
     */
    private static final int SPOT_DIM = 8;
    /**
     * Minimum separation of center of a spot from a side of a square.
     */
    private static final int SPOT_MARGIN = 10;
    /**
     * Width of the bars separating squares in pixels.
     */
    private static final int SEPARATOR_SIZE = 3;
    /**
     * Width of square plus one separator.
     */
    private static final int SQUARE_SEP = SQUARE_SIZE + SEPARATOR_SIZE;

    /**
     * Colors of various parts of the displayed board.
     */
    private static final Color
            NEUTRAL = Color.WHITE,
            SEPARATOR_COLOR = Color.BLACK,
            SPOT_COLOR = Color.BLACK,
            RED_TINT = new Color(255, 200, 200),
            BLUE_TINT = new Color(200, 200, 255);
    /**
     * The Board I am displaying.
     */
    private Board _board;

    /* .update and .paintComponent are synchronized because they are called
     *  by three different threads (the main thread, the thread that
     *  responds to events, and the display thread).  We don't want the
     *  saved copy of our Board to change while it is being displayed. */
    /**
     * Dimension in pixels of one side of the board.
     */
    private int _side;
    /**
     * Destination for commands derived from mouse clicks.
     */
    private final ArrayBlockingQueue<String> _commandQueue;

    /**
     * A new BoardWidget that monitors and displays a game Board, and
     * converts mouse clicks to commands to COMMANDQUEUE.
     */
    BoardWidget(ArrayBlockingQueue<String> commandQueue) {
        _commandQueue = commandQueue;
        _side = 6 * SQUARE_SEP + SEPARATOR_SIZE;
        setMouseHandler("click", this::doClick);
    }

    /**
     * Update my display to show BOARD.  Here, we save a copy of
     * BOARD (so that we can deal with changes to it only when we are ready
     * for them), and recompute the size of the displayed board.
     */
    synchronized void update(Board board) {
        if (board.equals(_board)) {
            return;
        }
        if (_board != null && _board.size() != board.size()) {
            invalidate();
        }
        _board = new Board(board);
        _side = _board.size() * SQUARE_SEP + SEPARATOR_SIZE;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(_side, _side);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(_side, _side);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(_side, _side);
    }

    /**
     * FIXME.
     */
    @Override
    public synchronized void paintComponent(Graphics2D g) {
        if (_board == null) {
            return;
        }
        super.paintComponent(g);
        g.setColor(SEPARATOR_COLOR);
        g.setStroke(new BasicStroke(SEPARATOR_SIZE));
        int x = 2;
        int y = 2;
        for (int i = 0; i < _board.size(); i++) {
            for (int j = 0; j < _board.size(); j++) {
                if (_board.get(x / SQUARE_SIZE + 1, y / SQUARE_SIZE + 1)
                        .getSide() == RED) {
                    g.setColor(RED_TINT);
                } else if (_board.get(x / SQUARE_SIZE + 1, y / SQUARE_SIZE + 1)
                        .getSide() == BLUE) {
                    g.setColor(BLUE_TINT);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(x, y, SQUARE_SIZE, SQUARE_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, SQUARE_SIZE, SQUARE_SIZE);
                displaySpots(g, x + SQUARE_SIZE / 2, y + SQUARE_SIZE / 2);

                x += SQUARE_SIZE;
            }
            x = 2;
            y += SQUARE_SIZE;
        }

    }

    /**
     * Color and display the spots on the square at row R and column C
     * on G.  (Used by paintComponent). FIXME
     */
    private void displaySpots(Graphics2D g, int r, int c) {


        int numSpots = _board.get(r / SQUARE_SIZE + 1, c / SQUARE_SIZE + 1)
                .getSpots();
        switch (numSpots) {
        case 1:
            spot(g, r, c);
            break;
        case 2:
            spot(g, r - 12, c - 12);
            spot(g, r + 12, c + 12);
            break;
        case 3:
            spot(g, r, c);
            spot(g, r + 12, c + 12);
            spot(g, r - 12, c - 12);
            break;
        case 4:
            spot(g, r + 12, c + 12);
            spot(g, r - 12, c + 12);
            spot(g, r + 12, c - 12);
            spot(g, r - 12, c - 12);
            break;
        case 5:
            spot(g, r, c);
            spot(g, r + 12, c + 12);
            spot(g, r - 12, c + 12);
            spot(g, r + 12, c - 12);
            spot(g, r - 12, c - 12);
            break;
        default:
            break;

        }


    }

    /**
     * Draw one spot centered at position (X, Y) on G.
     */
    private void spot(Graphics2D g, int x, int y) {
        g.setColor(SPOT_COLOR);
        g.fillOval(x - SPOT_DIM / 2, y - SPOT_DIM / 2, SPOT_DIM, SPOT_DIM);
    }

    /**
     * Respond to the mouse click depicted by EVENT. FIXME
     */
    public void doClick(String dummy, MouseEvent event) {
        if (_board.getWinner() != null) {
            _commandQueue.offer("new");
        } else {
            int x = event.getX() - SEPARATOR_SIZE,
                    y = event.getY() - SEPARATOR_SIZE;
            int r = x / SQUARE_SIZE + 1;
            int c = y / SQUARE_SIZE + 1;
            _commandQueue.offer(String.format("%d %d", r, c));
        }
    }
}
