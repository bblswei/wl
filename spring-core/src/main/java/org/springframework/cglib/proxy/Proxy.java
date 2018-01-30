package org.springframework.cglib.proxy;

import java.io.Serializable;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.CallbackFilter;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.NoOp;
import org.springframework.cglib.proxy.Proxy.1;
import org.springframework.cglib.proxy.Proxy.ProxyImpl;

public class Proxy implements Serializable {
	protected InvocationHandler h;
	private static final CallbackFilter BAD_OBJECT_METHOD_FILTER = new 1();

	protected Proxy(InvocationHandler h) {
		Enhancer.registerCallbacks(this.getClass(), new Callback[]{h, null});
		this.h = h;
	}

	public static InvocationHandler getInvocationHandler(Object proxy) {
		if (!(proxy instanceof ProxyImpl)) {
			throw new IllegalArgumentException("Object is not a proxy");
		} else {
			return ((Proxy) proxy).h;
		}
	}

	public static Class getProxyClass(ClassLoader loader, Class[] interfaces) {
		Enhancer e = new Enhancer();
		e.setSuperclass(ProxyImpl.class);
		e.setInterfaces(interfaces);
		e.setCallbackTypes(new Class[]{InvocationHandler.class, NoOp.class});
		e.setCallbackFilter(BAD_OBJECT_METHOD_FILTER);
		e.setUseFactory(false);
		return e.createClass();
	}

	public static boolean isProxyClass(Class cl) {
		return cl.getSuperclass().equals(ProxyImpl.class);
	}

	public static Object newProxyInstance(ClassLoader loader, Class[] interfaces, InvocationHandler h) {
		try {
			Class e = getProxyClass(loader, interfaces);
			return e.getConstructor(new Class[]{InvocationHandler.class}).newInstance(new Object[]{h});
		} catch (RuntimeException arg3) {
			throw arg3;
		} catch (Exception arg4) {
			throw new CodeGenerationException(arg4);
		}
	}
}