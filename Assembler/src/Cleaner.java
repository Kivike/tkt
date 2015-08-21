import java.util.ArrayList;
import java.lang.String;

/*
 * Cleans rows so they can be parsed with CommandParser
 */
public class Cleaner {
	/*
	 * Remove everything after / from a string
	 */
	public String removeComments(String text){
			for(int j = 0; j < text.length(); j++){
				if(text.charAt(j) == '/'){
					return(text.substring(0, j));
				}
			}
		return text;
	}

	/*
	 * Remove tabs and whitespaces from a string
	 */
	public String removeWhitespace(String text){
		String str = text.replaceAll("\t", " ");
		str = str.replaceAll("\n", "");
		while(true){
			if(str.indexOf("  ") == -1){
				break;
			}
			str = str.replaceAll("  ", " ");
		}
		if(str.indexOf(' ') == 0){
			str = str.substring(1);
		}

		str = str.replaceAll(", ", ",");
		str = str.replaceAll(", ", ",");

		return str;
	}

	/*
	 * Clean all given rows from extra whitespaces, comments etc.
	 */
	public ArrayList<String> cleanAll(ArrayList<String> rows) {
		if(Main.DEBUG_MODE) System.out.println("#### CLEAN ALL ROWS ####");

		ArrayList<String> newRows = new ArrayList<String>();

		for(int i = 0; i < rows.size(); i++) {
			String row = rows.get(i);

			// Remove everything after /
			row = removeComments(row);

			// Remove tabs and extra spaces
			row = removeWhitespace(row);

			if(row.isEmpty()) {
				continue;
			}

			newRows.add(row);
		}

		if(Main.DEBUG_MODE) Assembler.printRows(newRows);

		return newRows;
	}
}
