package org.springframework.cglib.proxy;

import org.springframework.cglib.proxy.Callback;

public interface FixedValue extends Callback {
	Object loadObject() throws Exception;
}