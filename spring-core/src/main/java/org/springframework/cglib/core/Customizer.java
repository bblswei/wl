package org.springframework.cglib.core;

import org.springframework.asm.Type;
import org.springframework.cglib.core.CodeEmitter;
import org.springframework.cglib.core.KeyFactoryCustomizer;

public interface Customizer extends KeyFactoryCustomizer {
	void customize(CodeEmitter arg0, Type arg1);
}