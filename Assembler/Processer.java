import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Processer {
	
	public static void initializeVariables(ArrayList<String> text) {
		
		HashMap<String, Integer> variables = new HashMap<String, Integer>();
		
		for(int i = text.size() - 1; i >= 0; i--){
			String line = text.get(i);
			if(line.contains(",")){
				variables.put(line.substring(0, line.indexOf(',')), 
						Integer.parseInt(line.substring(line.indexOf("DEC ") + 4)));
				text.remove(i);
			}
		}
		
		for(String line : text){
			if(line.contains(" ")){
				Integer value = variables.get(line.substring(line.indexOf(' ') + 1));
				if(value != null){
					text.set(text.indexOf(line), line.substring(0, line.indexOf(' ')) + " " + value);
				}
			}
		}
	}
	
	// Hae komennolle heksakoodin alku hashmapista ja lis‰‰ muistipaikka komentoon
	public static String formHexCode(String symbol, int memory) {
		String hexCode = Integer.toString(Command.symbols.get(symbol));
		
		hexCode += memory;
		
		return hexCode;
	}
	
	// Hae komennolle heksakoodi hashmapista
	public static String formHexCode(String symbol) {
		
		return Integer.toString(Command.symbols.get(symbol));
		
	}

}
