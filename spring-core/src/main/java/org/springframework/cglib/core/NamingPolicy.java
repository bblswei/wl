package org.springframework.cglib.core;

import org.springframework.cglib.core.Predicate;

public interface NamingPolicy {
	String getClassName(String arg0, String arg1, Object arg2, Predicate arg3);

	boolean equals(Object arg0);
}