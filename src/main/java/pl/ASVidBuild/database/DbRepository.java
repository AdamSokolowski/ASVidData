package pl.ASVidBuild.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbRepository {

	private static Connection conn = null;

	public static boolean createMainDatabase() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306?useSSL=false&useUnicode=true&characterEncoding=utf8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT",
					DbUtil.DB_USER, DbUtil.DB_PASS);
			Statement statement = conn.createStatement();
			statement.executeUpdate("CREATE DATABASE " + DbUtil.DB_NAME);
			conn.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void initDatabaseTables() {
		try {

			conn = DbUtil.getConn();

			if (tableExists("MediaFile", conn)) {
				System.out.println("Table 'MediaFile' found in database");
			} else {
				String sqlTabCreateMediaFile = mySQLQueryGeneratorCreateTable("MediaFile",
						MySQLSwitches.tabField("filePath", MySQLJavaFieldTypes.StringAsVarchar(400), true, true) + ", "
								+ MySQLSwitches.tabField("picturePath", MySQLJavaFieldTypes.StringAsVarchar(400), false,
										false, "''")
								+ ", " + MySQLSwitches.tabField("fileRating", MySQLJavaFieldTypes.byteAsTINYINT, false,
										false, "0"),
						"");
				Statement statement = conn.createStatement();
				statement.execute(sqlTabCreateMediaFile);
				System.out.println(sqlTabCreateMediaFile);
			}
			if (tableExists("Tag", conn)) {
				System.out.println("Table 'Tag' found in database");
			} else {
				String sqlTabCreateTag = mySQLQueryGeneratorCreateTable("Tag",
						MySQLSwitches.tabField("tagName", MySQLJavaFieldTypes.StringAsVarchar(100), true, true) + ", "
								+ MySQLSwitches.tabField("picturePath", MySQLJavaFieldTypes.StringAsVarchar(400), false,
										false),
						"");
				Statement statement = conn.createStatement();
				statement.execute(sqlTabCreateTag);
				System.out.println(sqlTabCreateTag);
			}
			if (tableExists("MediaFile_Tag", conn)) {
				System.out.println("Table 'MediaFile_Tag' found in database");
			} else {
				String sqlTabCreateMediaFile_Tag = mySQLQueryGeneratorCreateTable("MediaFile_Tag", "",
						MySQLSwitches.manyToManyRelation("MediaFile", "Tag"));
				Statement statement = conn.createStatement();
				statement.execute(sqlTabCreateMediaFile_Tag);
				System.out.println(sqlTabCreateMediaFile_Tag);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static boolean tableExists(String tableName, Connection databaseConnection) throws SQLException {
		DatabaseMetaData dbmeta = databaseConnection.getMetaData();
		ResultSet tables = dbmeta.getTables(null, null, tableName, null);
		if (tables.next()) {
			return true;
		}
		return false;
	}

	public static String mySQLQueryGeneratorCreateTable(String tableName, String tabFields, String relation) {
		return "CREATE TABLE " + tableName + " (id INT AUTO_INCREMENT PRIMARY KEY, " + tabFields + relation + ")";
	}

	public static String mySQLQueryGeneratorAddRecord(String tableName, String tabFields, String tabValues) {
		return "INSERT INTO " + tableName + "(" + tabFields + ") VALUES(" + tabValues + ")";
	}

	public static void mySQLAddRecord(String tableName, String tabFields, String tabValues,
			Connection databaseConnection) throws SQLException {
		String[] tabValuesArray = tabValues.split(", ");
		String sql = "INSERT INTO " + tableName + "(" + tabFields + ") VALUES(" + valuesQuestionMarks(tabValuesArray)
				+ ")";
		System.out.println(sql);
		PreparedStatement stmt = databaseConnection.prepareStatement(sql);
		for (int i = 0; i < tabValuesArray.length; i++) {
			stmt.setString(i + 1, tabValuesArray[i]);
		}
		stmt.executeUpdate();
	}

	public static void mySQLUpdateRecord(String tableName, String tabFields, String tabValues,
			String conditionArguments, Connection databaseConnection) throws SQLException {
		if (conditionArguments.indexOf(";") == -1) {		//checking if condition might have SQL Injection
			String[] tabFieldsArray = tabFields.split(", ");
			String[] tabValuesArray = tabValues.split(", ");
			String sql = "UPDATE " + tableName + " SET ";
			for (int i = 0; i < tabFieldsArray.length - 1; i++) {
				sql += tabFieldsArray[i] + "=?, ";
			}
			sql += tabFieldsArray[tabFieldsArray.length - 1] + "=? WHERE " + conditionArguments;
			System.out.println(sql);
			PreparedStatement stmt = databaseConnection.prepareStatement(sql);
			for (int i = 0; i < tabValuesArray.length; i++) {
				stmt.setString(i + 1, tabValuesArray[i]);
			}
			stmt.executeUpdate();
		} else {
			throw new SQLException();
		}

	}

	private static String valuesQuestionMarks(String[] tabValues) {
		String valuesQuestionMarks = "";
		int i = 0;
		while (i < tabValues.length - 1) {
			valuesQuestionMarks += "?,";
		}
		valuesQuestionMarks += "?";
		return valuesQuestionMarks;
	}

	public static String mySQLQueryGeneratorUpdateRecordValues(String tableName, String tabFields, String tabValues,
			String conditionArguments) {
		String[] tabFieldsArray = tabFields.split(", ");
		String[] tabValuesArray = tabValues.split(", ");
		String sql = "UPDATE " + tableName + " SET ";
		for (int i = 0; i < tabFieldsArray.length - 1; i++) {
			sql += tabFieldsArray[i] + "=" + tabValuesArray[i] + ", ";
		}
		sql += tabFieldsArray[tabFieldsArray.length - 1] + "=" + tabValuesArray[tabFieldsArray.length - 1] + " WHERE "
				+ conditionArguments;
		return sql;
	}
}
