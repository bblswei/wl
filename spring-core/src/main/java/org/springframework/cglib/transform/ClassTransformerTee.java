package org.springframework.cglib.transform;

import org.springframework.asm.ClassVisitor;
import org.springframework.cglib.transform.ClassTransformer;
import org.springframework.cglib.transform.ClassVisitorTee;

public class ClassTransformerTee extends ClassTransformer {
	private ClassVisitor branch;

	public ClassTransformerTee(ClassVisitor branch) {
		super(327680);
		this.branch = branch;
	}

	public void setTarget(ClassVisitor target) {
		this.cv = new ClassVisitorTee(this.branch, target);
	}
}