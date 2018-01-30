package org.springframework.cglib.transform.impl;

public interface InterceptFieldCallback {
	int writeInt(Object arg0, String arg1, int arg2, int arg3);

	char writeChar(Object arg0, String arg1, char arg2, char arg3);

	byte writeByte(Object arg0, String arg1, byte arg2, byte arg3);

	boolean writeBoolean(Object arg0, String arg1, boolean arg2, boolean arg3);

	short writeShort(Object arg0, String arg1, short arg2, short arg3);

	float writeFloat(Object arg0, String arg1, float arg2, float arg3);

	double writeDouble(Object arg0, String arg1, double arg2, double arg4);

	long writeLong(Object arg0, String arg1, long arg2, long arg4);

	Object writeObject(Object arg0, String arg1, Object arg2, Object arg3);

	int readInt(Object arg0, String arg1, int arg2);

	char readChar(Object arg0, String arg1, char arg2);

	byte readByte(Object arg0, String arg1, byte arg2);

	boolean readBoolean(Object arg0, String arg1, boolean arg2);

	short readShort(Object arg0, String arg1, short arg2);

	float readFloat(Object arg0, String arg1, float arg2);

	double readDouble(Object arg0, String arg1, double arg2);

	long readLong(Object arg0, String arg1, long arg2);

	Object readObject(Object arg0, String arg1, Object arg2);
}