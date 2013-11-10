package controllers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.quartz.CronExpression;

import models.DBConnection;
import models.QueryJob;
import play.api.templates.Html;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import services.ConnectionService;
import services.QueryJobService;
import views.html.queryjob.edit;
import views.html.queryjob.index;
import forms.QueryJobFormAdapter;

public class QueryJobController extends Controller {

	final QueryJobService queryJobService;
	final ConnectionService connService;
	final QueryJobFormAdapter formAdapter;

	@Inject
	public QueryJobController(final QueryJobService queryJobService,
			final ConnectionService connService,
			final QueryJobFormAdapter formAdapter) {
		this.queryJobService = queryJobService;
		this.connService = connService;
		this.formAdapter = formAdapter;
	}

	public Result index() {
		List<QueryJob> jobs = queryJobService.findAll();
		return ok(index.render(jobs));
	}

	public Result load(Long jobId) {
		Form<forms.QueryJob> form = Form.form(forms.QueryJob.class);
		if (jobId != null && jobId != 0) {
			QueryJob dbconn = queryJobService.getQueryJobById(jobId);
			form = form.fill(formAdapter.toForm(dbconn));
		}
		return ok(edit.render(jobId, form, getConnectionOptions()));
	}

	public Result save(Long jobId) throws Exception {
		Form<forms.QueryJob> form = Form.form(forms.QueryJob.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest(edit.render(jobId, form, getConnectionOptions()));
		}
		forms.QueryJob jobform = form.get();

		try {
			new CronExpression(jobform.cron);
		} catch (Exception e) {
			form.reject("cron", e.getMessage());
			return badRequest(edit.render(jobId, form, getConnectionOptions()));
		}

		QueryJob jobEntity = null;
		if (jobId != null && jobId != 0) {
			jobEntity = queryJobService.getQueryJobById(jobId);
			jobEntity = formAdapter.toEntity(jobform, jobEntity);
		} else {
			jobEntity = formAdapter.toNewEntity(jobform);
		}
		queryJobService.save(jobEntity);
		return redirect(routes.QueryJobController.index());
	}

	public Result test(Long jobId) {
		try {
			String html = queryJobService.test(jobId);
			return ok(Html.apply(html));
		} catch (Exception e) {
			return ok("Failed: " + e.getMessage());
		}
	}

	public Result invokeNow(Long jobId) {
		queryJobService.invokeNow(jobId);
		return redirect(routes.QueryJobController.index());
	}

	protected Map<String, String> getConnectionOptions() {
		Map<String, String> options = new LinkedHashMap<String, String>();
		List<DBConnection> conns = connService.findAll();
		for (DBConnection d : conns) {
			options.put(String.valueOf(d.getId()), d.getName());
		}
		return options;
	}

}
