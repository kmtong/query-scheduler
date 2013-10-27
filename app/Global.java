import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

import play.Application;
import play.GlobalSettings;
import services.SchedulerService;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Global extends GlobalSettings {

	Injector injector;

	@Override
	public void onStart(Application application) {
		super.onStart(application);
		injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(CamelContext.class).to(DefaultCamelContext.class)
						.asEagerSingleton();
			}
		});

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
		// restore previous timer states
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
