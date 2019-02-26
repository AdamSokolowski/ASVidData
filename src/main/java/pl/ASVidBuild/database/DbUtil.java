package pl.ASVidBuild.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil implements DbSet {
	private static Connection conn;
	private static int connectIteration = 0;

	public static Connection getConn() throws SQLException {
		return getInstance();
	}

	private static Connection getInstance() throws SQLException {
		connectIteration++;
		if ((conn == null) || (conn.isClosed())) {
			try {
				conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			} catch (SQLException e) {
				connectIteration = 0;
				e.printStackTrace();
			}
		}
		return conn;
	}

	public static void releaseConnection() {
		if (connectIteration == 1) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("Failed to close database connection.");
				e.printStackTrace();
			}
		}
		if (connectIteration > 0) {
			connectIteration--;
		}
	}
}
