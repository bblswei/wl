package org.springframework.cglib.proxy;

import org.springframework.cglib.proxy.Callback;

public interface Dispatcher extends Callback {
	Object loadObject() throws Exception;
}