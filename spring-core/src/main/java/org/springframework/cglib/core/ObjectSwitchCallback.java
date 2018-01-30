package org.springframework.cglib.core;

import org.springframework.asm.Label;

public interface ObjectSwitchCallback {
	void processCase(Object arg0, Label arg1) throws Exception;

	void processDefault() throws Exception;
}