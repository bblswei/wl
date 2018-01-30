package org.springframework.cglib.proxy;

import java.lang.reflect.Method;
import org.springframework.cglib.proxy.Callback;

public interface InvocationHandler extends Callback {
	Object invoke(Object arg0, Method arg1, Object[] arg2) throws Throwable;
}