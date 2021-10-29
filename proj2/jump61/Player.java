package jump61;

/**
 * Represents one player in a game of Jump61.  At any given time, each
 * Player is attached to a Game and has a Side.  Each call of getMove()
 * returns a valid move.
 *
 * @author Paul N. Hilfinger
 */
abstract class Player {

    /**
     * The game I'm in.
     */
    private final Game _game;
    /**
     * My current color.
     */
    private final Side _color;

    /**
     * A Player in GAME, initially playing COLOR.
     */
    Player(Game game, Side color) {
        _game = game;
        _color = color;
    }

    /**
     * Return the color I am currently playing.
     */
    final Side getSide() {
        return _color;
    }

    /**
     * Return the Game I am currently playing in.
     */
    final Game getGame() {
        return _game;
    }

    /**
     * Return the Board containing the current position
     * (read-only).
     */
    final Board getBoard() {
        return _game.getBoard();
    }

    /**
     * Return my next move, or a command.  Assumes that I am of the
     * proper color and that the game is not yet won.
     */
    abstract String getMove();

}
