package org.springframework.cglib.core;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.springframework.asm.Type;
import org.springframework.cglib.core.Customizer;
import org.springframework.cglib.core.FieldTypeCustomizer;
import org.springframework.cglib.core.HashCodeCustomizer;
import org.springframework.cglib.core.KeyFactoryCustomizer;
import org.springframework.cglib.core.Signature;
import org.springframework.cglib.core.TypeUtils;
import org.springframework.cglib.core.KeyFactory.1;
import org.springframework.cglib.core.KeyFactory.2;
import org.springframework.cglib.core.KeyFactory.3;
import org.springframework.cglib.core.KeyFactory.4;
import org.springframework.cglib.core.KeyFactory.Generator;

public abstract class KeyFactory {
	private static final Signature GET_NAME = TypeUtils.parseSignature("String getName()");
	private static final Signature GET_CLASS = TypeUtils.parseSignature("Class getClass()");
	private static final Signature HASH_CODE = TypeUtils.parseSignature("int hashCode()");
	private static final Signature EQUALS = TypeUtils.parseSignature("boolean equals(Object)");
	private static final Signature TO_STRING = TypeUtils.parseSignature("String toString()");
	private static final Signature APPEND_STRING = TypeUtils.parseSignature("StringBuffer append(String)");
	private static final Type KEY_FACTORY = TypeUtils.parseType("org.springframework.cglib.core.KeyFactory");
	private static final Signature GET_SORT = TypeUtils.parseSignature("int getSort()");
	private static final int[] PRIMES = new int[]{11, 73, 179, 331, 521, 787, 1213, 1823, 2609, 3691, 5189, 7247, 10037,
			13931, 19289, 26627, '轋', '씉', 69403, 95401, 131129, 180179, 247501, 340057, 467063, 641371, 880603,
			1209107, 1660097, 2279161, 3129011, 4295723, 5897291, 8095873, 11114263, 15257791, 20946017, 28754629,
			39474179, 54189869, 74391461, 102123817, 140194277, 192456917, 264202273, 362693231, 497900099, 683510293,
			938313161, 1288102441, 1768288259};
	public static final Customizer CLASS_BY_NAME = new 1();
	public static final FieldTypeCustomizer STORE_CLASS_AS_STRING = new 2();
	public static final HashCodeCustomizer HASH_ASM_TYPE = new 3();

	@Deprecated
   public static final Customizer OBJECT_BY_CLASS = new 4();

	public static KeyFactory create(Class keyInterface) {
		return create(keyInterface, (Customizer) null);
	}

	public static KeyFactory create(Class keyInterface, Customizer customizer) {
		return create(keyInterface.getClassLoader(), keyInterface, customizer);
	}

	public static KeyFactory create(Class keyInterface, KeyFactoryCustomizer first, List<KeyFactoryCustomizer> next) {
		return create(keyInterface.getClassLoader(), keyInterface, first, next);
	}

	public static KeyFactory create(ClassLoader loader, Class keyInterface, Customizer customizer) {
		return create(loader, keyInterface, customizer, Collections.emptyList());
	}

	public static KeyFactory create(ClassLoader loader, Class keyInterface, KeyFactoryCustomizer customizer,
			List<KeyFactoryCustomizer> next) {
		Generator gen = new Generator();
		gen.setInterface(keyInterface);
		if (customizer != null) {
			gen.addCustomizer(customizer);
		}

		if (next != null && !next.isEmpty()) {
			Iterator arg4 = next.iterator();

			while (arg4.hasNext()) {
				KeyFactoryCustomizer keyFactoryCustomizer = (KeyFactoryCustomizer) arg4.next();
				gen.addCustomizer(keyFactoryCustomizer);
			}
		}

		gen.setClassLoader(loader);
		return gen.create();
	}
}