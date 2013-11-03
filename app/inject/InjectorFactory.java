package inject;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import dashboard.ProcessTracker;

public class InjectorFactory {

	private static Injector INSTANCE;

	public static Injector getInstance() {
		if (INSTANCE == null) {
			INSTANCE = createInjector();
		}
		return INSTANCE;
	}

	private static Injector createInjector() {
		return Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(CamelContext.class).to(DefaultCamelContext.class)
						.asEagerSingleton();
				bind(ProcessTracker.class).asEagerSingleton();
			}
		});
	}
}
