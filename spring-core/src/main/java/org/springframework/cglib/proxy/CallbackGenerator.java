package org.springframework.cglib.proxy;

import java.util.List;
import org.springframework.cglib.core.ClassEmitter;
import org.springframework.cglib.core.CodeEmitter;
import org.springframework.cglib.proxy.CallbackGenerator.Context;

interface CallbackGenerator {
	void generate(ClassEmitter arg0, Context arg1, List arg2) throws Exception;

	void generateStatic(CodeEmitter arg0, Context arg1, List arg2) throws Exception;
}