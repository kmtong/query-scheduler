package services.routes;

import org.apache.camel.builder.RouteBuilder;

import dashboard.ProcessTracker;

public class TrackerRouteBuilder extends RouteBuilder {

	final ProcessTracker tracker;

	public TrackerRouteBuilder(ProcessTracker tracker) {
		this.tracker = tracker;
	}

	public static String getTrackerBeginEndpoint() {
		return "direct:tracker-begin";
	}

	public static String getTrackerEndEndpoint() {
		return "direct:tracker-end";
	}

	public static String getTrackerBeginRouteID() {
		return "TrackerBegin";
	}

	public static String getTrackerEndRouteID() {
		return "TrackerEnd";
	}

	@Override
	public void configure() throws Exception {
		from(getTrackerBeginEndpoint()).id(getTrackerBeginRouteID()).bean(
				tracker, "begin");

		from(getTrackerEndEndpoint()).id(getTrackerEndRouteID()).bean(tracker,
				"end");
	}

}
