package org.springframework.cglib.core.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.cglib.core.Customizer;
import org.springframework.cglib.core.KeyFactoryCustomizer;

public class CustomizerRegistry {
	private final Class[] customizerTypes;
	private Map<Class, List<KeyFactoryCustomizer>> customizers = new HashMap();

	public CustomizerRegistry(Class[] customizerTypes) {
		this.customizerTypes = customizerTypes;
	}

	public void add(KeyFactoryCustomizer customizer) {
		Class klass = customizer.getClass();
		Class[] arg2 = this.customizerTypes;
		int arg3 = arg2.length;

		for (int arg4 = 0; arg4 < arg3; ++arg4) {
			Class type = arg2[arg4];
			if (type.isAssignableFrom(klass)) {
				Object list = (List) this.customizers.get(type);
				if (list == null) {
					this.customizers.put(type, list = new ArrayList());
				}

				((List) list).add(customizer);
			}
		}

	}

	public <T> List<T> get(Class<T> klass) {
		List list = (List) this.customizers.get(klass);
		return list == null ? Collections.emptyList() : list;
	}

	@Deprecated
	public static CustomizerRegistry singleton(Customizer customizer) {
		CustomizerRegistry registry = new CustomizerRegistry(new Class[]{Customizer.class});
		registry.add(customizer);
		return registry;
	}
}