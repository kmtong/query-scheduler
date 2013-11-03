package services;

import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import models.DBDriver;

import com.avaje.ebean.Ebean;

public class DriverService {

	final JarByteClassLoaderCache loaderCache;

	@Inject
	public DriverService(JarByteClassLoaderCache loaderCache) {
		this.loaderCache = loaderCache;
	}

	public DBDriver getDriverById(Long id) {
		return Ebean.find(DBDriver.class, id);
	}

	public void save(DBDriver dbdriver) {
		Ebean.save(dbdriver);
		loaderCache.expire(getKey(dbdriver));
	}

	public List<DBDriver> findAll() {
		return Ebean.find(DBDriver.class).findList();
	}

	public ClassLoader getDriverClassLoader(final DBDriver driver)
			throws Exception {
		return loaderCache.getOrElse(getKey(driver),
				new Callable<JarByteClassLoader>() {
					public JarByteClassLoader call() throws Exception {
						JarByteClassLoader loader = new JarByteClassLoader(
								new URL[0], Thread.currentThread()
										.getContextClassLoader(), driver
										.getDriver());
						return loader;
					}
				});
	}

	protected String getKey(DBDriver conn) {
		return String.valueOf(conn.getId());
	}

}
