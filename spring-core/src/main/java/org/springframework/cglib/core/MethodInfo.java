package org.springframework.cglib.core;

import org.springframework.asm.Type;
import org.springframework.cglib.core.ClassInfo;
import org.springframework.cglib.core.Signature;

public abstract class MethodInfo {
	public abstract ClassInfo getClassInfo();

	public abstract int getModifiers();

	public abstract Signature getSignature();

	public abstract Type[] getExceptionTypes();

	public boolean equals(Object o) {
		return o == null
				? false
				: (!(o instanceof MethodInfo) ? false : this.getSignature().equals(((MethodInfo) o).getSignature()));
	}

	public int hashCode() {
		return this.getSignature().hashCode();
	}

	public String toString() {
		return this.getSignature().toString();
	}
}