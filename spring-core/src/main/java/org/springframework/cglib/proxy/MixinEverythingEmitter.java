package org.springframework.cglib.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import org.springframework.asm.ClassVisitor;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.cglib.core.RejectModifierPredicate;
import org.springframework.cglib.proxy.MixinEmitter;

class MixinEverythingEmitter extends MixinEmitter {
	public MixinEverythingEmitter(ClassVisitor v, String className, Class[] classes) {
		super(v, className, classes, (int[]) null);
	}

	protected Class[] getInterfaces(Class[] classes) {
		ArrayList list = new ArrayList();

		for (int i = 0; i < classes.length; ++i) {
			ReflectUtils.addAllInterfaces(classes[i], list);
		}

		return (Class[]) ((Class[]) list.toArray(new Class[list.size()]));
	}

	protected Method[] getMethods(Class type) {
		ArrayList methods = new ArrayList(Arrays.asList(type.getMethods()));
		CollectionUtils.filter(methods, new RejectModifierPredicate(24));
		return (Method[]) ((Method[]) methods.toArray(new Method[methods.size()]));
	}
}