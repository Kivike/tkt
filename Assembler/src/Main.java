import java.io.*;
import java.util.*;

import javax.xml.bind.DatatypeConverter;

/*
 * CodeAnAssembler
 * 16-bit basic computer assembler
 * Tietokonetekniikka
 *
 * Roope Rajala
 * Peetu Nuottajärvi
 */

/*
 * TODO: Handle rest of exceptions (https://wiki.oulu.fi/display/TKT/CodeAnAssembler)
 */
public class Main {
	private static String inputFile;
	private static String outputFile;

	private static final int MAX_LC = 5000;

	public static void main(String[] args) {
		processArguments(args);

		System.out.println("#### READ FILE ####");
		ArrayList<String> text = readFile(inputFile);
		printRows(text);

		ArrayList<Label> labels = new ArrayList<Label>();

		Cleaner cleaner = new Cleaner();
		// Clean comments, whitespaces etc. from the text
		System.out.println("#### CLEAN ROWS ####");
		text = cleaner.cleanAll(text);

		printRows(text);

		Processer processer = new Processer();
		// First iteration finds labels and adds their values and namues to a Label list
		System.out.println("#### FIRST ITERATION ####");
		text = firstIteration(labels, text, processer);
		printLabels(labels);

		System.out.println("#### SECOND ITERATION ####");
		TreeMap<Integer, Short> commands = secondIteration(labels, text, processer);;
		printCommands(commands);

		System.out.println("#### WRITE BINARY TO FILE ####");
		writeOutputFile(commands);
	}

	/*
	 * Handles given commandline arguments and sets defaults if none is given
	 */
	private static void processArguments(String[] args) {
		// Default arguments
		inputFile = "./testi005.asm";
		outputFile = "./output.bin";

		if(args.length == 1) {
			if(args[0].equals("--info")) {
				System.out.printf("Peetu Nuottaj�rvi, %d\n", (6969696));
				System.out.printf("Roope Rajala, %d\n", (2374556));
				return;
			}else{
				inputFile = args[0];
			}
		} else if(args.length == 2) {
			inputFile = args[0];
			outputFile = args[1];
		}
	}

	/*
	 * First iteration fetches labels from text and stores them to ArrayList of labels (see Label class)
	 */
	private static ArrayList<String> firstIteration(ArrayList<Label> labels, ArrayList<String> rows, Processer processer) {
		// Loop through rows
		for(int i = 0; i < rows.size(); i++) {
			String row = rows.get(i);

			// Break loop when END is found
			if(row.startsWith("END")) {
				break;
			}

			if (processer.rowIsLabel(row)) {
				// If row is label, get the int value and add it to dict by its name
				Label label = processer.getLabelFromString(row);
				labels.add(labels.size(), label);

				row = label.command;
				rows.set(i, row);
			}
		}

		return rows;
	}

	/*
	 * Second iteration gets command integer for each row and stores them by memory slot to HashMap
	 */
	private static TreeMap<Integer, Short> secondIteration(ArrayList<Label> labels, ArrayList<String> rows, Processer processer) {
		int lc = 0;
		TreeMap<Integer, Short> commandByMemorySlot = new TreeMap<Integer, Short>();

		for(int i = 0; i < rows.size(); i++) {
			String row = rows.get(i);

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

		return commandByMemorySlot;
	}

	public static ArrayList<String> readFile(String filename){
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
	private static void printRows(ArrayList<String> rows) {
		for(int i = 0; i < rows.size(); i++) {
			System.out.println(rows.get(i));
		}
	}

	// Print list of Labels
	// See Label class
	private static void printLabels(ArrayList<Label> labels) {
		for(int i = 0; i < labels.size(); i++) {
			System.out.println(labels.get(i).name + "," + labels.get(i).command);
		}
	}

	// Print TreeMap<Integer MEMORYSLOT, Short COMMAND> commands
	private static void printCommands(TreeMap<Integer, Short> commands) {
		for(Map.Entry<Integer, Short> entry : commands.entrySet()) {
			Integer memorySlot = entry.getKey();
			Short command = entry.getValue();

			System.out.printf("[%s] %s\n", Integer.toHexString(memorySlot), Integer.toHexString(command & 0xffff));
		}
	}

	// Write commands to a binary file (16-bit)
	public static void writeOutputFile(TreeMap<Integer, Short> commands) {
		int lastMemorySlot = commands.lastKey();
		try {
			DataOutputStream os = new DataOutputStream(new FileOutputStream(outputFile));

			for(int i = 0; i <= lastMemorySlot; i++) {
				System.out.print(i);
				if(commands.containsKey(i)) {
					os.writeShort(commands.get(i));
				} else {
					os.writeShort(0);
				}
			}

			os.close();
		} catch(FileNotFoundException fex) {
			fex.printStackTrace();
			System.exit(1);
		} catch(IOException ioex) {
			ioex.printStackTrace();
			System.exit(1);
		}

		System.exit(0);
	}
}

/*
/ A simple, easy program

ORG 100
LDA A
ADD B
STA 	C
	HLT
A,	DEC 83
	B, DEC -23
C,	DEC 0
END
*/
