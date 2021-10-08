package enigma;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

import static enigma.EnigmaException.*;

/**
 * Class that represents a complete enigma machine.
 *
 * @author Aniketh Prasad
 */
class Machine {

    /**
     * A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     * and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     * available rotors.
     */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _allRotors = (LinkedList<Rotor>) allRotors;
        _numRotors = numRotors;
        _pawls = pawls;
    }

    /**
     * Return the number of rotor slots I have.
     */
    int numRotors() {
        return _numRotors;
    }

    /**
     * Return the number pawls (and thus rotating rotors) I have.
     */
    int numPawls() {
        return _pawls;
    }

    /**
     * Set my rotor slots to the rotors named ROTORS from my set of
     * available rotors (ROTORS[0] names the reflector).
     * Initially, all rotors are set at their 0 setting.
     */
    void insertRotors(String[] rotors) throws EnigmaException {
        _machine = new LinkedList<Rotor>();
        int count = 0;
        if (isDuplicate(rotors)) {
            throw new EnigmaException("Duplicate rotors");
        }
        for (String name : rotors) {
            if (!isRotor(name)) {
                throw new EnigmaException("Invalid Rotor");
            }
            for (Rotor k : _allRotors) {
                if (k.name().equals(name)) {
                    if (count == 0 && !k.toString().contains("Reflector")) {
                        throw new EnigmaException("First rotor not reflector");
                    }
                    _machine.add(k);
                    count += 1;
                }
            }
        }
    }

    /**
     * @return if duplicate strings in names.
     * @param names a string array of names of possible rotors.
     */
    private boolean isDuplicate(String[] names) {
        List<String> lst = Arrays.asList(names);
        Set<String> set = new HashSet<String>(lst);
        return lst.size() != set.size();
    }

    /**
     * @return if name is a valid rotor.
     * @param name a String describing the name of a possible rotor.
     */
    boolean isRotor(String name) {
        for (Rotor k : _allRotors) {
            if (k.name().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Set my rotors according to SETTING, which must be a string of
     * numRotors()-1 characters in my alphabet. The first letter refers
     * to the leftmost rotor setting (not counting the reflector).
     */
    void setRotors(String setting) {
        for (int i = 1; i < _machine.size(); i++) {

            _machine.get(i).set(setting.charAt(i - 1));
        }
    }

    /**
     * Set the ringstelleung setting.
     * @param setting String representing the ring settings for all rotors.
     */
    void setRing(String setting) {
        for (int i = 1; i < _machine.size(); i++) {
            _machine.get(i).setRing(setting.charAt(i - 1));
        }
    }

    /**
     * Set the plugboard to PLUGBOARD.
     */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /**
     * Returns the result of converting the input character C (as an
     * index in the range 0..alphabet size - 1), after first advancing
     * the machine.
     */
    int convert(int c) {
        boolean[] move = new boolean[_machine.size()];
        move[_machine.size() - 1] = true;
        for (int i = _machine.size() - 1; i > 1; i--) {
            if (_machine.get(i).atNotch()) {
                move[i - 1] = true;
                if (_machine.get(i - 1).rotates()) {
                    move[i] = true;
                }
            }
        }

        for (int i = 0; i < move.length; i++) {
            if (move[i]) {
                _machine.get(i).advance();
            }
        }

        c = _plugboard.permute(c);

        for (int i = _machine.size() - 1; i >= 0; i--) {
            c = _machine.get(i).convertForward(c);
        }
        for (int i = 1; i < _machine.size(); i++) {
            c = _machine.get(i).convertBackward(c);
        }
        c = _plugboard.permute(c);
        return c;
    }

    /**
     * Returns the encoding/decoding of MSG, updating the state of
     * the rotors accordingly.
     */
    String convert(String msg) {
        String returnStr = "";
        char[] chars = msg.toCharArray();
        for (char k : chars) {
            if (_alphabet.contains(k)) {
                returnStr += _alphabet.toChar(convert(_alphabet.toInt(k)));
            } else if (k != ' ') {
                throw new EnigmaException("Invalid alphabet");
            }

        }

        return returnStr;
    }

    /**
     * Common alphabet of my rotors.
     */
    private final Alphabet _alphabet;

    /**
     * All usable rotors.
     */
    private LinkedList<Rotor> _allRotors;

    /**
     * Rotors being used in the machine.
     */
    private LinkedList<Rotor> _machine = new LinkedList<Rotor>();

    /**
     * Total number of Rotors.
     */
    private int _numRotors;

    /**
     * Total number of pawls.
     */
    private int _pawls;

    /**
     * Plugboard permutation.
     */
    private Permutation _plugboard;

    /**
     * Ringstellung settings.
     */
}
