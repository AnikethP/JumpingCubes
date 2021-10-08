package enigma;

import static enigma.EnigmaException.*;

/**
 * Superclass that represents a rotor in the enigma machine.
 *
 * @author Aniketh Prasad
 */
class Rotor {

    /**
     * A rotor named NAME whose permutation is given by PERM.
     */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _position = 0;
    }

    /**
     * Return my name.
     */
    String name() {
        return _name;
    }

    /**
     * Return my alphabet.
     */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /**
     * Return my permutation.
     */
    Permutation permutation() {
        return _permutation;
    }

    /**
     * Return the size of my alphabet.
     */
    int size() {
        return _permutation.size();
    }

    /**
     * Return true iff I have a ratchet and can move.
     */
    boolean rotates() {
        return false;
    }

    /**
     * Return true iff I reflect.
     */
    boolean reflecting() {
        return false;
    }

    /**
     * Return my current setting.
     */
    int setting() {
        return _position;
    }
    /**
     * Returns ring setting.
     */
    int ring() {
        return _ring;
    }

    /**
     * Set setting() to POSN.
     */
    void set(int posn) {
        _position = posn;
    }

    /**
     * Set ringstellung setting.
     * @param k char for ring setting of the rotor
     */
    void setRing(char k) {
        _ring = _permutation.alphabet().toInt(k);
    }

    /**
     * Set setting() to character CPOSN.
     */
    void set(char cposn) {
        set(_permutation.alphabet().toInt(cposn));
    }

    /**
     * Return the conversion of P (an integer in the range 0..size()-1)
     * according to my permutation.
     */
    int convertForward(int p) {
        int in = _permutation.wrap(p + setting() - ring());
        int convert = _permutation.permute(in);
        int out = convert - setting() + ring();
        return _permutation.wrap(out);
    }

    /**
     * Return the conversion of E (an integer in the range 0..size()-1)
     * according to the inverse of my permutation.
     */
    int convertBackward(int e) {
        int in = _permutation.wrap(e + setting() - ring());
        int convert = _permutation.invert(in);
        int out = convert - setting() + ring();
        return _permutation.wrap(out);
    }

    /**
     * Returns true iff I am positioned to allow the rotor to my left
     * to advance.
     */
    boolean atNotch() {
        return false;
    }

    /**
     * Advance me one position, if possible. By default, does nothing.
     */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /**
     * My name.
     */
    private final String _name;

    /**
     * The permutation implemented by this rotor in its 0 position.
     */
    private Permutation _permutation;

    /**
     * Current position of the rotor.
     */
    private int _position;
    /**
     * Current position of the ring.
     */
    private int _ring = 0;
}
