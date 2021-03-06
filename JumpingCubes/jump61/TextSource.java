package jump61;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A CommandSource that takes commands from a Reader.
 *
 * @author P. N. Hilfinger
 */
class TextSource implements CommandSource {

    /**
     * Source of command input.
     */
    private Scanner _inp;
    /**
     * Readers to use after the first.
     */
    private final ArrayList<Reader> _readers;

    /**
     * A source of commands read from the concatenation of the content of
     * READERS.
     */
    TextSource(List<Reader> readers) {
        if (readers.isEmpty()) {
            throw new IllegalArgumentException("must be at least one reader");
        }
        _readers = new ArrayList<>(readers);
        _inp = new Scanner(readers.remove(0));
    }

    @Override
    public String getCommand(String prompt) {
        if (prompt != null) {
            System.out.print(prompt);
            System.out.flush();
        }
        if (_inp.hasNextLine()) {
            return _inp.nextLine();
        } else if (!_readers.isEmpty()) {
            _inp = new Scanner(_readers.remove(0));
            return getCommand(prompt);
        } else {
            return null;
        }
    }
}
