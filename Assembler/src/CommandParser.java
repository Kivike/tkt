import java.text.ParseException;
import java.util.*;

/*
 * Parses commands from strings
 */
public class CommandParser {
	public short checkStringForORG(String string) {
		short origin = -1;

		if(string.length() > 4 && string.substring(0, 4).equals("ORG ")) {
			String[] splitString = string.split(" ");

			try {
				origin = Short.parseShort(splitString[1], 16);
			} catch(NumberFormatException ex){
				Assembler.printErrorAndExit(7);
			}
		}

		if(origin >= Assembler.MEMORY_WORDS){
			Assembler.printErrorAndExit(1);
		}

		return origin;
	}

	/**
	 * Parse basic computer command from a string row
	 * @param row Row to parse command from
	 * @param labels List of labels fetched at first iteration
	 * @return Returns command as short (handle as hexadecimal)
	 **/
	public short parseCommandFromRow(String row, ArrayList<Label> labels) {
		// Check if command is a pseudo comand
		for(Command.Pseudo pseudo : Command.Pseudo.values()) {
			if(row.startsWith(pseudo.name())) {
				return getPseudoCommandFromRow(row);
			}
		}

		// Check if command is of type M
		for(Command.TypeM typeM : Command.TypeM.values()) {
			if(row.startsWith(typeM.name())) {
				return getTypeMCommandFromRow(row, labels);
			}
		}

		// Check if command is of type R
		for(Command.TypeR typeR : Command.TypeR.values()) {
			if(row.startsWith(typeR.name())) {
				return Command.typeRcommands.get(typeR);
			}
		}

		// Check if command is of type IO
		for(Command.TypeIO typeIO : Command.TypeIO.values()) {
			if(row.startsWith(typeIO.name())) {
				return Command.typeIOcommands.get(typeIO);
			}
		}

		// No command was found, exit
		Assembler.printErrorAndExit(3);

		return -1;
	}

	/*
	 * Check if row is label (contains ,-symbol)
	 */
	public boolean rowIsLabel(String row) {
		return row.contains(",") ? true : false;
	}

	/*
	 * Checks whether command is direct M command or indirect M command
	 *
	 * Direct M command includes value of memory slot in the command
	 * Indirect M command has value of memory slot where the actual memory slot is taken
	 */
	public boolean isCommandDirect(String[] rowParts) {
		if(rowParts.length == 3 && rowParts[2].equals("I")) {
			return false;
		}

		return true;
	}

	/*
	 * Get type of M command
	 */
	private Command.TypeM getTypeMCommandTypeFromString(String string) {
		for(Command.TypeM typeM : Command.TypeM.values()) {
			if(string.startsWith(typeM.name())) {
				return typeM;
			}
		}

		return Command.TypeM.None;
	}

	/*
	 * Parse type M command from a row and return command as integer
	 */
	public short getTypeMCommandFromRow(String row, ArrayList<Label> labels) {
		String[] splitString = row.split(" ");

		Command.TypeM commandType = getTypeMCommandTypeFromString(splitString[0]);

		if(commandType == Command.TypeM.None) {
			return 0x0;
		}

		short command;
		String firstPart = Integer.toHexString(getMTypeCommandFirstPart(splitString, commandType));
		String secondPart = Integer.toHexString(getMTypeCommandSecondPart(splitString, labels));

		command = (short)Integer.parseInt(firstPart + secondPart, 16);
		return command;
	}

	/*
	 * Get first part of M command (command type)
	 */
	private short getMTypeCommandFirstPart(String[] splitString, Command.TypeM commandType) {
		short firstPart = 0;

		try {
			if(isCommandDirect(splitString)) {
				firstPart = Command.typeMcommandsDirect.get(commandType);
			} else {
				firstPart = Command.typeMcommandsIndirect.get(commandType);
			}
		} catch (Exception ex) {
			Assembler.printErrorAndExit(4);
		}

		return firstPart;
	}

	/*
	 * Get second part of M command (command parameter)
	 */
	private short getMTypeCommandSecondPart(String[] splitString, ArrayList<Label> labels) {
		short secondPart = -1;

		if(Character.isDigit(splitString[1].charAt(0))) {
			try {
				secondPart = Short.parseShort(splitString[1], 16);
			} catch (Exception ex) {
				Assembler.printErrorAndExit(7);
			}
		} else {
			for(int i = 0; i < labels.size(); i++) {
				Label label = labels.get(i);

				if(label.name.equals(splitString[1])) {
					secondPart = label.memorySlot;
				}
			}
		}

		if(secondPart == -1) {
			Assembler.printErrorAndExit(4);
		}

		return secondPart;
	}

	/*
	 * Parse HEX or DEC pseudocommand from a row and return comamand as integer
	 */
	public short getPseudoCommandFromRow(String row) {
		String[] splitString = row.split(" ");

		short command = 0x0;

		// First we parse command to int to seperate invalid number
		// exception from too big number exception
		int intCmd = 0x0;

		try {
			if(splitString[0].equals("HEX")) {
				intCmd = Integer.parseInt(splitString[1], 16);
			} else if(splitString[0].equals("DEC")) {
				intCmd = Integer.parseInt(splitString[1], 10);
			} else {
				Assembler.printErrorAndExit(3);
			}
		} catch (NumberFormatException ex) {
			Assembler.printErrorAndExit(7);
		}

		if(intCmd > Short.MAX_VALUE) {
			Assembler.printErrorAndExit(6);
		} else {
			command = (short)intCmd;
		}

		return command;
	}

	/* Get label from string
	*
	* example string: A, DEC 25
	* name: A
	* command: DEC 25
	*/
	public Label getLabelFromString(String string) {
		Label label = new Label();

		String[] splitString = string.split(",");

		label.name = splitString[0];
		label.command = splitString[1];

		if(label.name.length() > 3) {
			Assembler.printErrorAndExit(2);
		}

		return label;
	}
}
