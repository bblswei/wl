package org.springframework.cglib.proxy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.cglib.core.ClassesKey;
import org.springframework.cglib.core.KeyFactory;
import org.springframework.cglib.proxy.Mixin.Generator;
import org.springframework.cglib.proxy.Mixin.MixinKey;
import org.springframework.cglib.proxy.Mixin.Route;

public abstract class Mixin {
	private static final MixinKey KEY_FACTORY;
	private static final Map ROUTE_CACHE;
	public static final int STYLE_INTERFACES = 0;
	public static final int STYLE_BEANS = 1;
	public static final int STYLE_EVERYTHING = 2;

	public abstract Mixin newInstance(Object[] arg0);

	public static Mixin create(Object[] delegates) {
		Generator gen = new Generator();
		gen.setDelegates(delegates);
		return gen.create();
	}

	public static Mixin create(Class[] interfaces, Object[] delegates) {
		Generator gen = new Generator();
		gen.setClasses(interfaces);
		gen.setDelegates(delegates);
		return gen.create();
	}

	public static Mixin createBean(Object[] beans) {
		return createBean((ClassLoader) null, beans);
	}

	public static Mixin createBean(ClassLoader loader, Object[] beans) {
		Generator gen = new Generator();
		gen.setStyle(1);
		gen.setDelegates(beans);
		gen.setClassLoader(loader);
		return gen.create();
	}

	public static Class[] getClasses(Object[] delegates) {
		return (Class[]) ((Class[]) Route.access$100(route(delegates)).clone());
	}

	private static Route route(Object[] delegates) {
		Object key = ClassesKey.create(delegates);
		Route route = (Route) ROUTE_CACHE.get(key);
		if (route == null) {
			ROUTE_CACHE.put(key, route = new Route(delegates));
		}

		return route;
	}

	static {
		KEY_FACTORY = (MixinKey) KeyFactory.create(MixinKey.class, KeyFactory.CLASS_BY_NAME);
		ROUTE_CACHE = Collections.synchronizedMap(new HashMap());
	}
}