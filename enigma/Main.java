package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Xiaoru Zhao
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine machine = readConfig();
        if (!_input.hasNext("\\*")) {
            throw new EnigmaException("no setting");
        }
        String first = "";
        while (_input.hasNextLine()) {
            Scanner aLine = new Scanner(_input.nextLine());
            if (aLine.hasNext()) {
                first = aLine.next();
                if (first.equals("*")) {
                    String[] myRotors = new String[machine.numRotors()];
                    for (int i = 0; i < machine.numRotors(); i += 1) {
                        myRotors[i] = aLine.next();
                    }
                    machine.insertRotors(myRotors);
                    machine.setRotors(aLine.next());
                    if (aLine.hasNext("\\w+")) {
                        String ring = aLine.next();
                        char[] alpha = new char[machine.getAlphabet().size()];
                        int rotor = 0;
                        for (int m = 0; m < ring.length(); m += 1) {
                            int k = machine.getAlphabet().toInt(ring.charAt(m));
                            int a = 0;
                            while (k < machine.getAlphabet().size()) {
                                alpha[a] = machine.getAlphabet().toChar(k);
                                a += 1;
                                k += 1;
                            }
                            for (int b = 0;
                                 b < machine.getAlphabet().toInt(
                                         ring.charAt(m));
                                 b += 1) {
                                alpha[a] = machine.getAlphabet().toChar(b);
                                a += 1;
                            }
                            Alphabet alphabet = new Alphabet(alpha);
                            machine.setAlphabet(alphabet, rotor);
                            rotor += 1;
                        }
                    }
                    while (aLine.hasNext()) {
                        Permutation plugboard = new Permutation(aLine.next(),
                                machine.getAlphabet());
                        machine.setPlugboard(plugboard);
                    }
                } else {
                    String msg = first;
                    while (aLine.hasNext()) {
                        msg += aLine.next();
                    }
                    printMessageLine(machine.convert(msg));
                }
            } else {
                printMessageLine("");
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.next());
            int numRotors = _config.nextInt();
            int numPawls = _config.nextInt();
            Collection<Rotor> allRotors = new ArrayList<>();
            while (_config.hasNext()) {
                allRotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, numPawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next();
            String type = _config.next();
            String perm = _config.nextLine();
            Scanner checkperm = new Scanner(perm);
            while (checkperm.hasNext("\\(\\S+\\)")) {
                checkperm.next();
            }
            if (checkperm.hasNext()) {
                throw new EnigmaException("no ) at the end.");
            }
            if (type.length() > 1) {
                type = type.substring(1);
                return new MovingRotor(name,
                        new Permutation(perm, _alphabet), type);
            } else if (type.charAt(0) == 'N') {
                return new FixedRotor(name, new Permutation(perm, _alphabet));
            } else if (type.charAt(0) == 'R') {
                if (_config.hasNext("\\(\\S+")) {
                    return new Reflector(name, new Permutation(perm
                            + _config.nextLine(), _alphabet));
                } else {
                    return new Reflector(
                            name, new Permutation(perm, _alphabet));
                }
            }
            return null;
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        M.setRotors(settings);
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        String result = "";
        for (int i = 0; i < msg.length(); i += 1) {
            if (i != 0 && (i + 1) % 5 == 0) {
                result += msg.charAt(i) + " ";
            } else {
                result += msg.charAt(i);
            }
        }
        _output.println(result);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
