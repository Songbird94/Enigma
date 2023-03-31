package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author Xiaoru Zhao
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        _name = name;
        _permutation = perm;
        _setting = 0;
    }

    @Override
    boolean reflecting() {
        return true;
    }

    @Override
    void set(int posn) {
        if (posn != 0) {
            throw error("reflector has only one position");
        }
    }

    @Override
    void set(char cposn) {
        if (_permutation.alphabet().toInt(cposn) != 0) {
            throw error("reflector has only one position");
        }
    }

    /** Name of reflector. */
    private final String _name;

    /** Permutation of reflector. */
    private Permutation _permutation;

    /** Setting of reflector. */
    private int _setting;

}
