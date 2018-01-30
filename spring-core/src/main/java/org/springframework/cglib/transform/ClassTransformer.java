package org.springframework.cglib.transform;

import org.springframework.asm.ClassVisitor;

public abstract class ClassTransformer extends ClassVisitor {
	public ClassTransformer() {
		super(327680);
	}

	public ClassTransformer(int opcode) {
		super(opcode);
	}

	public abstract void setTarget(ClassVisitor arg0);
}