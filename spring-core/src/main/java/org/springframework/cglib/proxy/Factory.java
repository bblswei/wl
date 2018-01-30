package org.springframework.cglib.proxy;

import org.springframework.cglib.proxy.Callback;

public interface Factory {
	Object newInstance(Callback arg0);

	Object newInstance(Callback[] arg0);

	Object newInstance(Class[] arg0, Object[] arg1, Callback[] arg2);

	Callback getCallback(int arg0);

	void setCallback(int arg0, Callback arg1);

	void setCallbacks(Callback[] arg0);

	Callback[] getCallbacks();
}