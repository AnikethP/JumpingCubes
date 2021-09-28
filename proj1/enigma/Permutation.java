package enigma;

import static enigma.EnigmaException.*;
import java.util.ArrayList;
/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Aniketh Prasad
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        //char[] cyclesChar = cycles.toCharArray();
        //"(ABCD) (FG) (FH)"
        addCycle(cycles);
        // FIXME
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        String curr = "";
        for(int i = 0; i < cycle.length(); i++) {
            if(cycle.charAt(i) == ')')
            {
                _cycles.add(curr);
                curr = "";
            }
            else if(Character.isDigit(cycle.charAt(i)) || Character.isLetter(cycle.charAt(i)))
            {
                curr += cycle.charAt(i);
            }
        }
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size(); // FIXME
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int pMod = wrap(p);
        char x = _alphabet.toChar(p);

        for(String s : _cycles)
        {
            for(int i = 0; i < s.length(); i++)
            {
                if(s.charAt(i) == x)
                {
                    if(i == s.length()-1)
                    {
                        return _alphabet.toInt(s.charAt(0));
                    }
                    else
                    {
                        return _alphabet.toInt(s.charAt(i+1));
                    }
                }
            }
        }
        return p;
        // FIXME

    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        return 0;  // FIXME
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return 0;  // FIXME
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        return 0;  // FIXME
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        return true;  // FIXME
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    private ArrayList<String> _cycles = new ArrayList<String>();
    // FIXME: ADDITIONAL FIELDS HERE, AS NEEDED
}
