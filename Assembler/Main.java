import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;


public class Main {

	public static void main(String[] args) {
		String inputFile = "./testi004.asm";
		String outputFile = "./output.bin";
		
		if(args.length == 1) {
			if(args[0].equals("--info")) {
				System.out.printf("Peetu Nuottajärvi, %d\n", (6969696));
				System.out.printf("Roope Rajala, %d\n", (2374556));
				return;
			}else{
				inputFile = args[0];
			}
		}else if(args.length == 2) {
			inputFile = args[0];
			outputFile = args[1];
		}
		
		ArrayList<String> text = readFile(inputFile);
		Cleaner.removeEmpty(text);
		Cleaner.removeComments(text);
		Cleaner.removeWhitespace(text);
		int org = Processer.getOrg(text);
		Processer.removeEnd(text);
		HashMap<Integer, String> memorySlots = Processer.findMemorySlots(text, org);
		Processer.initializeVariables(memorySlots, org);
		Processer.commandSeparator(memorySlots, org);
		
		for (Entry<Integer, String> entry : memorySlots.entrySet()) {
			Integer key = entry.getKey();
	        String value = entry.getValue();
	        System.out.println( key + "-" + value );
	    }
		
		writeFile(memorySlots);
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
	
	public static void writeFile(HashMap<Integer, String> memorySlots){
		
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
