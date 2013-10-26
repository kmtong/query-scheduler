package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class DBDriver {

	@Id
	long id;

	String name;

	String driverClass;

	@Lob
	byte[] driver;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public byte[] getDriver() {
		return driver;
	}

	public void setDriver(byte[] driver) {
		this.driver = driver;
	}

}
