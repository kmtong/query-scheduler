package controllers;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import models.QueryJob;
import play.mvc.Controller;
import play.mvc.Result;
import services.QueryJobService;
import services.routes.QueryJobRouteBuilder;
import views.html.dashboard.index;
import dashboard.ProcessTracker;

public class Application extends Controller {

	final ProcessTracker tracker;
	final QueryJobService jobService;

	@Inject
	public Application(ProcessTracker tracker, QueryJobService jobService) {
		this.tracker = tracker;
		this.jobService = jobService;
	}

	public Result index() {
		Map<String, QueryJob> runningJobs = new HashMap<String, QueryJob>();
		for (String routeId : tracker.getRunning().keySet()) {
			long jobId = QueryJobRouteBuilder.parseQueryJobID(routeId);
			runningJobs.put(routeId, jobService.getQueryJobById(jobId));
		}
		return ok(index.render(tracker.getRunning(), runningJobs));
	}

}
