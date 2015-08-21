import java.util.HashMap;

/*
 * Database of command types and their hex values
 */
public class Command {
	// Pseudo commands
	public enum Pseudo {
		None,
		ORG, END, DEC, HEX
	}

	// Type M commands
	public enum TypeM {
		None,
		AND, ADD, LDA, STA, BUN, BSA, ISZ,
	}

	// Type R commands
	public enum TypeR {
		None,
		CLA, CLE, CMA, CME, CIR, CIL, INC, SPA, SNA, SZA, SZE, HLT
	}

	// Type IO commands
	public enum TypeIO {
		None,
		INP, OUT, SKI, SKO, ION, IOF
	}

	// M type commands only have their first byte known
	// Rest are given in second part of command
	public static final HashMap<TypeM, Short> typeMcommandsDirect = new HashMap<TypeM, Short>();
	public static final HashMap<TypeM, Short> typeMcommandsIndirect = new HashMap<TypeM, Short>();

	public static final HashMap<TypeR, Short> typeRcommands = new HashMap<TypeR, Short>();

	public static final HashMap<TypeIO, Short> typeIOcommands = new HashMap<TypeIO, Short>();

	static {
		typeMcommandsDirect.put(TypeM.AND, (short) 0x0);
		typeMcommandsDirect.put(TypeM.ADD, (short) 0x1);
		typeMcommandsDirect.put(TypeM.LDA, (short) 0x2);
		typeMcommandsDirect.put(TypeM.STA, (short) 0x3);
		typeMcommandsDirect.put(TypeM.BUN, (short) 0x4);
		typeMcommandsDirect.put(TypeM.BSA, (short) 0x5);
		typeMcommandsDirect.put(TypeM.ISZ, (short) 0x6);

		typeMcommandsIndirect.put(TypeM.AND, (short) 0x8);
		typeMcommandsIndirect.put(TypeM.ADD, (short) 0x9);
		typeMcommandsIndirect.put(TypeM.LDA, (short) 0xA);
		typeMcommandsIndirect.put(TypeM.STA, (short) 0xB);
		typeMcommandsIndirect.put(TypeM.BUN, (short) 0xC);
		typeMcommandsIndirect.put(TypeM.BSA, (short) 0xD);
		typeMcommandsIndirect.put(TypeM.ISZ, (short) 0xE);

		typeRcommands.put(TypeR.CLA, (short) 0x7800);
		typeRcommands.put(TypeR.CLE, (short) 0x7400);
		typeRcommands.put(TypeR.CMA, (short) 0x7200);
		typeRcommands.put(TypeR.CME, (short) 0x7100);
		typeRcommands.put(TypeR.CIR, (short) 0x7080);
		typeRcommands.put(TypeR.CIL, (short) 0x7040);
		typeRcommands.put(TypeR.INC, (short) 0x7020);
		typeRcommands.put(TypeR.SPA, (short) 0x7010);
		typeRcommands.put(TypeR.SNA, (short) 0x7008);
		typeRcommands.put(TypeR.SZA, (short) 0x7004);
		typeRcommands.put(TypeR.SZE, (short) 0x7002);
		typeRcommands.put(TypeR.HLT, (short) 0x7001);

		typeIOcommands.put(TypeIO.INP, (short) 0xF800);
		typeIOcommands.put(TypeIO.OUT, (short) 0xF400);
		typeIOcommands.put(TypeIO.SKI, (short) 0xF200);
		typeIOcommands.put(TypeIO.SKO, (short) 0xF100);
		typeIOcommands.put(TypeIO.ION, (short) 0xF080);
		typeIOcommands.put(TypeIO.IOF, (short) 0xF040);
	}
}
