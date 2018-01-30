package org.springframework.cglib.reflect;

import org.springframework.cglib.core.KeyFactory;
import org.springframework.cglib.reflect.MethodDelegate.Generator;
import org.springframework.cglib.reflect.MethodDelegate.MethodDelegateKey;

public abstract class MethodDelegate {
	private static final MethodDelegateKey KEY_FACTORY;
	protected Object target;
	protected String eqMethod;

	public static MethodDelegate createStatic(Class targetClass, String methodName, Class iface) {
		Generator gen = new Generator();
		gen.setTargetClass(targetClass);
		gen.setMethodName(methodName);
		gen.setInterface(iface);
		return gen.create();
	}

	public static MethodDelegate create(Object target, String methodName, Class iface) {
		Generator gen = new Generator();
		gen.setTarget(target);
		gen.setMethodName(methodName);
		gen.setInterface(iface);
		return gen.create();
	}

	public boolean equals(Object obj) {
		MethodDelegate other = (MethodDelegate) obj;
		return other != null && this.target == other.target && this.eqMethod.equals(other.eqMethod);
	}

	public int hashCode() {
		return this.target.hashCode() ^ this.eqMethod.hashCode();
	}

	public Object getTarget() {
		return this.target;
	}

	public abstract MethodDelegate newInstance(Object arg0);

	static {
		KEY_FACTORY = (MethodDelegateKey) KeyFactory.create(MethodDelegateKey.class, KeyFactory.CLASS_BY_NAME);
	}
}