import inject.InjectorFactory;
import play.Application;
import play.GlobalSettings;
import services.SchedulerService;

import com.google.inject.Injector;

public class Global extends GlobalSettings {

	public static Injector injector;

	@Override
	public void onStart(Application application) {
		super.onStart(application);
		injector = InjectorFactory.getInstance();
		// restore previous timer states
		try {
			injector.getInstance(SchedulerService.class).restoreStates();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onStop(Application application) {
		super.onStop(application);
		// reset previous timer states
		try {
			injector.getInstance(SchedulerService.class).clearStates();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <A> A getControllerInstance(Class<A> clazz) throws Exception {
		return injector.getInstance(clazz);
	}

}
