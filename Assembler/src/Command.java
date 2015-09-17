import java.util.HashMap;

/*
 * Database of command types and their hex values
 */
public class Command {
	public enum Pseudo {
		None,
		ORG, END, DEC, HEX
	}

	public enum TypeM {
		None,
		AND, ADD, LDA, STA, BUN, BSA, ISZ,
	}

	public enum TypeR {
		None,
		CLA, CLE, CMA, CME, CIR, CIL, INC, SPA, SNA, SZA, SZE, HLT
	}

	public enum TypeIO {
		None,
		INP, OUT, SKI, SKO, ION, IOF
	}

	// M type commands only have their first byte known
	// Rest are given in second part of command
	public static final HashMap<TypeM, Short> MDirect = new HashMap<TypeM, Short>();
	public static final HashMap<TypeM, Short> MIndirect = new HashMap<TypeM, Short>();

	public static final HashMap<TypeR, Short> R = new HashMap<TypeR, Short>();

	public static final HashMap<TypeIO, Short> IO = new HashMap<TypeIO, Short>();

	static {
		MDirect.put(TypeM.AND, (short) 0x0);
		MDirect.put(TypeM.ADD, (short) 0x1);
		MDirect.put(TypeM.LDA, (short) 0x2);
		MDirect.put(TypeM.STA, (short) 0x3);
		MDirect.put(TypeM.BUN, (short) 0x4);
		MDirect.put(TypeM.BSA, (short) 0x5);
		MDirect.put(TypeM.ISZ, (short) 0x6);

		MIndirect.put(TypeM.AND, (short) 0x8);
		MIndirect.put(TypeM.ADD, (short) 0x9);
		MIndirect.put(TypeM.LDA, (short) 0xA);
		MIndirect.put(TypeM.STA, (short) 0xB);
		MIndirect.put(TypeM.BUN, (short) 0xC);
		MIndirect.put(TypeM.BSA, (short) 0xD);
		MIndirect.put(TypeM.ISZ, (short) 0xE);

		R.put(TypeR.CLA, (short) 0x7800);
		R.put(TypeR.CLE, (short) 0x7400);
		R.put(TypeR.CMA, (short) 0x7200);
		R.put(TypeR.CME, (short) 0x7100);
		R.put(TypeR.CIR, (short) 0x7080);
		R.put(TypeR.CIL, (short) 0x7040);
		R.put(TypeR.INC, (short) 0x7020);
		R.put(TypeR.SPA, (short) 0x7010);
		R.put(TypeR.SNA, (short) 0x7008);
		R.put(TypeR.SZA, (short) 0x7004);
		R.put(TypeR.SZE, (short) 0x7002);
		R.put(TypeR.HLT, (short) 0x7001);

		IO.put(TypeIO.INP, (short) 0xF800);
		IO.put(TypeIO.OUT, (short) 0xF400);
		IO.put(TypeIO.SKI, (short) 0xF200);
		IO.put(TypeIO.SKO, (short) 0xF100);
		IO.put(TypeIO.ION, (short) 0xF080);
		IO.put(TypeIO.IOF, (short) 0xF040);
	}
}
