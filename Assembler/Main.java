import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Main {

	public static void main(String[] args) {
		
		String filename = "./testi004.asm";
		ArrayList<String> text = readFile(filename);
		Cleaner.removeEmpty(text);
		Cleaner.removeComments(text);
		Cleaner.removeWhitespace(text);
		
		System.out.println(text);

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
	
	public static ArrayList<Byte> translateToBinary(ArrayList<String> text){
		
		
		return null;
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
