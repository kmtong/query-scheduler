package controllers;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import models.DBConnection;
import models.DBDriver;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import services.ConnectionService;
import services.DriverService;
import views.html.connection.edit;
import views.html.connection.index;
import forms.DBConnectionFormAdapter;

public class ConnectionController extends Controller {

	final ConnectionService connService;
	final DriverService driverService;
	final DBConnectionFormAdapter connAdapter;

	@Inject
	public ConnectionController(final ConnectionService connService,
			final DriverService driverService,
			final DBConnectionFormAdapter connAdapter) {
		this.connService = connService;
		this.driverService = driverService;
		this.connAdapter = connAdapter;
	}

	public Result index() {
		List<DBConnection> conns = connService.findAll();
		return ok(index.render(conns));
	}

	public Result load(Long connId) {
		Form<forms.DBConnection> form = Form.form(forms.DBConnection.class);
		if (connId != null && connId != 0) {
			DBConnection dbconn = connService.getConnectionById(connId);
			form = form.fill(connAdapter.toForm(dbconn));
		}
		return ok(edit.render(connId, form, getDriverOptions()));
	}

	public Result save(Long connId) throws Exception {
		Form<forms.DBConnection> connForm = Form.form(forms.DBConnection.class)
				.bindFromRequest();
		forms.DBConnection conn = connForm.get();
		DBConnection dbconn = null;
		if (connId != null && connId != 0) {
			dbconn = connService.getConnectionById(connId);
			dbconn = connAdapter.toEntity(conn, dbconn);
		} else {
			dbconn = connAdapter.toNewEntity(conn);
		}
		connService.save(dbconn);
		return redirect(routes.ConnectionController.index());
	}

	public Result test(Long connId) {
		try {
			DBConnection dbconn = connService.getConnectionById(connId);
			Connection conn = connService.getConnection(dbconn);
			if (conn != null) {
				return ok("OK");
			}
			return ok("Failed");
		} catch (Exception e) {
			return ok("Failed: " + e.getMessage());
		}
	}

	protected Map<String, String> getDriverOptions() {
		Map<String, String> options = new LinkedHashMap<String, String>();
		List<DBDriver> drivers = driverService.findAll();
		for (DBDriver d : drivers) {
			options.put(String.valueOf(d.getId()), d.getName());
		}
		return options;
	}

}
