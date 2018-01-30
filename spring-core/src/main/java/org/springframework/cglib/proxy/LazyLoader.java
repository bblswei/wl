package org.springframework.cglib.proxy;

import org.springframework.cglib.proxy.Callback;

public interface LazyLoader extends Callback {
	Object loadObject() throws Exception;
}