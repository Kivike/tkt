import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by roope_000 on 18.8.2015.
 */
public class Assembler {
    private final int MAX_LC = 5000;
    private String outputFile;
    public static boolean debug;

    public void run(String inputFile, String outputFile, boolean debug) {
        this.outputFile = outputFile;
        this.debug = debug;

        ArrayList<String> text = readFile(inputFile);
        if(debug) printRows(text);

        ArrayList<Label> labels = new ArrayList<Label>();

        Cleaner cleaner = new Cleaner();
        // Clean comments, whitespaces etc. from the text
        text = cleaner.cleanAll(text);
        if(debug) printRows(text);

        Processer processer = new Processer();
        // First iteration finds labels and adds their values and namues to a Label list

        text = firstIteration(labels, text, processer);
        TreeMap<Integer, Short> commands = secondIteration(labels, text, processer);;

        writeOutputFile(commands);
    }

    /*
	 * First iteration fetches labels from text and stores them to ArrayList of labels (see Label class)
	 */
    private ArrayList<String> firstIteration(ArrayList<Label> labels, ArrayList<String> rows, Processer processer) {
        if(debug) System.out.println("#### FIRST ITERATION ####");
        short lc = 0;

        // Loop through rows
        for(int i = 0; i < rows.size(); i++) {
            String row = rows.get(i);

            if(lc > MAX_LC) {
                System.exit(5);
            }

            // Break loop when END is found
            if(row.startsWith("END")) {
                break;
            }

            if (processer.rowIsLabel(row)) {
                // If row is label, get the int value and add it to dict by its name
                Label label = processer.getLabelFromString(row);
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
    private TreeMap<Integer, Short> secondIteration(ArrayList<Label> labels, ArrayList<String> rows, Processer processer) {
        if(debug) System.out.println("#### SECOND ITERATION ####");

        int lc = 0;
        TreeMap<Integer, Short> commandByMemorySlot = new TreeMap<Integer, Short>();

        for(int i = 0; i < rows.size(); i++) {
            String row = rows.get(i);

            if(lc > MAX_LC) {
                System.exit(5);
            }

            if(row.startsWith("END")) {
                break;
            }

            // We checked for labels in first iteration, skip them in second
            if(row.contains(",")) {
                continue;
            }

            // Check if row changes memory slot
            int org = processer.checkStringForORG(row);

            // If new origin found, set it as lc and continue to next row
            if(org != -1) {
                lc = org;
                continue;
            }

            short command = processer.parseCommandFromRow(row, labels);
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
            return lines;
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }

    // Print list of strings
    private void printRows(ArrayList<String> rows) {
        for(int i = 0; i < rows.size(); i++) {
            System.out.println(rows.get(i));
        }
    }

    // Print list of Labels
    // See Label class
    private void printLabels(ArrayList<Label> labels) {
        System.out.println("LABELS:");
        for(int i = 0; i < labels.size(); i++) {
            System.out.println(labels.get(i).name + "," + labels.get(i).command);
        }
    }

    // Print TreeMap<Integer MEMORYSLOT, Short COMMAND> commands
    private void printCommands(TreeMap<Integer, Short> commands) {
        System.out.println("[MEMORY SLOT] COMMAND");
        for(Map.Entry<Integer, Short> entry : commands.entrySet()) {
            Integer memorySlot = entry.getKey();
            Short command = entry.getValue();

            System.out.printf("[%s] %s\n", Integer.toHexString(memorySlot), Integer.toHexString(command & 0xffff));
        }
    }

    // Write commands to a binary file (16-bit)
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
            System.exit(1);
        } catch(IOException ioEx) {
            ioEx.printStackTrace();
            System.exit(1);
        }
    }
}
