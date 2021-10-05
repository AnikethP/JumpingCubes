package enigma;

import java.util.HashMap;
import java.util.Collection;
import java.util.LinkedList;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _allRotors = (LinkedList<Rotor>) allRotors;
        _numRotors = numRotors;
        _pawls = pawls;
        // FIXME
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors; // FIXME
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        // FIXME
        _machine = new LinkedList<Rotor>();
        for(String name : rotors)
        {
            for(Rotor k : _allRotors)
            {
                if(k.name().equals(name))
                {
                    _machine.add(k);
                }
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        // FIXME
        for(int i = 1; i < _machine.size(); i++)
        {

            _machine.get(i).set(setting.charAt(i-1));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        // FIXME
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        //System.out.println(_machine.size());
        boolean[] move = new boolean[_machine.size()]; //which rotors will be moved
        move[_machine.size()-1] = true; // move the fast rotor
        for(int i = _machine.size()-1; i > 1; i--){ //Check for notches
            if(_machine.get(i).atNotch()){
                move[i-1] = true;
                move[i] = true;
            }


        }

        for(int i = 0; i < move.length; i++){//advance all the rotors
            if (move[i]){
                _machine.get(i).advance();
            }
        }
        //plugboard conversion
        c = _plugboard.permute(c); //plugboard permutation in
        for(int i = _machine.size()-1; i >= 0; i--) //convert all forward
        {
            c = _machine.get(i).convertForward(c);
        }

        for(int i = 1; i < _machine.size(); i++){ //convert all backward
            c = _machine.get(i).convertBackward(c);
        }
        c = _plugboard.permute(c); // plugboard permutation out
        return c;
        // FIXME
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String returnStr = "";
        char[] chars = msg.toCharArray();
        for(char k : chars){
            if(Character.isLetter(k)){
                returnStr+= _alphabet.toChar(convert(_alphabet.toInt(k)));
            }

        }

        return returnStr;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    private LinkedList<Rotor> _allRotors;
    private LinkedList<Rotor> _machine = new LinkedList<Rotor>();
    private int _numRotors;
    private int _pawls;
    private Permutation _plugboard;
    // FIXME: ADDITIONAL FIELDS HERE, IF NEEDED.
}
