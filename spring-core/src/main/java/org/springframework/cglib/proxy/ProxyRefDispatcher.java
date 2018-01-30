package org.springframework.cglib.proxy;

import org.springframework.cglib.proxy.Callback;

public interface ProxyRefDispatcher extends Callback {
	Object loadObject(Object arg0) throws Exception;
}