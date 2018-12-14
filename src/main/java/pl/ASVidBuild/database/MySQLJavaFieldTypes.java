package pl.ASVidBuild.database;

public class MySQLJavaFieldTypes {
	public static final String intAsINT = "INT";
	public static final String byteAsTINYINT = "TINYINT";
	public static final String booleanASBOOLEAN = "BOOLEAN";
	
	
	
	public static String StringAsVarchar(int length) {
		return "VARCHAR("+ length+")";
	}
	
	public static String doubleAsDecimal(int maxDigitsCount, int maxFractalDigitsCount) {
		return "DECIMAL(" + maxDigitsCount + "," + maxFractalDigitsCount+")";
	}
	
	
}
