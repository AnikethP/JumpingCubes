package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void testSize() {
        Alphabet a = new Alphabet("");
        Alphabet b = new Alphabet("ABCDEFG");
        Permutation p = new Permutation("(BACDEFG)", b);
        Permutation s = new Permutation("", a);
        assertEquals(7, p.size());
        assertEquals(0, s.size());
    }

    @Test
    public void testPermute() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        /* TODO: Add additional assert statements here! */
        assertEquals('D', p.permute('C'));
        assertEquals(3, p.permute(2));
        assertEquals('B', p.permute('D'));
        assertEquals(1, p.permute(3));
        //checkPerm("Test1:", "ABCD", "BACD", p, new Alphabet("ABCD"));

        Permutation x = new Permutation("(BACD)", new Alphabet("ABCDEF"));
        assertEquals('E', x.permute('E'));
        assertEquals(4, x.permute(4));
    }

    @Test
    public void testDerangement() {
        Permutation p = new Permutation("(BACD) (E)", new Alphabet("ABCDEF") );
        assertFalse(p.derangement());

        Permutation l = new Permutation("(BACD)", new Alphabet("ABCDEF") );
        assertFalse(l.derangement());

        Permutation y = new Permutation("(DCBA)", new Alphabet("ABCD"));
        assertTrue(y.derangement());
    }

    @Test
    public void testAlphabet() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCDEF"));
        Alphabet a = new Alphabet("ABCDEF");

        for(int i = 0; i < 6; i++){
            assertEquals(a.toChar(i), p.alphabet().toChar(i));
        }

    }

    @Test(expected = EnigmaException.class)
    public void testNotInAlphabet() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        p.invert('F');

    }
    @Test(expected = EnigmaException.class)
    public void testInvalidPermutation() {
        Permutation x = new Permutation("(BACDB)", new Alphabet("ABCD"));
        x.invert('B');
    }

}
