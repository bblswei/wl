package org.springframework.cglib.core;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import org.springframework.asm.Type;
import org.springframework.cglib.core.Predicate;
import org.springframework.cglib.core.TypeUtils;

public class VisibilityPredicate implements Predicate {
	private boolean protectedOk;
	private String pkg;
	private boolean samePackageOk;

	public VisibilityPredicate(Class source, boolean protectedOk) {
		this.protectedOk = protectedOk;
		this.samePackageOk = source.getClassLoader() != null;
		this.pkg = TypeUtils.getPackageName(Type.getType(source));
	}

	public boolean evaluate(Object arg) {
		Member member = (Member) arg;
		int mod = member.getModifiers();
		return Modifier.isPrivate(mod)
				? false
				: (Modifier.isPublic(mod)
						? true
						: (Modifier.isProtected(mod) && this.protectedOk
								? true
								: this.samePackageOk && this.pkg
										.equals(TypeUtils.getPackageName(Type.getType(member.getDeclaringClass())))));
	}
}