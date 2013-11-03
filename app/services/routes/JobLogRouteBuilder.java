package services.routes;

import org.apache.camel.builder.RouteBuilder;

public class JobLogRouteBuilder extends RouteBuilder {

	public JobLogRouteBuilder() {
	}

	public static String getJobLogEndpoint() {
		return "direct:job-log";
	}

	public static String getJobLogRouteID() {
		return "JobLog";
	}

	@Override
	public void configure() throws Exception {
		// XXX log to database for execution result
		from(getJobLogEndpoint()).id(getJobLogRouteID()).to("log:history");
	}

}
