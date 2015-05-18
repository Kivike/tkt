import java.util.ArrayList;
import java.util.HashMap;

public class Processer {

	public static void initializeVariables(HashMap<Integer, String> memorySlots, int org) {
		
		HashMap<String, Integer> variables = new HashMap<String, Integer>();
		
		for(int i = org; i < memorySlots.size() + org; i++){
			String line = memorySlots.get(i);
			if(line.contains(",")){
				variables.put(line.substring(0, line.indexOf(',')), i);
			}
		}
		
		for(int i = org; i < memorySlots.size() + org; i++){
			String line = memorySlots.get(i);
			if(line.contains(" ")){
				Integer value = variables.get(line.substring(line.indexOf(' ') + 1));
				if(value != null){
					memorySlots.put(i, line.substring(0, line.indexOf(' ')) + " " + value);
				}
			}
		}
	}

	public static HashMap<Integer, String> findMemorySlots(ArrayList<String> text, int org){
		
		HashMap<Integer, String> memorySlots = new HashMap<Integer, String>();
		for(int i = 0; i < text.size(); i++){
			memorySlots.put(i + org, text.get(i));		
		}		
		
		return memorySlots;
	}
	
	public static int getOrg(ArrayList<String> text){
		int org = Integer.parseInt(text.get(0).substring(text.get(0).indexOf("ORG ") + "ORG ".length()));
		if(org == -1){
			new Exception("No origin for program given").printStackTrace();
			System.exit(0);
		}
		return org;
	}
	
	public static ArrayList<String> removeEnd(ArrayList<String> text){		
		text.remove(0);
		
		boolean removing = false;
		for(int i = 0; i < text.size(); i++){
			if(text.get(i).contains("END")){
				removing = true;
			}
			if(removing){
				text.remove(i);
			}
		}
		return text;
	}
	
	public static void commandSeparator(HashMap<Integer, String> memorySlots, int org){
		for(int i = org; i < memorySlots.size() + org; i++){
			String line = memorySlots.get(i);
			if(line.contains(",")){
				line = formHexCodeVariable(line);
			}else if(line.contains(" ")){
			    line = formHexCode(line.substring(0, line.indexOf(" ")), i);
			}else{
				line = formHexCode(line);
			}
			memorySlots.put(i, line);
		}
	}
	
	public static String formHexCodeVariable(String line){
		line = line.substring(line.indexOf(", ") + ", ".length());
		String value;
		if(line.contains("DEC")){
			value = Integer.toHexString(Integer.parseInt(line.substring(line.indexOf("DEC ") + "DEC ".length())));
			//If number is a 8 bit 2's complement or over 4 bit number	
			if(value.length() > 4){
				if(value.substring(0, 4).equals("ffff")){
					value = value.substring(4);
				}else{
					new Exception("only 4 byte values allowed");
				}
			}
			
		}else{
			value = line;
		}
		return value;
	}
	
	// Hae komennolle heksakoodin alku hashmapista ja lis‰‰ muistipaikka komentoon
	public static String formHexCode(String symbol, int memory) {
		String hexCode = Integer.toString(Command.symbols.get(symbol));
		
		hexCode += memory;
		
		return hexCode;
	}
	
	// Hae komennolle heksakoodi hashmapista
	public static String formHexCode(String symbol) {
		
		return Integer.toHexString(Command.symbols.get(symbol));
		
	}

}
