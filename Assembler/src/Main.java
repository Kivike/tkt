/*
 * CodeAnAssembler
 * 16-bit basic computer assembler
 * Tietokonetekniikka
 *
 * Roope Rajala
 * Peetu Nuottajärvi
 *
 *
 *******************************
 *   Read README.MD for usage  *
 *******************************
 */


public class Main {
	// Show debug logs?
	final static boolean DEBUG_MODE = false;

	// File to open when no file is given as command line argument
	final static String TEST_FILE = "./testi006.asm";

	// Output file when no output/only input file is given
	final static String DEFAULT_OUTPUT_FILE = "./output.bin";

	private static String inputFile;
	private static String outputFile;

	public static void main(String[] args) {
		processArguments(args);
		Assembler assembler = new Assembler();

		try {
			assembler.run(inputFile, outputFile);
		} catch (Exception ex) {
			System.out.println("Unexpected error");
			System.exit(8);
		}

		System.out.println(inputFile + " succesfully assembled to " + outputFile);
		System.exit(0);
	}

	/*
	 * Handles given commandline arguments and sets defaults if none is given
	 */
	private static void processArguments(String[] args) {
		// Default arguments
		inputFile = TEST_FILE;
		outputFile = DEFAULT_OUTPUT_FILE;

		if(args.length == 1) {
			if(args[0].equals("--info")) {
				System.out.printf("Peetu Nuottaj�rvi, %d\n", (2374491));
				System.out.printf("Roope Rajala, %d\n", (2374556));
				return;
			} else {
				inputFile = args[0];
			}
		} else if(args.length >= 2) {
			inputFile = args[0];
			outputFile = args[1];
		}
	}
}

