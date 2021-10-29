package jump61;

import ucb.gui2.LayoutSpec;
import ucb.gui2.TopLevel;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * The GUI controller for jump61.  To require minimal change to textual
 * interface, we adopt the strategy of converting GUI input (mouse clicks)
 * into textual commands that are sent to the Game object through a
 * a Writer.  The Game object need never know where its input is coming from.
 * A Display is an Observer of Games and Boards so that it is notified when
 * either changes.
 *
 * @author Aniketh Prasad
 */
class Display extends TopLevel implements View, CommandSource, Reporter {

    /**
     * Time interval in msec to wait after a board update.
     */
    static final long BOARD_UPDATE_INTERVAL = 50;
    /**
     * Queue for commands going to the controlling Game.
     */
    private final ArrayBlockingQueue<String> _commandQueue =
            new ArrayBlockingQueue<>(5);
    /**
     * The widget that displays the actual playing board.
     */
    private final BoardWidget _boardWidget;

    /**
     * A new window with given TITLE displaying GAME, and using COMMANDWRITER
     * to send commands to the current game. FIXME
     */
    Display(String title) {
        super(title, true);

        addMenuButton("Game->Quit", this::quit);
        addMenuButton("Game->New Game", this::newGame);
        addMenuButton("Switch Player->AI Red", this::redAI);
        addMenuButton("Switch Player->AI Blue", this::blueAI);
        addMenuButton("Switch Player->Human Red", this::redPerson);
        addMenuButton("Switch Player->Human Blue", this::bluePerson);
        addMenuButton("Select Size->2", this::resize);
        addMenuButton("Select Size->3", this::resize);
        addMenuButton("Select Size->4", this::resize);
        addMenuButton("Select Size->5", this::resize);
        addMenuButton("Select Size->6", this::resize);
        addMenuButton("Select Size->7", this::resize);
        addMenuButton("Select Size->8", this::resize);
        addMenuButton("Select Size->9", this::resize);
        addMenuButton("Select Size->10", this::resize);

        _boardWidget = new BoardWidget(_commandQueue);
        add(_boardWidget, new LayoutSpec("y", 1, "width", 2));
        display(true);
    }
    /**
     * Response to "resize" button click.
     */
    void resize(String dummy) {
        String numberOnly = dummy.replaceAll("[^0-9]", "");
        _commandQueue.offer("size " + numberOnly);
    }
    /**
     * Response to "change player" button click.
     */
    void redAI(String dummy) {
        _commandQueue.offer("auto red");
    }
    /**
     * Response to "change player" button click.
     */
    void blueAI(String dummy) {
        _commandQueue.offer("auto blue");
    }
    /**
     * Response to "change player" button click.
     */
    void redPerson(String dummy) {
        _commandQueue.offer("manual red");
    }
    /**
     * Response to "change player" button click.
     */
    void bluePerson(String dummy) {
        _commandQueue.offer("manual blue");
    }

    /**
     * Response to "Quit" button click.
     */
    void quit(String dummy) {
        System.exit(0);
    }

    /**
     * Response to "New Game" button click. FIXME
     */
    void newGame(String dummy) {
        _commandQueue.offer("new");
        _boardWidget.update(new Board(6));
    }

    /**
     * FIXME.
     */
    @Override
    public void update(Board board) {
        _boardWidget.update(board);
        pack();
        _boardWidget.repaint();
    }

    @Override
    public String getCommand(String ignored) {
        try {
            return _commandQueue.take();
        } catch (InterruptedException excp) {
            throw new Error("unexpected interrupt");
        }
    }

    @Override
    public void announceWin(Side side) {
        showMessage(String.format("%s wins!", side.toCapitalizedString()),
                "Game Over", "information");
    }

    @Override
    public void announceMove(int row, int col) {
    }

    @Override
    public void msg(String format, Object... args) {
        showMessage(String.format(format, args), "", "information");
    }

    @Override
    public void err(String format, Object... args) {
        showMessage(String.format(format, args), "Error", "error");
    }
}
