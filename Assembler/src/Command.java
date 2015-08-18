import java.util.HashMap;

/*
 * Database of command types and their hex values
 */
public class Command {
	// Type of command
	public enum Type {
		None,
		Pseudo, M, R, IO
	}

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
	public static final HashMap<TypeM, Short> typeMsymbolsDirect = new HashMap<TypeM, Short>();
	public static final HashMap<TypeM, Short> typeMsymbolsIndirect = new HashMap<TypeM, Short>();

	public static final HashMap<TypeR, Short> typeRsymbols = new HashMap<TypeR, Short>();

	public static final HashMap<TypeIO, Short> typeIOsymbols = new HashMap<TypeIO, Short>();

	static {
		typeMsymbolsDirect.put(TypeM.AND, (short)0x0);
		typeMsymbolsDirect.put(TypeM.ADD, (short)0x1);
		typeMsymbolsDirect.put(TypeM.LDA, (short)0x2);
		typeMsymbolsDirect.put(TypeM.STA, (short)0x3);
		typeMsymbolsDirect.put(TypeM.BUN, (short)0x4);
		typeMsymbolsDirect.put(TypeM.BSA, (short)0x5);
		typeMsymbolsDirect.put(TypeM.ISZ, (short)0x6);

		typeMsymbolsIndirect.put(TypeM.AND, (short)0x8);
		typeMsymbolsIndirect.put(TypeM.ADD, (short)0x9);
		typeMsymbolsIndirect.put(TypeM.LDA, (short)0xA);
		typeMsymbolsIndirect.put(TypeM.STA, (short)0xB);
		typeMsymbolsIndirect.put(TypeM.BUN, (short)0xC);
		typeMsymbolsIndirect.put(TypeM.BSA, (short)0xD);
		typeMsymbolsIndirect.put(TypeM.ISZ, (short)0xE);

		typeRsymbols.put(TypeR.CLA, (short)0x7800);
		typeRsymbols.put(TypeR.CLE, (short)0x7400);
		typeRsymbols.put(TypeR.CMA, (short)0x7200);
		typeRsymbols.put(TypeR.CME, (short)0x7100);
		typeRsymbols.put(TypeR.CIR, (short)0x7080);
		typeRsymbols.put(TypeR.CIL, (short)0x7040);
		typeRsymbols.put(TypeR.INC, (short)0x7020);
		typeRsymbols.put(TypeR.SPA, (short)0x7010);
		typeRsymbols.put(TypeR.SNA, (short)0x7008);
		typeRsymbols.put(TypeR.SZA, (short)0x7004);
		typeRsymbols.put(TypeR.SZE, (short)0x7002);
		typeRsymbols.put(TypeR.HLT, (short)0x7001);

		typeIOsymbols.put(TypeIO.INP, (short)0xF800);
		typeIOsymbols.put(TypeIO.OUT, (short)0xF400);
		typeIOsymbols.put(TypeIO.SKI, (short)0xF200);
		typeIOsymbols.put(TypeIO.SKO, (short)0xF100);
		typeIOsymbols.put(TypeIO.ION, (short)0xF080);
		typeIOsymbols.put(TypeIO.IOF, (short)0xF040);
	}
}
