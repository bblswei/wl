package org.springframework.cglib.transform;

import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.ProtectionDomain;
import org.springframework.asm.Attribute;
import org.springframework.asm.ClassReader;
import org.springframework.cglib.core.ClassGenerator;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.cglib.transform.ClassFilter;
import org.springframework.cglib.transform.ClassReaderGenerator;
import org.springframework.cglib.transform.AbstractClassLoader.1;

public abstract class AbstractClassLoader extends ClassLoader {
	private ClassFilter filter;
	private ClassLoader classPath;
	private static ProtectionDomain DOMAIN = (ProtectionDomain)AccessController.doPrivileged(new 1());

	protected AbstractClassLoader(ClassLoader parent, ClassLoader classPath, ClassFilter filter) {
		super(parent);
		this.filter = filter;
		this.classPath = classPath;
	}

	public Class loadClass(String name) throws ClassNotFoundException {
		Class loaded = this.findLoadedClass(name);
		if (loaded != null && loaded.getClassLoader() == this) {
			return loaded;
		} else if (!this.filter.accept(name)) {
			return super.loadClass(name);
		} else {
			ClassReader r;
			try {
				InputStream e = this.classPath.getResourceAsStream(name.replace('.', '/') + ".class");
				if (e == null) {
					throw new ClassNotFoundException(name);
				}

				try {
					r = new ClassReader(e);
				} finally {
					e.close();
				}
			} catch (IOException arg15) {
				throw new ClassNotFoundException(name + ":" + arg15.getMessage());
			}

			try {
				DebuggingClassWriter e1 = new DebuggingClassWriter(2);
				this.getGenerator(r).generateClass(e1);
				byte[] b = e1.toByteArray();
				Class c = super.defineClass(name, b, 0, b.length, DOMAIN);
				this.postProcess(c);
				return c;
			} catch (RuntimeException arg11) {
				throw arg11;
			} catch (Error arg12) {
				throw arg12;
			} catch (Exception arg13) {
				throw new CodeGenerationException(arg13);
			}
		}
	}

	protected ClassGenerator getGenerator(ClassReader r) {
		return new ClassReaderGenerator(r, this.attributes(), this.getFlags());
	}

	protected int getFlags() {
		return 0;
	}

	protected Attribute[] attributes() {
		return null;
	}

	protected void postProcess(Class c) {
	}
}