package services;

import java.util.List;

import models.DBDriver;

import com.avaje.ebean.Ebean;

public class DriverService {

	public DBDriver getDriverById(Long id) {
		return Ebean.find(DBDriver.class, id);
	}

	public void save(DBDriver dbdriver) {
		Ebean.save(dbdriver);
	}

	public List<DBDriver> findAll() {
		return Ebean.find(DBDriver.class).findList();
	}
}
