package enigma;

import static enigma.EnigmaException.*;

/**
 * Class that represents a reflector in the enigma.
 *
 * @author Aniketh Prasad
 */
class Reflector extends FixedRotor {

    /**
     * A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM.
     */
    Reflector(String name, Permutation perm) {
        super(name, perm);
    }

    @Override
    void set(int posn) {
        if (posn != 0) {
            throw error("reflector has only one position");
        }
    }

    @Override
    public String toString() {
        return "Reflector " + name();
    }

    @Override
    int convertForward(int p) {
        int convert = permutation().permute(p);
        return permutation().wrap(convert);
    }

    @Override
    int setting() {
        return 0;
    }
    @Override
    int convertBackward(int e) {
        int convert = permutation().invert(e);
        return permutation().wrap(convert);

    }
}
