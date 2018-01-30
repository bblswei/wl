package org.springframework.cglib.beans;

import org.springframework.asm.Type;
import org.springframework.cglib.beans.ImmutableBean.Generator;
import org.springframework.cglib.core.Signature;
import org.springframework.cglib.core.TypeUtils;

public class ImmutableBean {
	private static final Type ILLEGAL_STATE_EXCEPTION = TypeUtils.parseType("IllegalStateException");
	private static final Signature CSTRUCT_OBJECT = TypeUtils.parseConstructor("Object");
	private static final Class[] OBJECT_CLASSES = new Class[]{Object.class};
	private static final String FIELD_NAME = "CGLIB$RWBean";

	public static Object create(Object bean) {
		Generator gen = new Generator();
		gen.setBean(bean);
		return gen.create();
	}
}