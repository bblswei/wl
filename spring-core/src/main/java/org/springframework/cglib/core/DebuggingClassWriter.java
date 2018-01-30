package org.springframework.cglib.core;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.security.AccessController;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.ClassWriter;
import org.springframework.cglib.core.DebuggingClassWriter.1;

public class DebuggingClassWriter extends ClassVisitor {
	public static final String DEBUG_LOCATION_PROPERTY = "cglib.debugLocation";
	private static String debugLocation = System.getProperty("cglib.debugLocation");
	private static Constructor traceCtor;
	private String className;
	private String superName;

	public DebuggingClassWriter(int flags) {
		super(327680, new ClassWriter(flags));
	}

	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		this.className = name.replace('/', '.');
		this.superName = superName.replace('/', '.');
		super.visit(version, access, name, signature, superName, interfaces);
	}

	public String getClassName() {
		return this.className;
	}

	public String getSuperName() {
		return this.superName;
	}

	public byte[] toByteArray() {
      return (byte[])((byte[])AccessController.doPrivileged(new 1(this)));
   }

	static {
		if (debugLocation != null) {
			System.err.println("CGLIB debugging enabled, writing to \'" + debugLocation + "\'");

			try {
				Class clazz = Class.forName("org.springframework.asm.util.TraceClassVisitor");
				traceCtor = clazz.getConstructor(new Class[]{ClassVisitor.class, PrintWriter.class});
			} catch (Throwable arg0) {
				;
			}
		}

	}
}