package services;

import javax.inject.Inject;

import models.QueryJob;

import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.builder.RouteBuilder;

public class SchedulerService {

	CamelContext camel;

	@Inject
	public SchedulerService(CamelContext camel) {
		this.camel = camel;
	}

	public void restoreStates() throws Exception {
		clearStates();
		// XXX query all active jobs and build camel routes
		camel.addRoutes(createRouteBuilder(null));
		camel.start();
	}

	public void clearStates() throws Exception {
		camel.stop();
		for (Route r : camel.getRoutes()) {
			camel.removeRoute(r.getId());
		}
	}

	/**
	 * Create a RouteBuilder for a Query Job and utilize Camel-Quartz components
	 * for scheduling
	 */
	protected RouteBuilder createRouteBuilder(final QueryJob job) {
		RouteBuilder builder = new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				String id = "QueryJob-1234";

				// part 1: scheduling
				// XXX test every 10 sec for now
				from("quartz://QueryJob/1234?cron=*/10+*+*+*+*+?").id(id)
				// part 2: processing
						.to("log:" + id);
			}
		};
		return builder;
	}
}
