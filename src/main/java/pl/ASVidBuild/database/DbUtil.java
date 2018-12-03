package pl.ASVidBuild.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.TimeZone;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DbUtil {
	private static Connection ds;

	public static Connection getConn() throws SQLException {
		return getInstance();
	}

	private static Connection getInstance() {
		if (ds == null) {
			try {
				ds = DriverManager.getConnection(DbRepository.DB_URL, DbRepository.DB_USER, DbRepository.DB_PASS);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/asviddata");
			} catch (NamingException e) {
				e.printStackTrace();
			}*/
		}
		return ds;
	}
}