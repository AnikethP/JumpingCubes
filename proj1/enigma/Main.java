package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.LinkedList;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Aniketh Prasad
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
        Machine mech = readConfig();
        while(_input.hasNextLine()){
            String y = _input.nextLine();
            //System.out.println("Settings: " + y);
            if(y.charAt(0) != '*'){
                throw new EnigmaException("Bad settings");
            }
            setUp(mech, y);
            System.setOut(_output);
            String x = "";
            while(_input.hasNext("([A-z]+|[ ]+)")){
                x = _input.nextLine();
                //System.out.println("x: " + x);
                printMessageLine(mech.convert(x));
            }


        }


    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            // FIXME
            Scanner scanFile = _config;
            _alphabet = new Alphabet(scanFile.nextLine());
            int slots = scanFile.nextInt();
            int pawls = scanFile.nextInt();

            LinkedList<Rotor> allRotors = new LinkedList<>();
            scanFile.nextLine();
            while(scanFile.hasNextLine()){
                String name = scanFile.next();
                String info = scanFile.next();
                String type = info.substring(0,1);

                String perm = "";
                perm += scanFile.nextLine();
                //System.out.println(scanFile.hasNext("[(].*"));
                while(scanFile.hasNext("[(].*")){
                    perm += scanFile.nextLine();
                }
                Permutation p = new Permutation(perm, _alphabet);
                String notches = "";
                if(info.length() > 1){
                    notches = info.substring(1);

                }

                if(type.equals("M")){
                    allRotors.add(new MovingRotor(name, p, notches));
                }

                if(type.equals("N")){
                    allRotors.add(new FixedRotor(name, p));
                }

                if(type.equals("R")){
                    allRotors.add(new Reflector(name, p));
                }
            }

            return new Machine(_alphabet, slots, pawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            return null; // FIXME
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) throws EnigmaException{
        // FIXME
        Scanner scanString = new Scanner(settings);
        String x = scanString.next();
 //       if (!x.equals("*")){
   //         throw new EnigmaException("Invalid settings");
     //   }
        String[] rotors = new String[M.numRotors()];
        for(int i = 0; i < M.numRotors(); i++){
            rotors[i] = scanString.next();
        }
        M.insertRotors(rotors);

        String notches = scanString.next();
        if(notches.length() != M.numRotors()-1){
            throw new EnigmaException("Wrong amount of notches for amount of rotors");
        }
        M.setRotors(notches);
        //System.out.println(notches);
        if(scanString.hasNextLine()){
            String rest = scanString.nextLine();
            M.setPlugboard(new Permutation(rest, _alphabet));
        }
        else{
            M.setPlugboard(new Permutation("", _alphabet));
        }





    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        // FIXME
        char[] message = msg.toCharArray();
        String outputMessage = "";
        int count = 0;
        for(char m : message){
            if(count == 5){
                outputMessage += " " + m;
                count = 0;
            }
            else{
                outputMessage += m;
                count+=1;
            }
        }
        _output.println(outputMessage);
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
