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
			} catch(Exception ex){
				// TODO: THROW ERROR
			}
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
		Command.Type commandType = getCommandType(row);
		short command = 0;

		if(commandType == Command.Type.Pseudo) {
			command = getPseudoCommandFromRow(row);
		} else if(commandType == Command.Type.M) {
			command = getTypeMCommandFromRow(row, labels);
		} else if(commandType == Command.Type.R) {
			command = getTypeRCommandFromRow(row);
		} else if(commandType == Command.Type.IO) {
			command = getTypeIOCommandFromRow(row);
		} else {
			// Variable
			for(int j = 0; j < labels.size(); j++) {
				if(labels.get(j).name.equals(row)) {
					command = labels.get(j).memorySlot;
				}
			}

			if(command == 0) {
				System.exit(8);
			}
		}

		return command;
	}

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
	 * Get type of the command from row (Pseudo, M, R, IO)
	 */
	public Command.Type getCommandType(String row) {
		// Check if command is of type M
		for(Command.TypeM typeM : Command.TypeM.values()) {
			if(row.startsWith(typeM.name())) {
				return Command.Type.M;
			}
		}

		// Check if command is of type R
		for(Command.TypeR typeR : Command.TypeR.values()) {
			if(row.startsWith(typeR.name())) {
				return Command.Type.R;
			}
		}

		// Check if command is of type IO
		for(Command.TypeIO typeIO : Command.TypeIO.values()) {
			if(row.startsWith(typeIO.name())) {
				return Command.Type.IO;
			}
		}

		// Check if command is a pseudo comand
		for(Command.Pseudo pseudo : Command.Pseudo.values()) {
			if(row.startsWith(pseudo.name())) {
				return Command.Type.Pseudo;
			}
		}

		// If no command was found, return none as command type
		return Command.Type.None;
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

	// Get type of R command
	private Command.TypeR getTypeRCommandTypeFromString(String string) {
		for(Command.TypeR typeR : Command.TypeR.values()) {
			if(string.startsWith(typeR.name())) {
				return typeR;
			}
		}

		return Command.TypeR.None;
	}

	/*
	 * Get type of IO command
	 */
	private Command.TypeIO getTypeIOCommandTypeFromString(String string) {
		for(Command.TypeIO typeIO : Command.TypeIO.values()) {
			if(string.startsWith(typeIO.name())) {
				return typeIO;
			}
		}

		return Command.TypeIO.None;
	}

	/*
	 * Get type of pseudocommand
	 */
	public Command.Pseudo getPseudoCommandTypeFromString(String string) {
		for(Command.Pseudo pseudo : Command.Pseudo.values()) {
			if(string.startsWith(pseudo.name())) {
				return pseudo;
			}
		}

		return Command.Pseudo.None;
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

		if(Assembler.debug) System.out.println("Mtype, First:" + firstPart + " Second:" + secondPart);

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
				firstPart = Command.typeMsymbolsDirect.get(commandType);
			} else {
				firstPart = Command.typeMsymbolsIndirect.get(commandType);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(4);
		}

		return firstPart;
	}

	/*
	 * Get second part of M command (command parameter)
	 */
	private short getMTypeCommandSecondPart(String[] splitString, ArrayList<Label> labels) {
		short secondPart = 0;

		if(Character.isDigit(splitString[1].charAt(0))) {
			try {
				secondPart = Short.parseShort(splitString[1], 16);
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(7);
			}
		} else {
			try {
				for(int i = 0; i < labels.size(); i++) {
					Label label = labels.get(i);

					if(label.name.equals(splitString[1])) {
						secondPart = label.memorySlot;
					}
				}
			} catch(Exception ex) {
				// TODO: Throw error
			}
		}

		return secondPart;
	}
	/*
	 * Parse type R command from a row and return command as integer
	 */
	public short getTypeRCommandFromRow(String row) {
		Command.TypeR commandType = getTypeRCommandTypeFromString(row);

		return Command.typeRsymbols.get(commandType);
	}

	/*
	 * Parse HEX or DEC pseudocommand from a row and return comamand as integer
	 */
	public short getPseudoCommandFromRow(String row) {
		String[] splitString = row.split(" ");

		short command = 0x0;

		if(splitString[0].equals("HEX")) {
			command = Short.parseShort(splitString[1], 16);
		} else if(splitString[0].equals("DEC")) {
			command = Short.parseShort(splitString[1], 10);
		} else {
			System.exit(3);
		}

		return command;
	}

	/*
	 * Parse type IO command from a row and return command as integer
	 */
	public short getTypeIOCommandFromRow(String row) {
		Command.TypeIO commandType = getTypeIOCommandTypeFromString(row);

		return Command.typeIOsymbols.get(commandType);
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

		return label;
	}
}
