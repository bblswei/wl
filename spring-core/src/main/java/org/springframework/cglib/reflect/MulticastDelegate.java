package org.springframework.cglib.reflect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.cglib.reflect.MulticastDelegate.Generator;

public abstract class MulticastDelegate implements Cloneable {
	protected Object[] targets = new Object[0];

	public List getTargets() {
		return new ArrayList(Arrays.asList(this.targets));
	}

	public abstract MulticastDelegate add(Object arg0);

	protected MulticastDelegate addHelper(Object target) {
		MulticastDelegate copy = this.newInstance();
		copy.targets = new Object[this.targets.length + 1];
		System.arraycopy(this.targets, 0, copy.targets, 0, this.targets.length);
		copy.targets[this.targets.length] = target;
		return copy;
	}

	public MulticastDelegate remove(Object target) {
		for (int i = this.targets.length - 1; i >= 0; --i) {
			if (this.targets[i].equals(target)) {
				MulticastDelegate copy = this.newInstance();
				copy.targets = new Object[this.targets.length - 1];
				System.arraycopy(this.targets, 0, copy.targets, 0, i);
				System.arraycopy(this.targets, i + 1, copy.targets, i, this.targets.length - i - 1);
				return copy;
			}
		}

		return this;
	}

	public abstract MulticastDelegate newInstance();

	public static MulticastDelegate create(Class iface) {
		Generator gen = new Generator();
		gen.setInterface(iface);
		return gen.create();
	}
}