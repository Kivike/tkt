import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;


public class Main {

	public static void main(String[] args) {
		
		String filename = "./testi004.asm";
		ArrayList<String> text = readFile(filename);
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
