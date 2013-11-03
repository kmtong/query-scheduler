package dashboard;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;

import play.Logger;

public class ProcessTracker {

	Map<String, Exchange> running;

	public ProcessTracker() {
		this.running = new HashMap<String, Exchange>();
	}

	public void begin(Exchange xchg) {
		Logger.info("Begin: " + xchg.getFromRouteId());
		running.put(xchg.getFromRouteId(), xchg);
	}

	public void end(Exchange xchg) {
		Logger.info("End: " + xchg.getFromRouteId() + ", Failed: "
				+ xchg.isFailed());
		running.remove(xchg.getFromRouteId());
	}

	public Map<String, Exchange> getRunning() {
		return running;
	}
}
