package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotor that has no ratchet and does not advance.
 *  @author Xiaoru Zhao
 */
class FixedRotor extends Rotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is given by PERM. */
    FixedRotor(String name, Permutation perm) {
        super(name, perm);
        _name = name;
        _permutation = perm;
        _setting = 0;
    }

    @Override
    boolean rotates() {
        return false;
    }

    @Override
    int convertForward(int p) {
        return _permutation.wrap(_permutation.permute(
                _permutation.wrap(p + _setting)) - _setting);
    }

    @Override
    int convertBackward(int e) {
        return _permutation.wrap(_permutation.invert(
                _permutation.wrap(e + _setting)) - _setting);
    }

    @Override
    void set(int posn) {
        _setting = posn;
    }

    @Override
    void set(char cposn) {
        _setting = alphabet().toInt(cposn);
    }

    @Override
    void setA(Alphabet alpha) {
        _permutation.changeAlphabet(alpha);
    }


    /** Name of the fixed rotor. */
    private final String _name;

    /** Permutation of fixed rotor. */
    private Permutation _permutation;

    /** Setting of fixed rotor. */
    private int _setting;
}
