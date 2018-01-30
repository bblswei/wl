package org.springframework.cglib.transform.impl;

import org.springframework.cglib.core.ClassGenerator;
import org.springframework.cglib.core.DefaultGeneratorStrategy;
import org.springframework.cglib.transform.MethodFilter;
import org.springframework.cglib.transform.MethodFilterTransformer;
import org.springframework.cglib.transform.TransformingClassGenerator;
import org.springframework.cglib.transform.impl.UndeclaredThrowableTransformer;
import org.springframework.cglib.transform.impl.UndeclaredThrowableStrategy.1;

public class UndeclaredThrowableStrategy extends DefaultGeneratorStrategy {
	private Class wrapper;
	private static final MethodFilter TRANSFORM_FILTER = new 1();

	public UndeclaredThrowableStrategy(Class wrapper) {
		this.wrapper = wrapper;
	}

	protected ClassGenerator transform(ClassGenerator cg) throws Exception {
		UndeclaredThrowableTransformer tr = new UndeclaredThrowableTransformer(this.wrapper);
		MethodFilterTransformer tr1 = new MethodFilterTransformer(TRANSFORM_FILTER, tr);
		return new TransformingClassGenerator(cg, tr1);
	}
}