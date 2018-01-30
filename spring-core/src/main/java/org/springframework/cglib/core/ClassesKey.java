package org.springframework.cglib.core;

import org.springframework.cglib.core.KeyFactory;
import org.springframework.cglib.core.ClassesKey.Key;

public class ClassesKey {
	private static final Key FACTORY = (Key) KeyFactory.create(Key.class);

	public static Object create(Object[] array) {
		return FACTORY.newInstance(classNames(array));
	}

	private static String[] classNames(Object[] objects) {
		if (objects == null) {
			return null;
		} else {
			String[] classNames = new String[objects.length];

			for (int i = 0; i < objects.length; ++i) {
				Object object = objects[i];
				if (object != null) {
					Class aClass = object.getClass();
					classNames[i] = aClass == null ? null : aClass.getName();
				}
			}

			return classNames;
		}
	}
}