package controllers;

import java.util.List;

import javax.inject.Inject;

import models.DBDriver;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import services.DriverService;
import views.html.driver.edit;
import views.html.driver.index;
import forms.DBDriverFormAdapter;

public class DriverController extends Controller {

	final DriverService driverService;
	final DBDriverFormAdapter driverAdapter;

	@Inject
	public DriverController(final DriverService driverService,
			final DBDriverFormAdapter driverAdapter) {
		this.driverService = driverService;
		this.driverAdapter = driverAdapter;
	}

	public Result index() {
		List<DBDriver> drivers = driverService.findAll();
		return ok(index.render(drivers));
	}

	public Result load(Long driverId) {
		Form<forms.DBDriver> form = Form.form(forms.DBDriver.class);
		if (driverId != null && driverId != 0) {
			DBDriver dbdriver = driverService.getDriverById(driverId);
			form = form.fill(driverAdapter.toForm(dbdriver));
		}
		return ok(edit.render(driverId, form));
	}

	public Result save(Long driverId) throws Exception {
		Form<forms.DBDriver> driverForm = Form.form(forms.DBDriver.class)
				.bindFromRequest();
		forms.DBDriver driver = driverForm.get();
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart jarFilePart = body.getFile("jarFile");
		if (jarFilePart != null) {
			driver.jarFile = jarFilePart.getFile();
		}
		Logger.debug("Driver Name: " + driver.name);
		Logger.debug("Driver Class: " + driver.driverClass);
		Logger.debug("Driver JAR: " + driver.jarFile);

		DBDriver dbdriver = null;
		if (driverId != null && driverId != 0) {
			dbdriver = driverService.getDriverById(driverId);
			dbdriver = driverAdapter.toEntity(driver, dbdriver);
		} else {
			dbdriver = driverAdapter.toNewEntity(driver);
		}
		driverService.save(dbdriver);
		return redirect(routes.DriverController.index());
	}
}
