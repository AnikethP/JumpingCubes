package enigma;

import static enigma.EnigmaException.*;

/**
 * Class that represents a rotor that has no ratchet and does not advance.
 *
 * @author Aniketh Prasad
 */
class FixedRotor extends Rotor {

    /**
     * A non-moving rotor named NAME whose permutation at the 0 setting
     * is given by PERM.
     */
    FixedRotor(String name, Permutation perm) {
        super(name, perm);
    }

    @Override
    int setting() {
        return 0;
    }

    @Override
    public String toString() {
        return "FixedRotor " + name();
    }
}
