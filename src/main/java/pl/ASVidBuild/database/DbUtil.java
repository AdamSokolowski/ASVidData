package pl.ASVidBuild.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil implements DbSet{
	private static Connection conn;

	public static Connection getConn() throws SQLException {
		return getInstance();
	}

	private static Connection getInstance() throws SQLException {
		if ((conn == null) || (conn.isClosed())) {
			try {
				conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return conn;
	}
}
