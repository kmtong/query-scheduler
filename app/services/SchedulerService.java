package services;

import javax.inject.Inject;

import models.QueryJob;

import org.apache.camel.CamelContext;
import org.apache.camel.Route;

import play.Logger;

import services.routes.JobLogRouteBuilder;
import services.routes.QueryJobRouteBuilder;
import services.routes.TrackerRouteBuilder;
import dashboard.ProcessTracker;

public class SchedulerService {

	final CamelContext camel;
	final QueryJobService jobService;
	final PlayConfigurationProvider configProvider;
	final ProcessTracker tracker;

	@Inject
	public SchedulerService(CamelContext camel, QueryJobService jobService,
			ProcessTracker tracker, PlayConfigurationProvider configProvider)
			throws Exception {
		this.camel = camel;
		this.jobService = jobService;
		this.tracker = tracker;
		this.configProvider = configProvider;
	}

	protected void setupBasicRoutes() throws Exception {
		camel.addRoutes(new JobLogRouteBuilder());
		camel.addRoutes(new TrackerRouteBuilder(tracker));
	}

	public void restoreStates() throws Exception {
		clearStates();
		setupBasicRoutes();

		// query all active jobs and build camel routes
		for (QueryJob job : jobService.findAll()) {
			setupJob(job);
		}
		camel.start();
	}

	public void clearStates() throws Exception {
		for (Route r : camel.getRoutes()) {
			camel.stopRoute(r.getId());
			camel.removeRoute(r.getId());
		}
		camel.stop();
	}

	public void clearJob(QueryJob job) throws Exception {

		String triggerId = QueryJobRouteBuilder.getJobTriggerID(job);
		String processId = QueryJobRouteBuilder.getJobProcessID(job);

		if (camel.getInflightRepository().size(triggerId) > 0) {
			Logger.info("Inflight message going on for route: " + triggerId);
			camel.getInflightRepository().removeRoute(triggerId);
		}
		camel.stopRoute(triggerId);
		camel.removeRoute(triggerId);

		if (camel.getInflightRepository().size(processId) > 0) {
			Logger.info("Inflight message going on for route: " + processId);
			camel.getInflightRepository().removeRoute(processId);
		}
		camel.stopRoute(processId);
		camel.removeRoute(processId);
	}

	public void setupJob(QueryJob job) throws Exception {
		clearJob(job);
		QueryJobRouteBuilder builder = new QueryJobRouteBuilder(configProvider,
				job);
		camel.addRoutes(builder);
	}

}
