import play.Application;
import play.GlobalSettings;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Global extends GlobalSettings {

	Injector injector;

	@Override
	public void onStart(Application application) {
		super.onStart(application);
		injector = Guice.createInjector();
	}

	@Override
	public <A> A getControllerInstance(Class<A> clazz) throws Exception {
		return injector.getInstance(clazz);
	}

}
