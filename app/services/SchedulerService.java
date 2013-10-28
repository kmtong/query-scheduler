package services;

import java.net.URLEncoder;

import javax.inject.Inject;

import models.QueryJob;

import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.builder.RouteBuilder;

import play.Logger;

public class SchedulerService {

	final CamelContext camel;
	final QueryJobService jobService;

	@Inject
	public SchedulerService(CamelContext camel, QueryJobService jobService) {
		this.camel = camel;
		this.jobService = jobService;
	}

	public void restoreStates() throws Exception {
		clearStates();
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
		String id = getJobKey(job);
		Logger.info("Clear Job: " + id);
		camel.stopRoute(id);
		camel.removeRoute(id);
	}

	public void setupJob(QueryJob job) throws Exception {
		camel.addRoutes(createRouteBuilder(job));
	}

	/**
	 * Create a RouteBuilder for a Query Job and utilize Camel-Quartz components
	 * for scheduling
	 */
	protected RouteBuilder createRouteBuilder(final QueryJob job) {
		RouteBuilder builder = new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				String id = getJobKey(job);
				String fromUri = "quartz://QueryJob/" + job.getId() + "?cron="
						+ URLEncoder.encode(job.getCron(), "UTF-8");

				// part 1: scheduling
				from(fromUri).id(id)
				// part 2: processing
						.to("log:" + id);
			}
		};
		return builder;
	}

	protected String getJobKey(QueryJob job) {
		return "QueryJob-" + job.getId();
	}

}
