package org.springframework.cglib.transform.impl;

import org.springframework.asm.Type;

public interface InterceptFieldFilter {
	boolean acceptRead(Type arg0, String arg1);

	boolean acceptWrite(Type arg0, String arg1);
}