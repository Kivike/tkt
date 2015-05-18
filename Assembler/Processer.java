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

}
