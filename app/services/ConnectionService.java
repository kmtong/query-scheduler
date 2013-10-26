package services;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import models.DBConnection;

public class ConnectionService {

	private static final String USER_PROP_NAME = "user";
	private static final String PASSWORD_PROP_NAME = "password";

	public Connection getConnection(DBConnection conn) throws Exception {
		return newConnection(conn.getDriver().getDriver(), conn.getDriver()
				.getDriverClass(), conn.getUrl(), conn.getUsername(),
				conn.getPassword());
	}

	@SuppressWarnings({ "resource", "rawtypes" })
	protected Connection newConnection(byte[] driverContent,
			String driverClassName, String jdbcurl, String username,
			String password) throws IOException, ClassNotFoundException,
			InstantiationException, IllegalAccessException, SQLException {

		ClassLoader loader = new JarByteClassLoader(new URL[0], Thread
				.currentThread().getContextClassLoader(), driverContent);
		Class driverClass = loader.loadClass(driverClassName);
		Driver sqlDriver = (Driver) driverClass.newInstance();
		Properties p = new Properties();
		p.setProperty(USER_PROP_NAME, username);
		p.setProperty(PASSWORD_PROP_NAME, password);
		Connection sqlconn = sqlDriver.connect(jdbcurl, p);
		return sqlconn;

	}
}
