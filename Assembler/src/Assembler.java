import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/*
 * Assembler creates binary file from text file with basic computer commands
 */
public class Assembler {
    private final int MAX_LC = 5000;
    public static int currentRowNumber = 0;

    private String outputFile;
    public static boolean debug;

    public static String[] errorMessages = new String[] {
            "ORG Value Out of Range",
            "Bad Symbol",
            "Unknown Instruction",
            "Symbol not found",
            "Memory Overflow",
            "Number Out Of Range",
            "Bad number",
            "General Syntax Error",
            "File error"
    };

    public void run(String inputFile, String outputFile, boolean debug) {
        this.outputFile = outputFile;
        this.debug = debug;

        ArrayList<String> rows = readFile(inputFile);

        Cleaner cleaner = new Cleaner();
        // Clean comments, whitespaces etc. from the text
        rows = cleaner.cleanAll(rows);

        CommandParser commandParser = new CommandParser();

        ArrayList<Label> labels = new ArrayList<Label>();
        rows = firstIteration(labels, rows, commandParser);
        TreeMap<Integer, Short> commands = secondIteration(labels, rows, commandParser);;

        writeOutputFile(commands);
    }

    /*
	 * First iteration fetches labels from text and stores them to ArrayList of labels (see Label class)
	 */
    private ArrayList<String> firstIteration(ArrayList<Label> labels, ArrayList<String> rows, CommandParser commandParser) {
        if(debug) System.out.println("#### FIRST ITERATION ####");

        short lc = 0;

        // Loop through rows
        for(int i = 0; i < rows.size(); i++) {
            currentRowNumber = i + 1;

            String row = rows.get(i);

            int firstSpace = row.indexOf(' ');
            if(firstSpace == -1)
                firstSpace = 2;
            if(row.substring(0,firstSpace).length() >= 3 && !Character.isLetter(row.charAt(0)))
                printErrorAndExit(2);

            if(lc >= MAX_LC) {
                printErrorAndExit(5);
            }

            // Break loop when END is found
            if(row.startsWith("END")) {
                break;
            }

            // Check if row changes memory slot
            short org = commandParser.checkStringForORG(row);

            // If new origin found, set it as lc and continue to next row
            if(org != -1) {
                lc = org;
                continue;
            }

            if (commandParser.rowIsLabel(row)) {
                // If row is label, get the int value and add it to dict by its name
                Label label = commandParser.getLabelFromString(row);
                System.out.println("lc: " + lc);
                label.memorySlot = lc;
                labels.add(labels.size(), label);

                row = label.command;
                rows.set(i, row);
            }
            lc++;
        }

        if(debug) printLabels(labels);

        return rows;
    }

    /*
     * Second iteration gets command integer for each row and stores them by memory slot to HashMap
     */
    private TreeMap<Integer, Short> secondIteration(ArrayList<Label> labels, ArrayList<String> rows, CommandParser commandParser) {
        if(debug) System.out.println("#### SECOND ITERATION ####");

        int lc = 0;
        TreeMap<Integer, Short> commandByMemorySlot = new TreeMap<Integer, Short>();

        for(int i = 0; i < rows.size(); i++) {
            currentRowNumber = i + 1;

            String row = rows.get(i);

            if(lc >= MAX_LC) {
                printErrorAndExit(5);
            }

            if(row.startsWith("END")) {
                break;
            }

            // We checked for labels in first iteration, skip them in second
            if(row.contains(",")) {
                continue;
            }

            // Check if row changes memory slot
            int org = commandParser.checkStringForORG(row);

            // If new origin found, set it as lc and continue to next row
            if(org != -1) {
                lc = org;
                continue;
            }

            short command = commandParser.parseCommandFromRow(row, labels);
            commandByMemorySlot.put(lc, command);

            lc++;
        }

        if(debug) printCommands(commandByMemorySlot);

        return commandByMemorySlot;
    }

    public ArrayList<String> readFile(String filename){
        if(debug) System.out.println("#### READ FILE ####");

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            ArrayList<String> lines = new ArrayList<String>();
            String line = br.readLine();

            while (line != null) {
                lines.add(line);
                line = br.readLine();
            }
            br.close();

            if(debug) printRows(lines);

            return lines;
        } catch (IOException e) {
            e.printStackTrace();
            printErrorAndExit(1);
        }

        return null;
    }

    public static void printErrorAndExit(int errorNumber) {
        System.err.println(currentRowNumber + ":" + errorMessages[errorNumber - 1]);
        System.exit(errorNumber);
    }

    /*
     * Print list of strings
     */
    public static void printRows(ArrayList<String> rows) {
        for(int i = 0; i < rows.size(); i++) {
            System.out.println(rows.get(i));
        }
    }

    /*
     * Print list of Labels
     * See Label class
     */
    public static void printLabels(ArrayList<Label> labels) {
        System.out.println("LABELS:");
        for(int i = 0; i < labels.size(); i++) {
            System.out.println(labels.get(i).name + "," + labels.get(i).command);
        }
    }

    /*
     * Print TreeMap<Integer MEMORYSLOT, Short COMMAND> commands
     */
    public static void printCommands(TreeMap<Integer, Short> commands) {
        System.out.println("[MEMORY SLOT] COMMAND");
        for(Map.Entry<Integer, Short> entry : commands.entrySet()) {
            Integer memorySlot = entry.getKey();
            Short command = entry.getValue();

            System.out.printf("[%s] %s\n", Integer.toHexString(memorySlot), Integer.toHexString(command & 0xffff));
        }
    }

    /*
     * Write commands to a binary file (16-bit)
     */
    public void writeOutputFile(TreeMap<Integer, Short> commands) {
        if(debug) System.out.println("#### WRITE BINARY TO FILE ####");

        int lastMemorySlot = commands.lastKey();

        try {
            DataOutputStream os = new DataOutputStream(new FileOutputStream(outputFile));

            for(int i = 0; i <= lastMemorySlot; i++) {
                if(commands.containsKey(i)) {
                    os.writeShort(commands.get(i));
                } else {
                    os.writeShort(0);
                }
            }

            os.close();
        } catch(FileNotFoundException fEx) {
            fEx.printStackTrace();
            printErrorAndExit(9);
        } catch(IOException ioEx) {
            ioEx.printStackTrace();
            printErrorAndExit(9);
        }
    }
}
