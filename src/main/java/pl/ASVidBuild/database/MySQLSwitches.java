package pl.ASVidBuild.database;

public class MySQLSwitches {

	public static String oneToOneRelation(String parentTabName, boolean onDeleteCascade) {
		String result = ", FOREIGN KEY(id) REFERENCES " + parentTabName + "(id)";
		if (onDeleteCascade) {
			result = result + " ON DELETE CASCADE";
		}
		return result;
	}

	public static String oneToManyRelation(String parentTabName) {
		return ", " + parentTabName + "_id INT NOT NULL, FOREIGN KEY(" + parentTabName + "_id) REFERENCES "
				+ parentTabName + "(id)";
	}

	public static String manyToManyRelation(String TabName1, String TabName2) {
		return TabName1 + "_id INT NOT NULL, " + TabName2 + "_id INT NOT NULL, FOREIGN KEY(" + TabName1
				+ "_id) REFERENCES " + TabName1 + "(id), FOREIGN KEY("+TabName2+"_id) REFERENCES "+TabName2+"(id)";
	}

	public static String tabField(String fieldName, String javaFieldType, boolean unique, boolean valueRequired) {
		
		String result = fieldName + " " + javaFieldType;
		if(unique) {
			result += " UNIQUE";
		}
		if(valueRequired) {
			result +=" NOT NULL";
		}
		
		
		return result;
	}
	
	public static String tabField(String fieldName, String javaFieldType, boolean unique, boolean valueRequired, String defaultValue) {
		return tabField( fieldName, javaFieldType, unique, valueRequired)+" DEFAULT "+ defaultValue;
	}
	
}
