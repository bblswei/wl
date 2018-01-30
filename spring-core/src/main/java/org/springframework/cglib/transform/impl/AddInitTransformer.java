package org.springframework.cglib.transform.impl;

import java.lang.reflect.Method;
import org.springframework.asm.Type;
import org.springframework.cglib.core.CodeEmitter;
import org.springframework.cglib.core.Constants;
import org.springframework.cglib.core.MethodInfo;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.cglib.core.Signature;
import org.springframework.cglib.transform.ClassEmitterTransformer;
import org.springframework.cglib.transform.impl.AddInitTransformer.1;

public class AddInitTransformer extends ClassEmitterTransformer {
	private MethodInfo info;

	public AddInitTransformer(Method method) {
		this.info = ReflectUtils.getMethodInfo(method);
		Type[] types = this.info.getSignature().getArgumentTypes();
		if (types.length != 1 || !types[0].equals(Constants.TYPE_OBJECT)
				|| !this.info.getSignature().getReturnType().equals(Type.VOID_TYPE)) {
			throw new IllegalArgumentException(method + " illegal signature");
		}
	}

	public CodeEmitter begin_method(int access, Signature sig, Type[] exceptions) {
      CodeEmitter emitter = super.begin_method(access, sig, exceptions);
      return (CodeEmitter)(sig.getName().equals("<init>")?new 1(this, emitter):emitter);
   }
}