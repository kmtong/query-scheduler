package services;

import inject.InjectorFactory;
import play.Logger;
import akka.actor.UntypedActor;

import com.google.inject.Inject;

import data.QueryJobChange;
import data.QueryJobChange.Change;

public class SchedulerUpdater extends UntypedActor {

	@Inject
	SchedulerService schedulerService;
	@Inject
	QueryJobService jobService;

	public SchedulerUpdater() {
		InjectorFactory.getInstance().injectMembers(this);
	}

	@Override
	public void onReceive(Object arg0) throws Exception {
		QueryJobChange change = (QueryJobChange) arg0;
		Logger.info("Received Message: " + arg0);
		if (change.getChange() != Change.INSERT) {
			schedulerService.clearJob(change.getJob());
		}
		if (change.getChange() != Change.DELETE) {
			schedulerService.setupJob(change.getJob());
		}
	}

}
