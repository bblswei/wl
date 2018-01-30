package org.springframework.cglib.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.cglib.core.Signature;
import org.springframework.cglib.proxy.MethodInterceptorGenerator;
import org.springframework.cglib.proxy.MethodProxy.1;
import org.springframework.cglib.proxy.MethodProxy.CreateInfo;
import org.springframework.cglib.proxy.MethodProxy.FastClassInfo;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastClass.Generator;

public class MethodProxy {
	private Signature sig1;
	private Signature sig2;
	private CreateInfo createInfo;
	private final Object initLock = new Object();
	private volatile FastClassInfo fastClassInfo;

	public static MethodProxy create(Class c1, Class c2, String desc, String name1, String name2) {
		MethodProxy proxy = new MethodProxy();
		proxy.sig1 = new Signature(name1, desc);
		proxy.sig2 = new Signature(name2, desc);
		proxy.createInfo = new CreateInfo(c1, c2);
		return proxy;
	}

	private void init() {
      if(this.fastClassInfo == null) {
         Object arg0 = this.initLock;
         synchronized(this.initLock) {
            if(this.fastClassInfo == null) {
               CreateInfo ci = this.createInfo;
               FastClassInfo fci = new FastClassInfo((1)null);
               fci.f1 = helper(ci, ci.c1);
               fci.f2 = helper(ci, ci.c2);
               fci.i1 = fci.f1.getIndex(this.sig1);
               fci.i2 = fci.f2.getIndex(this.sig2);
               this.fastClassInfo = fci;
               this.createInfo = null;
            }
         }
      }

   }

	private static FastClass helper(CreateInfo ci, Class type) {
		Generator g = new Generator();
		g.setType(type);
		g.setClassLoader(ci.c2.getClassLoader());
		g.setNamingPolicy(ci.namingPolicy);
		g.setStrategy(ci.strategy);
		g.setAttemptLoad(ci.attemptLoad);
		return g.create();
	}

	public Signature getSignature() {
		return this.sig1;
	}

	public String getSuperName() {
		return this.sig2.getName();
	}

	public int getSuperIndex() {
		this.init();
		return this.fastClassInfo.i2;
	}

	FastClass getFastClass() {
		this.init();
		return this.fastClassInfo.f1;
	}

	FastClass getSuperFastClass() {
		this.init();
		return this.fastClassInfo.f2;
	}

	public static MethodProxy find(Class type, Signature sig) {
		try {
			Method e = type.getDeclaredMethod("CGLIB$findMethodProxy", MethodInterceptorGenerator.FIND_PROXY_TYPES);
			return (MethodProxy) e.invoke((Object) null, new Object[]{sig});
		} catch (NoSuchMethodException arg2) {
			throw new IllegalArgumentException("Class " + type + " does not use a MethodInterceptor");
		} catch (IllegalAccessException arg3) {
			throw new CodeGenerationException(arg3);
		} catch (InvocationTargetException arg4) {
			throw new CodeGenerationException(arg4);
		}
	}

	public Object invoke(Object obj, Object[] args) throws Throwable {
		try {
			this.init();
			FastClassInfo e = this.fastClassInfo;
			return e.f1.invoke(e.i1, obj, args);
		} catch (InvocationTargetException arg3) {
			throw arg3.getTargetException();
		} catch (IllegalArgumentException arg4) {
			if (this.fastClassInfo.i1 < 0) {
				throw new IllegalArgumentException("Protected method: " + this.sig1);
			} else {
				throw arg4;
			}
		}
	}

	public Object invokeSuper(Object obj, Object[] args) throws Throwable {
		try {
			this.init();
			FastClassInfo e = this.fastClassInfo;
			return e.f2.invoke(e.i2, obj, args);
		} catch (InvocationTargetException arg3) {
			throw arg3.getTargetException();
		}
	}
}