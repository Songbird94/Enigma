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

    /**
     * Testing time limit.
     */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;
    private Alphabet alphabet = new Alphabet();

    /**
     * Check that perm has an alphabet whose size is that of
     * FROMALPHA and TOALPHA and that maps each character of
     * FROMALPHA to the corresponding character of FROMALPHA, and
     * vice-versa. TESTID is used in error messages.
     */
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

    /*@Test
    public void testPermute() {
        Alphabet alpha = new Alphabet("ABCDEFGH");
        Permutation perm = new Permutation("(ABCD) (EF)(G)", alpha);
        assertEquals(perm.permute(0), 1);
        assertEquals(perm.permute(3), 0);
        assertEquals(perm.permute(4), 5);
        assertEquals(perm.permute(5), 4);
        assertEquals(perm.permute(6), 6);
        assertEquals(perm.permute(7), 7);
        assertEquals(perm.permute('G'), 'G');
        assertEquals(perm.permute('H'), 'H');
        assertEquals(perm.permute('F'), 'E');
        assertEquals(perm.permute('A'), 'B');
        assertEquals(perm.permute('D'), 'A');
        assertFalse(perm.derangement());
        assertEquals(perm.size(), 8);
        assertEquals(perm.alphabet(), alpha);

    }

    @Test
    public void testInvert() {
        Alphabet alpha = new Alphabet("ABCDEFGH");
        Permutation perm = new Permutation("(ABCD) (EF) (G)", alpha);
        assertEquals(perm.invert(0), 3);
        assertEquals(perm.invert(3), 2);
        assertEquals(perm.invert(4), 5);
        assertEquals(perm.invert(5), 4);
        assertEquals(perm.invert(6), 6);
        assertEquals(perm.invert(7), 7);
        assertEquals(perm.invert('B'), 'A');
        assertEquals(perm.invert('G'), 'G');
        assertEquals(perm.invert('H'), 'H');
        assertEquals(perm.invert('A'), 'D');
        assertEquals(perm.invert('E'), 'F');
        assertEquals(perm.alphabet(), alpha);
        assertEquals(perm.size(), 8);
        assertFalse(perm.derangement());

    }*/
}

