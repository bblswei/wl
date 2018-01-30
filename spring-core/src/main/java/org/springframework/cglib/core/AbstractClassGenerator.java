package org.springframework.cglib.core;

import java.lang.ref.WeakReference;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.WeakHashMap;
import org.springframework.asm.ClassReader;
import org.springframework.cglib.core.ClassGenerator;
import org.springframework.cglib.core.ClassNameReader;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.cglib.core.DefaultGeneratorStrategy;
import org.springframework.cglib.core.DefaultNamingPolicy;
import org.springframework.cglib.core.GeneratorStrategy;
import org.springframework.cglib.core.NamingPolicy;
import org.springframework.cglib.core.Predicate;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.cglib.core.AbstractClassGenerator.ClassLoaderData;
import org.springframework.cglib.core.AbstractClassGenerator.Source;

public abstract class AbstractClassGenerator<T> implements ClassGenerator {
	private static final ThreadLocal CURRENT = new ThreadLocal();
	private static volatile Map<ClassLoader, ClassLoaderData> CACHE = new WeakHashMap();
	private GeneratorStrategy strategy;
	private NamingPolicy namingPolicy;
	private Source source;
	private ClassLoader classLoader;
	private String namePrefix;
	private Object key;
	private boolean useCache;
	private String className;
	private boolean attemptLoad;

	protected T wrapCachedClass(Class klass) {
		return new WeakReference(klass);
	}

	protected Object unwrapCachedValue(T cached) {
		return ((WeakReference) cached).get();
	}

	protected AbstractClassGenerator(Source source) {
		this.strategy = DefaultGeneratorStrategy.INSTANCE;
		this.namingPolicy = DefaultNamingPolicy.INSTANCE;
		this.useCache = true;
		this.source = source;
	}

	protected void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}

	protected final String getClassName() {
		return this.className;
	}

	private void setClassName(String className) {
		this.className = className;
	}

	private String generateClassName(Predicate nameTestPredicate) {
		return this.namingPolicy.getClassName(this.namePrefix, this.source.name, this.key, nameTestPredicate);
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public void setNamingPolicy(NamingPolicy namingPolicy) {
		if (namingPolicy == null) {
			namingPolicy = DefaultNamingPolicy.INSTANCE;
		}

		this.namingPolicy = (NamingPolicy) namingPolicy;
	}

	public NamingPolicy getNamingPolicy() {
		return this.namingPolicy;
	}

	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}

	public boolean getUseCache() {
		return this.useCache;
	}

	public void setAttemptLoad(boolean attemptLoad) {
		this.attemptLoad = attemptLoad;
	}

	public boolean getAttemptLoad() {
		return this.attemptLoad;
	}

	public void setStrategy(GeneratorStrategy strategy) {
		if (strategy == null) {
			strategy = DefaultGeneratorStrategy.INSTANCE;
		}

		this.strategy = (GeneratorStrategy) strategy;
	}

	public GeneratorStrategy getStrategy() {
		return this.strategy;
	}

	public static AbstractClassGenerator getCurrent() {
		return (AbstractClassGenerator) CURRENT.get();
	}

	public ClassLoader getClassLoader() {
		ClassLoader t = this.classLoader;
		if (t == null) {
			t = this.getDefaultClassLoader();
		}

		if (t == null) {
			t = this.getClass().getClassLoader();
		}

		if (t == null) {
			t = Thread.currentThread().getContextClassLoader();
		}

		if (t == null) {
			throw new IllegalStateException("Cannot determine classloader");
		} else {
			return t;
		}
	}

	protected abstract ClassLoader getDefaultClassLoader();

	protected ProtectionDomain getProtectionDomain() {
		return null;
	}

	protected Object create(Object key) {
		try {
			ClassLoader e = this.getClassLoader();
			Map cache = CACHE;
			ClassLoaderData data = (ClassLoaderData) cache.get(e);
			if (data == null) {
				Class obj = AbstractClassGenerator.class;
				synchronized (AbstractClassGenerator.class) {
					cache = CACHE;
					data = (ClassLoaderData) cache.get(e);
					if (data == null) {
						WeakHashMap newCache = new WeakHashMap(cache);
						data = new ClassLoaderData(e);
						newCache.put(e, data);
						CACHE = newCache;
					}
				}
			}

			this.key = key;
			Object obj1 = data.get(this, this.getUseCache());
			return obj1 instanceof Class ? this.firstInstance((Class) obj1) : this.nextInstance(obj1);
		} catch (RuntimeException arg8) {
			throw arg8;
		} catch (Error arg9) {
			throw arg9;
		} catch (Exception arg10) {
			throw new CodeGenerationException(arg10);
		}
	}

	protected Class generate(ClassLoaderData data) {
		Object save = CURRENT.get();
		CURRENT.set(this);

		Class arg7;
		try {
			ClassLoader e = data.getClassLoader();
			if (e == null) {
				throw new IllegalStateException("ClassLoader is null while trying to define class "
						+ this.getClassName()
						+ ". It seems that the loader has been expired from a weak reference somehow. Please file an issue at cglib\'s issue tracker.");
			}

			String className;
			synchronized (e) {
				className = this.generateClassName(data.getUniqueNamePredicate());
				data.reserveName(className);
				this.setClassName(className);
			}

			Class gen;
			if (this.attemptLoad) {
				try {
					gen = e.loadClass(this.getClassName());
					Class b1 = gen;
					return b1;
				} catch (ClassNotFoundException arg19) {
					;
				}
			}

			byte[] b = this.strategy.generate(this);
			className = ClassNameReader.getClassName(new ClassReader(b));
			ProtectionDomain protectionDomain = this.getProtectionDomain();
			synchronized (e) {
				if (protectionDomain == null) {
					gen = ReflectUtils.defineClass(className, b, e);
				} else {
					gen = ReflectUtils.defineClass(className, b, e, protectionDomain);
				}
			}

			arg7 = gen;
		} catch (RuntimeException arg20) {
			throw arg20;
		} catch (Error arg21) {
			throw arg21;
		} catch (Exception arg22) {
			throw new CodeGenerationException(arg22);
		} finally {
			CURRENT.set(save);
		}

		return arg7;
	}

	protected abstract Object firstInstance(Class arg0) throws Exception;

	protected abstract Object nextInstance(Object arg0) throws Exception;
}