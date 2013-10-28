package models;

import play.libs.Akka;
import services.SchedulerUpdater;
import akka.actor.ActorRef;
import akka.actor.Props;

import com.avaje.ebean.event.BeanPersistAdapter;
import com.avaje.ebean.event.BeanPersistRequest;

import data.QueryJobChange;

public class QueryJobPersistHandler extends BeanPersistAdapter {

	// setup actor
	ActorRef updater = Akka.system().actorOf(
			Props.create(SchedulerUpdater.class), "SchedulerUpdater");

	@Override
	public boolean isRegisterFor(Class<?> arg0) {
		return (QueryJob.class == arg0);
	}

	@Override
	public void postDelete(BeanPersistRequest<?> request) {
		updater.tell(new QueryJobChange((QueryJob) request.getBean(),
				QueryJobChange.Change.DELETE), null);
	}

	@Override
	public void postInsert(BeanPersistRequest<?> request) {
		updater.tell(new QueryJobChange((QueryJob) request.getBean(),
				QueryJobChange.Change.INSERT), null);
	}

	@Override
	public void postUpdate(BeanPersistRequest<?> request) {
		updater.tell(new QueryJobChange((QueryJob) request.getBean(),
				QueryJobChange.Change.UPDATE), null);
	}

}
