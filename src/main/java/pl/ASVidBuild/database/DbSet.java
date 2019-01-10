package pl.ASVidBuild.database;

public interface DbSet {
	public static final String DB_NAME = "asviddata";
	public static final String DB_URL = "jdbc:mysql://localhost:3306/"+DB_NAME+"?useSSL=false&useUnicode=true&characterEncoding=utf8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT";
	public static final String DB_USER = "root";
	public static final String DB_PASS = "coderslab";
}
