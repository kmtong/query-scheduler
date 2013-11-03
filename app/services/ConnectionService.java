package services;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;

import models.DBConnection;

import com.avaje.ebean.Ebean;

public class ConnectionService {

	private static final String USER_PROP_NAME = "user";
	private static final String PASSWORD_PROP_NAME = "password";

	final DriverService driverService;

	@Inject
	public ConnectionService(DriverService driverService) {
		this.driverService = driverService;
	}

	public List<DBConnection> findAll() {
		return Ebean.find(DBConnection.class).findList();
	}

	public DBConnection getConnectionById(Long connId) {
		return Ebean.find(DBConnection.class, connId);
	}

	public void save(DBConnection dbconn) {
		Ebean.save(dbconn);
	}

	public Connection getConnection(DBConnection conn) throws Exception {

		ClassLoader driverLoader = driverService.getDriverClassLoader(conn
				.getDriver());
		return newConnection(driverLoader, conn.getDriver().getDriverClass(),
				conn.getUrl(), conn.getUsername(), conn.getPassword());
	}

	@SuppressWarnings({ "rawtypes" })
	protected Connection newConnection(ClassLoader loader,
			String driverClassName, String jdbcurl, String username,
			String password) throws IOException, ClassNotFoundException,
			InstantiationException, IllegalAccessException, SQLException {

		Class driverClass = loader.loadClass(driverClassName);
		Driver sqlDriver = (Driver) driverClass.newInstance();
		Properties p = new Properties();
		p.setProperty(USER_PROP_NAME, username);
		p.setProperty(PASSWORD_PROP_NAME, password);
		Connection sqlconn = sqlDriver.connect(jdbcurl, p);
		return sqlconn;

	}

}
