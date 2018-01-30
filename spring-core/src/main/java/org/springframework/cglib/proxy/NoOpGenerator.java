package org.springframework.cglib.proxy;

import java.util.Iterator;
import java.util.List;
import org.springframework.cglib.core.ClassEmitter;
import org.springframework.cglib.core.CodeEmitter;
import org.springframework.cglib.core.EmitUtils;
import org.springframework.cglib.core.MethodInfo;
import org.springframework.cglib.core.TypeUtils;
import org.springframework.cglib.proxy.CallbackGenerator;
import org.springframework.cglib.proxy.CallbackGenerator.Context;

class NoOpGenerator implements CallbackGenerator {
	public static final NoOpGenerator INSTANCE = new NoOpGenerator();

	public void generate(ClassEmitter ce, Context context, List methods) {
		Iterator it = methods.iterator();

		while (true) {
			MethodInfo method;
			do {
				if (!it.hasNext()) {
					return;
				}

				method = (MethodInfo) it.next();
			} while (!TypeUtils.isBridge(method.getModifiers())
					&& (!TypeUtils.isProtected(context.getOriginalModifiers(method))
							|| !TypeUtils.isPublic(method.getModifiers())));

			CodeEmitter e = EmitUtils.begin_method(ce, method);
			e.load_this();
			context.emitLoadArgsAndInvoke(e, method);
			e.return_value();
			e.end_method();
		}
	}

	public void generateStatic(CodeEmitter e, Context context, List methods) {
	}
}