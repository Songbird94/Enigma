package enigma;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Xiaoru Zhao
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _name = name;
        _permutation = perm;
        _notches = notches;
        _setting = 0;
        _notchBefore = false;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    void advance() {
        _setting += 1;
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
    boolean atNotch() {
        for (int i = 0; i < _notches.length(); i += 1) {
            if (_notches.charAt(i) == alphabet().toChar(
                    _permutation.wrap(_setting))) {
                return true;
            }
        }
        return false;
    }

    @Override
    boolean atNotchBefore() {
        return _notchBefore;
    }

    @Override
    void updateNotch(boolean a) {
        _notchBefore = a;
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


    /** Rotor name. */
    private final String _name;

    /** Rotor permutation. */
    private Permutation _permutation;

    /** Rotor notches. */
    private String _notches;

    /** Rotor setting. */
    private int _setting;

    /** Record of notch status. */
    private boolean _notchBefore;

}
