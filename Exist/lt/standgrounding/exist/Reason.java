package lt.standgrounding.exist;

public enum Reason {
	KILLAURA_ANGULARVELOCITY, KILLAURA_MULTIHIT, KILLAURAA_CROSSHAIR, KILLAURA_KNOCKBACK, XRAY, MACRO, FLY, TP, FASTPLACER, NUKE;
	protected static String[] H = new String[10];
	@Override
	public String toString() throws IllegalArgumentException{
		switch(this){
		case FASTPLACER:
			return "[Exist] " + H[8];
		case FLY:
			return "[Exist] " + H[6];
		case KILLAURAA_CROSSHAIR:
			return "[Exist] " + H[2];
		case KILLAURA_ANGULARVELOCITY:
			return "[Exist] " + H[0];
		case KILLAURA_KNOCKBACK:
			return "[Exist] " + H[3];
		case KILLAURA_MULTIHIT:
			return "[Exist] " + H[1];
		case MACRO:
			return "[Exist] " + H[5];
		case NUKE:
			return "[Exist] " + H[9];
		case TP:
			return "[Exist] " + H[7];
		case XRAY:
			return "[Exist] " + H[4];
		default:
			throw new IllegalArgumentException("Illegal argument. Contact the developer of this plugin");
		}
	}
	public static void passMessage(String msg, int ArrayNum){
		H[ArrayNum] = msg;
	}
}
