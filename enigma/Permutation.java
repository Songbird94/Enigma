package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Xiaoru Zhao
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycle = cycles;
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    void addCycle(String cycle) {
        _cycle = _cycle + cycle;
    }

    /** Return the cycle of permutation. */
    String getCycle() {
        return _cycle;
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
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        for (int k = 0; k < _cycle.length(); k += 1) {
            if (_cycle.charAt(k) == _alphabet.toChar(p)) {
                if (')' == _cycle.charAt(k + 1)) {
                    for (int i = k; i >= 0; i -= 1) {
                        if ('(' == _cycle.charAt(i)) {
                            return _alphabet.toInt(_cycle.charAt(i + 1));
                        }
                    }
                } else {
                    return _alphabet.toInt(_cycle.charAt(k + 1));
                }
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        for (int k = 0; k < _cycle.length(); k += 1) {
            if (_cycle.charAt(k) == _alphabet.toChar(c)) {
                if ('(' == _cycle.charAt(k - 1)) {
                    for (int i = k; i < _cycle.length(); i += 1) {
                        if (')' == _cycle.charAt(i)) {
                            return _alphabet.toInt(_cycle.charAt(i - 1));
                        }
                    }
                } else {
                    return _alphabet.toInt(_cycle.charAt(k - 1));
                }
            }
        }
        return c;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int index = permute(_alphabet.toInt(p));
        return _alphabet.toChar(index);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        int index = invert(_alphabet.toInt(c));
        return _alphabet.toChar(index);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Change the alphabet to ALPHA. */
    void changeAlphabet(Alphabet alpha) {
        _alphabet = alpha;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < _alphabet.size(); i += 1) {
            if (permute(i) == i) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Cycle of this permutation. */
    private String _cycle;
}
