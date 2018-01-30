package org.springframework.cglib.proxy;

import java.lang.reflect.Method;

public interface CallbackFilter {
	int accept(Method arg0);

	boolean equals(Object arg0);
}