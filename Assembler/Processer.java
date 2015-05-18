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
	
	
	public static void formHexCode(String symbol, int memory) {
		String hexCode;
		
		switch(symbol) {
			case "AND": 
				hexCode = "0" + memory;
				break;
				
			case "ADD":
				hexCode = "1" + memory;
				break;
				
			case "LDA":
				hexCode = "2" + memory;
				break;
			
			case "STA":
				hexCode = "3" + memory;
				break;
			
			case "BUN":
				hexCode = "4" + memory;
				break;
			
			case "BSA":
				hexCode = "5" + memory;
				break;
			
			case "ISZ":
				hexCode = "6" + memory;
				break;	
		}
		
		
	}

}
