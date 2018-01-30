package org.springframework.cglib.core;

import org.springframework.cglib.core.ClassGenerator;

public interface GeneratorStrategy {
	byte[] generate(ClassGenerator arg0) throws Exception;

	boolean equals(Object arg0);
}