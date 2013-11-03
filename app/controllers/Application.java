package controllers;

import javax.inject.Inject;

import play.mvc.Controller;
import play.mvc.Result;
import dashboard.ProcessTracker;
import views.html.dashboard.*;

public class Application extends Controller {

	final ProcessTracker tracker;

	@Inject
	public Application(ProcessTracker tracker) {
		this.tracker = tracker;
	}

	public Result index() {
		return ok(index.render(tracker.getRunning()));
	}

}
