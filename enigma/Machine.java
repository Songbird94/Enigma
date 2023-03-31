package enigma;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Xiaoru Zhao
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = new ArrayList<Rotor>();
        for (Rotor x : allRotors) {
            _allRotors.add(x);
        }
        _myRotors = new ArrayList<>();
        _plugboard = new Permutation("", alpha);
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        _myRotors.clear();
        for (int i = 0; i < rotors.length; i += 1) {
            if (_allRotors.isEmpty()) {
                throw new EnigmaException("Empty allRotors.");
            } else {
                for (int k = 0; k < _allRotors.size(); k += 1) {
                    if (_allRotors.get(k).name().equals(rotors[i])) {
                        _myRotors.add(_allRotors.get(k));
                        break;
                    }
                    if (!_allRotors.get(k).name().equals(
                            rotors[i]) && k == _allRotors.size() - 1) {
                        throw new EnigmaException("Rotor not contained.");
                    }
                }
            }
        }
        if (!_myRotors.get(0).reflecting()) {
            throw new EnigmaException("First one not reflector.");
        }
        int numFixed = 0;
        for (int m = 1; m < _numRotors - _pawls; m += 1) {
            if (!_myRotors.get(m).rotates()) {
                numFixed += 1;
            }
        }
        if (numFixed != _numRotors - _pawls - 1) {
            throw new EnigmaException("Incorrect number of fixed rotors.");
        }
        if (_myRotors.size() != _numRotors) {
            throw new EnigmaException(
                    "Inserted rotors number not equal to numRotors.");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int i = 1; i < _myRotors.size(); i += 1) {
            _myRotors.get(i).set(setting.charAt(i - 1));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard.addCycle(plugboard.getCycle());
        if (_plugboard.size() % 2 != 0) {
            throw new EnigmaException("Value maps to itself.");
        }
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        int result = c;
        result = _plugboard.permute(_plugboard.wrap(result));
        if (_myRotors.get(_numRotors - 1).atNotch()) {
            _myRotors.get(_numRotors - 1).updateNotch(true);
        }
        _myRotors.get(_numRotors - 1).advance();
        if (_myRotors.get(_numRotors - 1).atNotch()) {
            _myRotors.get(_numRotors - 1).updateNotch(true);
        }
        result = _myRotors.get(_numRotors - 1).convertForward(result);
        for (int i = _numRotors - 2; i >= 0; i -= 1) {
            if (_myRotors.get(i).rotates()) {
                if (_myRotors.get(i).atNotch()) {
                    _myRotors.get(i).updateNotch(true);
                }
                if (_myRotors.get(i + 1).atNotchBefore()
                        && !_myRotors.get(i + 1).atNotch()) {
                    _myRotors.get(i).advance();
                    _myRotors.get(i + 1).updateNotch(false);
                    if (_myRotors.get(i).atNotch()) {
                        _myRotors.get(i).updateNotch(true);
                    }
                } else if (_myRotors.get(i).atNotch()
                        && _myRotors.get(i - 1).rotates()) {
                    _myRotors.get(i).advance();
                    _myRotors.get(i - 1).advance();
                    _myRotors.get(i).updateNotch(false);
                }
            }
            result = _myRotors.get(i).convertForward(result);
        }
        for (int i = 1; i < _numRotors; i += 1) {
            result = _myRotors.get(i).convertBackward(result);
        }
        result = _plugboard.permute(_plugboard.wrap(result));
        return result;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String result = "";
        for (int i = 0; i < msg.length(); i += 1) {
            result += _alphabet.toChar(convert(_alphabet.toInt(msg.charAt(i))));
        }
        return result;
    }

    /** Returns the alphabet of machine. */
    Alphabet getAlphabet() {
        return _alphabet;
    }

    /** Set the alphabet ALPHABET to the NUMROTOR. */
    void setAlphabet(Alphabet alphabet, int numRotor) {
        _myRotors.get(numRotor).setA(alphabet);
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of my rotors. */
    private int _numRotors;

    /** Number of my pawls. */
    private int _pawls;

    /** Array of all rotors. */
    private ArrayList<Rotor> _allRotors;

    /** Array of my rotors. */
    private ArrayList<Rotor> _myRotors;

    /** My plugboard. */
    private Permutation _plugboard;
}
