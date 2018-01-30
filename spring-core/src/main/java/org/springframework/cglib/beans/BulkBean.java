package org.springframework.cglib.beans;

import org.springframework.cglib.beans.BulkBean.BulkBeanKey;
import org.springframework.cglib.beans.BulkBean.Generator;
import org.springframework.cglib.core.KeyFactory;

public abstract class BulkBean {
	private static final BulkBeanKey KEY_FACTORY = (BulkBeanKey) KeyFactory.create(BulkBeanKey.class);
	protected Class target;
	protected String[] getters;
	protected String[] setters;
	protected Class[] types;

	public abstract void getPropertyValues(Object arg0, Object[] arg1);

	public abstract void setPropertyValues(Object arg0, Object[] arg1);

	public Object[] getPropertyValues(Object bean) {
		Object[] values = new Object[this.getters.length];
		this.getPropertyValues(bean, values);
		return values;
	}

	public Class[] getPropertyTypes() {
		return (Class[]) ((Class[]) this.types.clone());
	}

	public String[] getGetters() {
		return (String[]) ((String[]) this.getters.clone());
	}

	public String[] getSetters() {
		return (String[]) ((String[]) this.setters.clone());
	}

	public static BulkBean create(Class target, String[] getters, String[] setters, Class[] types) {
		Generator gen = new Generator();
		gen.setTarget(target);
		gen.setGetters(getters);
		gen.setSetters(setters);
		gen.setTypes(types);
		return gen.create();
	}
}