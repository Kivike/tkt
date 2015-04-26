import java.util.ArrayList;
import java.lang.String;



public class Cleaner {
	
	public static ArrayList<String> removeComments(ArrayList<String> text){
		
		for(int i = text.size() - 1; i >= 0; i--){
			for(int j = 0; j < text.get(i).length(); j++){
				if(text.get(i).charAt(j) == '/'){
					text.set(i, text.get(i).substring(0, j));
				}
				
			}
		}
		return removeEmpty(text);
	}
	
	public static ArrayList<String> removeEmpty(ArrayList<String> text){
		for(int i = text.size() - 1; i >= 0; i--){
			if(text.get(i).isEmpty()){
				text.remove(i);
			}
		}
		return text;
	}
	
	public static ArrayList<String> removeWhitespace(ArrayList<String> text){
		for(int i = 0; i < text.size(); i++){
			String str = text.get(i).replaceAll("\t", "");
			str = str.replaceAll("\n", "");
			while(true){
				if(str.indexOf("  ") == -1){
					break;
				}
				str = str.replaceAll("  ", " ");	
			}
			text.set(i, str);
		}
			
		return text;
	}
}
