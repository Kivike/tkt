import java.util.HashMap;
import java.util.Map;


public class Command {
	
	public static final HashMap<String, Integer> symbols = new HashMap<String, Integer>();
	
	static {
		symbols.put("AND", 0x0);
		symbols.put("ADD", 0x1);
		symbols.put("LDA", 0x2);
		symbols.put("STA", 0x3);
		symbols.put("BUN", 0x4);
		symbols.put("BSA", 0x5);
		symbols.put("ISZ", 0x6);
		
		symbols.put("CLA", 0x7800);
		symbols.put("CLE", 0x7400);
		symbols.put("CMA", 0x7200);
		symbols.put("CME", 0x7100);
		symbols.put("CIR", 0x7080);
		symbols.put("CIL", 0x7040);
		symbols.put("INC", 0x7020);
		symbols.put("SPA", 0x7010);
		symbols.put("SNA", 0x7008);
		symbols.put("SZA", 0x7004);
		symbols.put("SZE", 0x7002);
		symbols.put("HLT", 0x7001);
		
		symbols.put("INP", 0xF800);
		symbols.put("OUT", 0xF400);
		symbols.put("SKI", 0xF200);
		symbols.put("SKO", 0xF100);
		symbols.put("ION", 0xF080);
		symbols.put("IOF", 0xF040);
	}
	
	/*public static final int AND = 0x0000;
	public static final int ADD = 0x1000;
	public static final int LDA = 0x2000;
	public static final int STA = 0x3000;
	public static final int BUN = 0x4000;
	public static final int BSA = 0x5000;
	public static final int ISZ = 0x6000;
	
	public static final int AND1 = 0x8000;
	public static final int ADD1 = 0x9000;
	public static final int LDA1 = 0xA000;
	public static final int STA1 = 0xB000;
	public static final int BUN1 = 0xC000;
	public static final int BSA1 = 0xD000;
	public static final int ISZ1 = 0xE000;
	
	public static final int CLA = 0x7800;
	public static final int CLE = 0x7400;
	public static final int CMA = 0x7200;
	public static final int CME = 0x7100;
	public static final int CIR = 0x7080;
	public static final int CIL = 0x7040;
	public static final int INC = 0x7020;
	public static final int SPA = 0x7010;
	public static final int SNA = 0x7008;
	public static final int SZA = 0x7004;
	public static final int SZE = 0x7002;
	public static final int HLT = 0x7001;
	
	public static final int INP = 0xF800;
	public static final int OUT = 0xF400;
	public static final int SKI = 0xF200;
	public static final int SKO = 0xF100;
	public static final int ION = 0xF080;
	public static final int IOF = 0xF040;
	*/
}
