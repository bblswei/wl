package org.springframework.cglib.transform.impl;

public interface FieldProvider {
	String[] getFieldNames();

	Class[] getFieldTypes();

	void setField(int arg0, Object arg1);

	Object getField(int arg0);

	void setField(String arg0, Object arg1);

	Object getField(String arg0);
}