package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Aniketh Prasad
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
        // FIXME
    }

    // FIXME?
    @Override
    boolean rotates(){
        return true;
    }
    @Override
    void advance() {
        // FIXME
        set((setting() + 1) % alphabet().size());
    }
    @Override
    boolean atNotch(){
        for(int i = 0; i < _notches.length(); i++)
        {
            if(alphabet().toInt(_notches.charAt(i)) == setting())
            {
                return true;
            }
        }
        return false;
    }
    @Override
    public String toString() {
        return "MovingRotor " + name();
    }
    private String _notches;
    // FIXME: ADDITIONAL FIELDS HERE, AS NEEDED

}
