package org.springframework.cglib.transform;

public interface MethodFilter {
	boolean accept(int arg0, String arg1, String arg2, String arg3, String[] arg4);
}