package org.springframework.cglib.core;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.springframework.cglib.core.KeyFactory;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.cglib.core.MethodWrapper.MethodWrapperKey;

public class MethodWrapper {
	private static final MethodWrapperKey KEY_FACTORY = (MethodWrapperKey) KeyFactory.create(MethodWrapperKey.class);

	public static Object create(Method method) {
		return KEY_FACTORY.newInstance(method.getName(), ReflectUtils.getNames(method.getParameterTypes()),
				method.getReturnType().getName());
	}

	public static Set createSet(Collection methods) {
		HashSet set = new HashSet();
		Iterator it = methods.iterator();

		while (it.hasNext()) {
			set.add(create((Method) it.next()));
		}

		return set;
	}
}