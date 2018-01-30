package org.springframework.cglib.util;

import org.springframework.asm.Type;
import org.springframework.cglib.core.KeyFactory;
import org.springframework.cglib.core.Signature;
import org.springframework.cglib.core.TypeUtils;
import org.springframework.cglib.util.StringSwitcher.Generator;
import org.springframework.cglib.util.StringSwitcher.StringSwitcherKey;

public abstract class StringSwitcher {
	private static final Type STRING_SWITCHER = TypeUtils.parseType("org.springframework.cglib.util.StringSwitcher");
	private static final Signature INT_VALUE = TypeUtils.parseSignature("int intValue(String)");
	private static final StringSwitcherKey KEY_FACTORY = (StringSwitcherKey) KeyFactory.create(StringSwitcherKey.class);

	public static StringSwitcher create(String[] strings, int[] ints, boolean fixedInput) {
		Generator gen = new Generator();
		gen.setStrings(strings);
		gen.setInts(ints);
		gen.setFixedInput(fixedInput);
		return gen.create();
	}

	public abstract int intValue(String arg0);
}