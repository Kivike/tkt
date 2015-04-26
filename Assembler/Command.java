
public class Command {
	public static final int AND = 0x0000;
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
}
