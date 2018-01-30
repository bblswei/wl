package org.springframework.cglib.transform.impl;

import java.lang.reflect.Constructor;
import org.springframework.asm.Type;
import org.springframework.cglib.core.CodeEmitter;
import org.springframework.cglib.core.Constants;
import org.springframework.cglib.core.Signature;
import org.springframework.cglib.core.TypeUtils;
import org.springframework.cglib.transform.ClassEmitterTransformer;
import org.springframework.cglib.transform.impl.UndeclaredThrowableTransformer.1;

public class UndeclaredThrowableTransformer extends ClassEmitterTransformer {
	private Type wrapper;

	public UndeclaredThrowableTransformer(Class wrapper) {
		this.wrapper = Type.getType(wrapper);
		boolean found = false;
		Constructor[] cstructs = wrapper.getConstructors();

		for (int i = 0; i < cstructs.length; ++i) {
			Class[] types = cstructs[i].getParameterTypes();
			if (types.length == 1 && types[0].equals(Throwable.class)) {
				found = true;
				break;
			}
		}

		if (!found) {
			throw new IllegalArgumentException(
					wrapper + " does not have a single-arg constructor that takes a Throwable");
		}
	}

	public CodeEmitter begin_method(int access, Signature sig, Type[] exceptions) {
      CodeEmitter e = super.begin_method(access, sig, exceptions);
      return (CodeEmitter)(!TypeUtils.isAbstract(access) && !sig.equals(Constants.SIG_STATIC)?new 1(this, e, exceptions):e);
   }
}