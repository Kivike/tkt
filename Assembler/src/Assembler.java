import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/*
 * Assembler creates binary file from text file with basic computer commands
 */
public class Assembler {
    public final static int MEMORY_WORDS = 4096;
    public static int currentRowNumber = 0;

    private String outputFile;

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

    public void run(String inputFile, String outputFile) {
        this.outputFile = outputFile;

        ArrayList<String> rows = readFile(inputFile);

        Cleaner cleaner = new Cleaner();
        // Clean comments, whitespaces etc. from the text
        rows = cleaner.cleanAll(rows);

        CommandParser commandParser = new CommandParser();

        ArrayList<Label> labels = new ArrayList<Label>();
        rows = firstIteration(labels, rows, commandParser);

        // TreeMap is an HashMap which is ordered by key values
        TreeMap<Short, Short> commands = secondIteration(labels, rows, commandParser);;

        writeOutputFile(commands);
    }

    /*
	 * First iteration fetches labels from text and stores them to ArrayList of labels (see Label class)
	 */
    private ArrayList<String> firstIteration(ArrayList<Label> labels, ArrayList<String> rows, CommandParser commandParser) {
        if(Main.DEBUG_MODE) System.out.println("#### FIRST ITERATION ####");

        short lc = 0;

        // Loop through rows
        for(int i = 0; i < rows.size(); i++) {
            currentRowNumber = i + 1;

            String row = rows.get(i);

            if(lc >= MEMORY_WORDS) {
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

                label.memorySlot = lc;
                labels.add(labels.size(), label);

                row = label.command;
                rows.set(i, row);
            }
            lc++;
        }

        if(Main.DEBUG_MODE) printLabels(labels);

        return rows;
    }

    /*
     * Second iteration gets command integer for each row and stores them by memory slot to HashMap
     */
    private TreeMap<Short, Short> secondIteration(ArrayList<Label> labels, ArrayList<String> rows, CommandParser commandParser) {
        if(Main.DEBUG_MODE) System.out.println("#### SECOND ITERATION ####");

        int lc = 0;
        TreeMap<Short, Short> commandByMemorySlot = new TreeMap<Short, Short>();

        for(int i = 0; i < rows.size(); i++) {
            currentRowNumber = i + 1;

            String row = rows.get(i);

            if(lc >= MEMORY_WORDS) {
                printErrorAndExit(5);
            }

            if(row.startsWith("END")) {
                break;
            }

            // We checked for labels in first iteration, skip them in second
            // Although we shouldn't have labels left at this point
            if(commandParser.rowIsLabel(row)) {
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
            commandByMemorySlot.put((short)(lc), command);

            lc++;
        }

        if(Main.DEBUG_MODE) printCommands(commandByMemorySlot);

        return commandByMemorySlot;
    }

    /*
     * Reads file with given name and returns its rows as ArrayList
     */
    public ArrayList<String> readFile(String filename){
        if(Main.DEBUG_MODE) System.out.println("#### READ FILE ####");

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            ArrayList<String> lines = new ArrayList<String>();
            String line = br.readLine();

            while (line != null) {
                lines.add(line);
                line = br.readLine();
            }
            br.close();

            if(Main.DEBUG_MODE) printRows(lines);

            return lines;
        } catch (IOException e) {
            e.printStackTrace();
            printErrorAndExit(9);
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
    public static void printCommands(TreeMap<Short, Short> commands) {
        System.out.println("[MEMORY SLOT] COMMAND");
        for(Map.Entry<Short, Short> entry : commands.entrySet()) {
            Short memorySlot = entry.getKey();
            Short command = entry.getValue();

            System.out.printf("[%s] %s\n", Integer.toHexString(memorySlot), Integer.toHexString(command & 0xffff));
        }
    }

    /*
     * Write commands to a binary file (16-bit)
     */
    public void writeOutputFile(TreeMap<Short, Short> commands) {
        if(Main.DEBUG_MODE) System.out.println("#### WRITE BINARY TO FILE ####");

        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(outputFile));

            for(short memorySlotIndex = 0; memorySlotIndex < MEMORY_WORDS; memorySlotIndex++) {
                if(commands.containsKey(memorySlotIndex)) {
                    byte[] bytes = new byte[2];

                    short command = commands.get(memorySlotIndex);

                    // writeShort uses little-endian for shorts so we have to divide short
                    // into two bytes and use writeByte
                    bytes[0] = (byte)(command & 0xff);
                    bytes[1] = (byte)((command >> 8) & 0xff);

                    out.writeByte(bytes[0]);
                    out.writeByte(bytes[1]);
                } else {
                    out.writeShort((short)0);
                }
            }

            out.close();
        } catch(FileNotFoundException fEx) {
            fEx.printStackTrace();
            printErrorAndExit(9);
        } catch(IOException ioEx) {
            ioEx.printStackTrace();
            printErrorAndExit(9);
        }
    }
}
