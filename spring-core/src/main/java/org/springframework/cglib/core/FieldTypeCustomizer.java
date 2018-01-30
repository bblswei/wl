package org.springframework.cglib.core;

import org.springframework.asm.Type;
import org.springframework.cglib.core.CodeEmitter;
import org.springframework.cglib.core.KeyFactoryCustomizer;

public interface FieldTypeCustomizer extends KeyFactoryCustomizer {
	void customize(CodeEmitter arg0, int arg1, Type arg2);

	Type getOutType(int arg0, Type arg1);
}