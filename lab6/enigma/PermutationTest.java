package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class. For the purposes of
 * this lab (in order to test) this is an abstract class, but in proj1, it will
 * be a concrete class. If you want to copy your tests for proj1, you can make
 * this class concrete by removing the 4 abstract keywords and implementing the
 * 3 abstract methods.
 *
 *  @author Aniketh Prasad
 *  */
public abstract class PermutationTest {

    /**
     * For this lab, you must use this to get a new Permutation,
     * the equivalent to:
     * new Permutation(cycles, alphabet)
     * @return a Permutation with cycles as its cycles and alphabet as
     * its alphabet
     * @see Permutation for description of the Permutation conctructor
     */
    abstract Permutation getNewPermutation(String cycles, Alphabet alphabet);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet(chars)
     * @return an Alphabet with chars as its characters
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet(String chars);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet()
     * @return a default Alphabet with characters ABCD...Z
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet();

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /** Check that PERM has an ALPHABET whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha,
                           Permutation perm, Alphabet alpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.toInt(c), ei = alpha.toInt(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    private void checkNoRepeats()
    {

    }
    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        Alphabet alpha = getNewAlphabet();
        Permutation perm = getNewPermutation("", alpha);
        checkPerm("identity", UPPER_STRING, UPPER_STRING, perm, alpha);
    }


    // FIXME: Add tests here that pass on a correct Permutation and fail on buggy Permutations.
    @Test
    public void testInvertChar() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        /* TODO: Add additional assert statements here! */
        assertEquals('B', p.invert('A'));
        assertEquals(1, p.invert(0));
        assertEquals('D', p.invert('B'));
        assertEquals(3, p.invert(1));
        checkPerm("Test1:", "ABCD", "CADB", p, p.alphabet());

        Permutation x = getNewPermutation("(BACD)", getNewAlphabet("ABCDEF"));
        assertEquals('E', x.invert('E'));
        assertEquals(4, x.invert(4));
    }

    @Test
    public void testSize() {
        Alphabet a = getNewAlphabet("");
        Alphabet b = getNewAlphabet("ABCDEFG");
        Permutation p = getNewPermutation("(BACDEFG)", b);
        Permutation s = getNewPermutation("", a);
        assertEquals(7, p.size());
        assertEquals(0, s.size());
    }

    @Test
    public void testPermute() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        /* TODO: Add additional assert statements here! */
        assertEquals('D', p.permute('C'));
        assertEquals(3, p.permute(2));
        assertEquals('B', p.permute('D'));
        assertEquals(1, p.permute(3));
        //checkPerm("Test1:", "ABCD", "BACD", p, getNewAlphabet("ABCD"));

        Permutation x = getNewPermutation("(BACD)", getNewAlphabet("ABCDEF"));
        assertEquals('E', x.permute('E'));
        assertEquals(4, x.permute(4));
    }

    @Test
    public void testDerangement() {
        Permutation p = getNewPermutation("(BACD) (E)", getNewAlphabet("ABCDEF") );
        assertFalse(p.derangement());

        Permutation l = getNewPermutation("(BACD)", getNewAlphabet("ABCDEF") );
        assertFalse(l.derangement());

        Permutation y = getNewPermutation("(DCBA)", getNewAlphabet("ABCD"));
        assertTrue(y.derangement());
    }

    @Test
    public void testAlphabet() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCDEF"));
        Alphabet a = getNewAlphabet("ABCDEF");

        for(int i = 0; i < 6; i++){
            assertEquals(a.toChar(i), p.alphabet().toChar(i));
        }

    }

    @Test(expected = EnigmaException.class)
    public void testNotInAlphabet() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        p.invert('F');

    }
    @Test(expected = EnigmaException.class)
    public void testInvalidPermutation() {
        Permutation x = getNewPermutation("(BACDB)", getNewAlphabet("ABCD"));
        x.invert('B');
    }

}
