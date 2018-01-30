package org.springframework.cglib.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.springframework.asm.Type;
import org.springframework.cglib.core.Constants;
import org.springframework.cglib.core.Signature;
import org.springframework.cglib.reflect.FastConstructor;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.cglib.reflect.FastClass.Generator;

public abstract class FastClass {
	private Class type;

	protected FastClass() {
		throw new Error("Using the FastClass empty constructor--please report to the cglib-devel mailing list");
	}

	protected FastClass(Class type) {
		this.type = type;
	}

	public static FastClass create(Class type) {
		return create(type.getClassLoader(), type);
	}

	public static FastClass create(ClassLoader loader, Class type) {
		Generator gen = new Generator();
		gen.setType(type);
		gen.setClassLoader(loader);
		return gen.create();
	}

	public Object invoke(String name, Class[] parameterTypes, Object obj, Object[] args)
			throws InvocationTargetException {
		return this.invoke(this.getIndex(name, parameterTypes), obj, args);
	}

	public Object newInstance() throws InvocationTargetException {
		return this.newInstance(this.getIndex(Constants.EMPTY_CLASS_ARRAY), (Object[]) null);
	}

	public Object newInstance(Class[] parameterTypes, Object[] args) throws InvocationTargetException {
		return this.newInstance(this.getIndex(parameterTypes), args);
	}

	public FastMethod getMethod(Method method) {
		return new FastMethod(this, method);
	}

	public FastConstructor getConstructor(Constructor constructor) {
		return new FastConstructor(this, constructor);
	}

	public FastMethod getMethod(String name, Class[] parameterTypes) {
		try {
			return this.getMethod(this.type.getMethod(name, parameterTypes));
		} catch (NoSuchMethodException arg3) {
			throw new NoSuchMethodError(arg3.getMessage());
		}
	}

	public FastConstructor getConstructor(Class[] parameterTypes) {
		try {
			return this.getConstructor(this.type.getConstructor(parameterTypes));
		} catch (NoSuchMethodException arg2) {
			throw new NoSuchMethodError(arg2.getMessage());
		}
	}

	public String getName() {
		return this.type.getName();
	}

	public Class getJavaClass() {
		return this.type;
	}

	public String toString() {
		return this.type.toString();
	}

	public int hashCode() {
		return this.type.hashCode();
	}

	public boolean equals(Object o) {
		return o != null && o instanceof FastClass ? this.type.equals(((FastClass) o).type) : false;
	}

	public abstract int getIndex(String arg0, Class[] arg1);

	public abstract int getIndex(Class[] arg0);

	public abstract Object invoke(int arg0, Object arg1, Object[] arg2) throws InvocationTargetException;

	public abstract Object newInstance(int arg0, Object[] arg1) throws InvocationTargetException;

	public abstract int getIndex(Signature arg0);

	public abstract int getMaxIndex();

	protected static String getSignatureWithoutReturnType(String name, Class[] parameterTypes) {
		StringBuffer sb = new StringBuffer();
		sb.append(name);
		sb.append('(');

		for (int i = 0; i < parameterTypes.length; ++i) {
			sb.append(Type.getDescriptor(parameterTypes[i]));
		}

		sb.append(')');
		return sb.toString();
	}
}