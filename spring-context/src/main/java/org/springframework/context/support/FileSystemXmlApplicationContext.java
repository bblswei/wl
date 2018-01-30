package org.springframework.context.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

public class FileSystemXmlApplicationContext extends AbstractXmlApplicationContext {

	public FileSystemXmlApplicationContext() {
	}

	public FileSystemXmlApplicationContext(ApplicationContext parent) {
		super(parent);
	}

	public FileSystemXmlApplicationContext(String configLocation) throws BeansException {
		this(new String[] {configLocation}, true, null);
	}

	public FileSystemXmlApplicationContext(String... configLocations) throws BeansException {
		this(configLocations, true, null);
	}

	public FileSystemXmlApplicationContext(String[] configLocations, ApplicationContext parent) throws BeansException {
		this(configLocations, true, parent);
	}

	public FileSystemXmlApplicationContext(String[] configLocations, boolean refresh) throws BeansException {
		this(configLocations, refresh, null);
	}

	public FileSystemXmlApplicationContext(String[] configLocations, boolean refresh, ApplicationContext parent)
			throws BeansException {

		super(parent);
		setConfigLocations(configLocations);
		if (refresh) {
			refresh();
		}
	}

	@Override
	protected Resource getResourceByPath(String path) {
		if (path != null && path.startsWith("/")) {
			path = path.substring(1);
		}
		return new FileSystemResource(path);
	}

}