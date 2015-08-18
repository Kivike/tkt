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

/*
 * TODO: Handle rest of exceptions (https://wiki.oulu.fi/display/TKT/CodeAnAssembler)
 */
public class Main {
	// Show debug logs
	final static boolean DEBUG_MODE = true;
	final static String TEST_FILE = "./testi006.asm";
	final static String DEFAULT_OUTPUT_FILE = "./output.bin";

	private static String inputFile;
	private static String outputFile;

	public static void main(String[] args) {
		processArguments(args);
		Assembler assembler = new Assembler();

		try {
			assembler.run(inputFile, outputFile, DEBUG_MODE);
		} catch (Exception ex) {
			System.out.println("Unexpected error");
			System.exit(8);
		}

		System.out.println("Completed succesfully");
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
}

