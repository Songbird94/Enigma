package enigma;


/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Xiaoru Zhao
 */
class Alphabet {

    /** A char list of alphabet. */
    private char[] _char;

    /** A new alphabet containing CHARS. The K-th character has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _char = new char [chars.length()];
        for (int i = 0; i < chars.length(); i += 1) {
            _char[i] = chars.charAt(i);
        }
    }

    /** A new alphabet containing CHARS. */
    Alphabet(char[] chars) {
        _char = chars;
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _char.length;
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        for (int i = 0; i < _char.length; i += 1) {
            if (_char[i] == ch) {
                return true;
            }
        }
        return false;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return _char[index];
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        for (int i = 0; i < _char.length; i += 1) {
            if (_char[i] == ch) {
                return i;
            }
        }
        return 0;
    }

}
