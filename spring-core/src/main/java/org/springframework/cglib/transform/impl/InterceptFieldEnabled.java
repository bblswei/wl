package org.springframework.cglib.transform.impl;

import org.springframework.cglib.transform.impl.InterceptFieldCallback;

public interface InterceptFieldEnabled {
	void setInterceptFieldCallback(InterceptFieldCallback arg0);

	InterceptFieldCallback getInterceptFieldCallback();
}