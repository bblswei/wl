package org.springframework.cglib.proxy;

import java.lang.reflect.Method;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.MethodProxy;

public interface MethodInterceptor extends Callback {
	Object intercept(Object arg0, Method arg1, Object[] arg2, MethodProxy arg3) throws Throwable;
}