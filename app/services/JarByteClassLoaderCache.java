package services;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class JarByteClassLoaderCache {

	Map<String, JarByteClassLoader> cache = new HashMap<String, JarByteClassLoader>();

	public synchronized ClassLoader getOrElse(String key,
			Callable<JarByteClassLoader> callable) throws Exception {
		if (cache.containsKey(key)) {
			return cache.get(key);
		} else {
			JarByteClassLoader loader = callable.call();
			cache.put(key, loader);
			return loader;
		}
	}

	public synchronized void expire(String key) {
		JarByteClassLoader loader = cache.get(key);
		if (loader != null) {
			loader.unload();
			cache.remove(key);
		}
	}
}
