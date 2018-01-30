package org.springframework.cglib.reflect;

import org.springframework.cglib.core.KeyFactory;
import org.springframework.cglib.reflect.ConstructorDelegate.ConstructorKey;
import org.springframework.cglib.reflect.ConstructorDelegate.Generator;

public abstract class ConstructorDelegate {
	private static final ConstructorKey KEY_FACTORY;

	public static ConstructorDelegate create(Class targetClass, Class iface) {
		Generator gen = new Generator();
		gen.setTargetClass(targetClass);
		gen.setInterface(iface);
		return gen.create();
	}

	static {
		KEY_FACTORY = (ConstructorKey) KeyFactory.create(ConstructorKey.class, KeyFactory.CLASS_BY_NAME);
	}
}