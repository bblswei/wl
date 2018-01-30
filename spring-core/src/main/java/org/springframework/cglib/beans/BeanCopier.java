package org.springframework.cglib.beans;

import org.springframework.asm.Type;
import org.springframework.cglib.beans.BeanCopier.BeanCopierKey;
import org.springframework.cglib.beans.BeanCopier.Generator;
import org.springframework.cglib.core.Constants;
import org.springframework.cglib.core.Converter;
import org.springframework.cglib.core.KeyFactory;
import org.springframework.cglib.core.Signature;
import org.springframework.cglib.core.TypeUtils;

public abstract class BeanCopier {
	private static final BeanCopierKey KEY_FACTORY = (BeanCopierKey) KeyFactory.create(BeanCopierKey.class);
	private static final Type CONVERTER = TypeUtils.parseType("org.springframework.cglib.core.Converter");
	private static final Type BEAN_COPIER = TypeUtils.parseType("org.springframework.cglib.beans.BeanCopier");
	private static final Signature COPY;
	private static final Signature CONVERT;

	public static BeanCopier create(Class source, Class target, boolean useConverter) {
		Generator gen = new Generator();
		gen.setSource(source);
		gen.setTarget(target);
		gen.setUseConverter(useConverter);
		return gen.create();
	}

	public abstract void copy(Object arg0, Object arg1, Converter arg2);

	static {
		COPY = new Signature("copy", Type.VOID_TYPE,
				new Type[]{Constants.TYPE_OBJECT, Constants.TYPE_OBJECT, CONVERTER});
		CONVERT = TypeUtils.parseSignature("Object convert(Object, Class, Object)");
	}
}