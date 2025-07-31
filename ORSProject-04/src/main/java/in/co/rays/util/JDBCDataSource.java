package in.co.rays.util;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public final class JDBCDataSource {

	private JDBCDataSource() {

	}

	private static JDBCDataSource dataSource = null;
	private static ComboPooledDataSource cpds = null;

	private static JDBCDataSource getInstance() {
		if (dataSource == null) {

			dataSource = new JDBCDataSource();
			dataSource.cpds = new ComboPooledDataSource();
			ResourceBundle rb = ResourceBundle.getBundle("in.co.rays.bundle.system");

			try {
				dataSource.cpds.setDriverClass(rb.getString("driver"));
				dataSource.cpds.setJdbcUrl(rb.getString("url"));
				dataSource.cpds.setUser(rb.getString("username"));
				dataSource.cpds.setPassword(rb.getString("password"));
				dataSource.cpds.setAcquireIncrement(Integer.parseInt(rb.getString("acquireIncrement")));
				dataSource.cpds.setInitialPoolSize(Integer.parseInt(rb.getString("intialPoolSize")));
				dataSource.cpds.setMaxPoolSize(Integer.parseInt(rb.getString("maxPoolSize")));
				dataSource.cpds.setMinPoolSize(Integer.parseInt(rb.getString("minPoolSize")));
				dataSource.cpds.setMaxIdleTime(Integer.parseInt(rb.getString("timeout")));
				;
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		}

		return dataSource;
	}

	public static Connection getConnection() {
		try {
			return getInstance().cpds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeConnection(ResultSet rs, PreparedStatement pstmt) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
