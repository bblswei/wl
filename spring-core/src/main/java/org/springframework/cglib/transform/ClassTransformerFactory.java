package org.springframework.cglib.transform;

import org.springframework.cglib.transform.ClassTransformer;

public interface ClassTransformerFactory {
	ClassTransformer newInstance();
}