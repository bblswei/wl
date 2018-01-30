package org.springframework.cglib.proxy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.springframework.asm.ClassReader;
import org.springframework.cglib.proxy.BridgeMethodResolver.BridgedFinder;

class BridgeMethodResolver {
	private final Map declToBridge;
	private final ClassLoader classLoader;

	public BridgeMethodResolver(Map declToBridge, ClassLoader classLoader) {
		this.declToBridge = declToBridge;
		this.classLoader = classLoader;
	}

	public Map resolveAll() {
		HashMap resolved = new HashMap();
		Iterator entryIter = this.declToBridge.entrySet().iterator();

		while (entryIter.hasNext()) {
			Entry entry = (Entry) entryIter.next();
			Class owner = (Class) entry.getKey();
			Set bridges = (Set) entry.getValue();

			try {
				(new ClassReader(this.classLoader.getResourceAsStream(owner.getName().replace('.', '/') + ".class")))
						.accept(new BridgedFinder(bridges, resolved), 6);
			} catch (IOException arg6) {
				;
			}
		}

		return resolved;
	}
}