package services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.commons.io.IOUtils;

public class JarByteClassLoader extends URLClassLoader {

	File cachedFile;

	public JarByteClassLoader(URL[] urls, ClassLoader parent,
			byte[] jarFileContent) throws IOException {
		super(urls, parent);
		cachedFile = File.createTempFile("bytearrayjar", ".jar");
		IOUtils.copy(new ByteArrayInputStream(jarFileContent),
				new FileOutputStream(cachedFile));
		this.addURL(cachedFile.toURI().toURL());
	}

	public void unload() {
		if (cachedFile != null) {
			cachedFile.delete();
		}
		cachedFile = null;
	}

	@Override
	protected void finalize() throws Throwable {
		unload();
		super.finalize();
	}

}