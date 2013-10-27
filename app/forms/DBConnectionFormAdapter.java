package forms;

import javax.inject.Inject;

import models.DBDriver;
import services.DriverService;

public class DBConnectionFormAdapter implements
		Adapter<DBConnection, models.DBConnection> {

	final DriverService driverService;

	@Inject
	public DBConnectionFormAdapter(DriverService driverService) {
		this.driverService = driverService;
	}

	@Override
	public DBConnection toForm(models.DBConnection entity) {
		DBConnection form = new DBConnection();
		form.driverId = entity.getDriver() != null ? entity.getDriver().getId()
				: null;
		form.name = entity.getName();
		form.url = entity.getUrl();
		form.username = entity.getUsername();
		form.password = entity.getPassword();
		return form;
	}

	@Override
	public models.DBConnection toNewEntity(DBConnection form) throws Exception {
		models.DBConnection entity = new models.DBConnection();
		return toEntity(form, entity);
	}

	@Override
	public models.DBConnection toEntity(DBConnection form,
			models.DBConnection entity) throws Exception {
		entity.setName(form.name);
		entity.setUrl(form.url);
		entity.setUsername(form.username);
		entity.setPassword(form.password);
		entity.setDriver(findDriverById(form.driverId));
		return entity;
	}

	private DBDriver findDriverById(Long driverId) {
		return driverService.getDriverById(driverId);
	}

}
