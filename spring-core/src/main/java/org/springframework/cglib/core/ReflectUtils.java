package org.springframework.cglib.core;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.springframework.asm.Type;
import org.springframework.cglib.core.ClassInfo;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.cglib.core.Constants;
import org.springframework.cglib.core.MethodInfo;
import org.springframework.cglib.core.Signature;
import org.springframework.cglib.core.TypeUtils;
import org.springframework.cglib.core.ReflectUtils.1;
import org.springframework.cglib.core.ReflectUtils.2;
import org.springframework.cglib.core.ReflectUtils.3;
import org.springframework.cglib.core.ReflectUtils.4;
import org.springframework.cglib.core.ReflectUtils.5;
import org.springframework.cglib.core.ReflectUtils.6;

public class ReflectUtils {
	private static final Map primitives = new HashMap(8);
	private static final Map transforms = new HashMap(8);
	private static final ClassLoader defaultLoader = ReflectUtils.class.getClassLoader();
	private static Method DEFINE_CLASS;
	private static Method DEFINE_CLASS_UNSAFE;
	private static final ProtectionDomain PROTECTION_DOMAIN;
	private static final Object UNSAFE;
	private static final Throwable THROWABLE;
	private static final List<Method> OBJECT_METHODS = new ArrayList();
	private static final String[] CGLIB_PACKAGES;

	public static ProtectionDomain getProtectionDomain(Class source) {
      return source == null?null:(ProtectionDomain)AccessController.doPrivileged(new 4(source));
   }

	public static Type[] getExceptionTypes(Member member) {
		if (member instanceof Method) {
			return TypeUtils.getTypes(((Method) member).getExceptionTypes());
		} else if (member instanceof Constructor) {
			return TypeUtils.getTypes(((Constructor) member).getExceptionTypes());
		} else {
			throw new IllegalArgumentException("Cannot get exception types of a field");
		}
	}

	public static Signature getSignature(Member member) {
		if (member instanceof Method) {
			return new Signature(member.getName(), Type.getMethodDescriptor((Method) member));
		} else if (member instanceof Constructor) {
			Type[] types = TypeUtils.getTypes(((Constructor) member).getParameterTypes());
			return new Signature("<init>", Type.getMethodDescriptor(Type.VOID_TYPE, types));
		} else {
			throw new IllegalArgumentException("Cannot get signature of a field");
		}
	}

	public static Constructor findConstructor(String desc) {
		return findConstructor(desc, defaultLoader);
	}

	public static Constructor findConstructor(String desc, ClassLoader loader) {
		try {
			int e = desc.indexOf(40);
			String className = desc.substring(0, e).trim();
			return getClass(className, loader).getConstructor(parseTypes(desc, loader));
		} catch (ClassNotFoundException arg3) {
			throw new CodeGenerationException(arg3);
		} catch (NoSuchMethodException arg4) {
			throw new CodeGenerationException(arg4);
		}
	}

	public static Method findMethod(String desc) {
		return findMethod(desc, defaultLoader);
	}

	public static Method findMethod(String desc, ClassLoader loader) {
		try {
			int e = desc.indexOf(40);
			int dot = desc.lastIndexOf(46, e);
			String className = desc.substring(0, dot).trim();
			String methodName = desc.substring(dot + 1, e).trim();
			return getClass(className, loader).getDeclaredMethod(methodName, parseTypes(desc, loader));
		} catch (ClassNotFoundException arg5) {
			throw new CodeGenerationException(arg5);
		} catch (NoSuchMethodException arg6) {
			throw new CodeGenerationException(arg6);
		}
	}

	private static Class[] parseTypes(String desc, ClassLoader loader) throws ClassNotFoundException {
		int lparen = desc.indexOf(40);
		int rparen = desc.indexOf(41, lparen);
		ArrayList params = new ArrayList();
		int start = lparen + 1;

		while (true) {
			int types = desc.indexOf(44, start);
			if (types < 0) {
				if (start < rparen) {
					params.add(desc.substring(start, rparen).trim());
				}

				Class[] arg7 = new Class[params.size()];

				for (int i = 0; i < arg7.length; ++i) {
					arg7[i] = getClass((String) params.get(i), loader);
				}

				return arg7;
			}

			params.add(desc.substring(start, types).trim());
			start = types + 1;
		}
	}

	private static Class getClass(String className, ClassLoader loader) throws ClassNotFoundException {
		return getClass(className, loader, CGLIB_PACKAGES);
	}

	private static Class getClass(String className, ClassLoader loader, String[] packages)
			throws ClassNotFoundException {
		String save = className;
		int dimensions = 0;

		for (int index = 0; (index = className.indexOf("[]", index) + 1) > 0; ++dimensions) {
			;
		}

		StringBuffer brackets = new StringBuffer(className.length() - dimensions);

		for (int prefix = 0; prefix < dimensions; ++prefix) {
			brackets.append('[');
		}

		className = className.substring(0, className.length() - 2 * dimensions);
		String arg13 = dimensions > 0 ? brackets + "L" : "";
		String suffix = dimensions > 0 ? ";" : "";

		try {
			return Class.forName(arg13 + className + suffix, false, loader);
		} catch (ClassNotFoundException arg12) {
			int transform = 0;

			while (transform < packages.length) {
				try {
					return Class.forName(arg13 + packages[transform] + '.' + className + suffix, false, loader);
				} catch (ClassNotFoundException arg11) {
					++transform;
				}
			}

			if (dimensions == 0) {
				Class arg14 = (Class) primitives.get(className);
				if (arg14 != null) {
					return arg14;
				}
			} else {
				String arg15 = (String) transforms.get(className);
				if (arg15 != null) {
					try {
						return Class.forName(brackets + arg15, false, loader);
					} catch (ClassNotFoundException arg10) {
						;
					}
				}
			}

			throw new ClassNotFoundException(save);
		}
	}

	public static Object newInstance(Class type) {
		return newInstance(type, Constants.EMPTY_CLASS_ARRAY, (Object[]) null);
	}

	public static Object newInstance(Class type, Class[] parameterTypes, Object[] args) {
		return newInstance(getConstructor(type, parameterTypes), args);
	}

	public static Object newInstance(Constructor cstruct, Object[] args) {
		boolean flag = cstruct.isAccessible();

		Object arg3;
		try {
			if (!flag) {
				cstruct.setAccessible(true);
			}

			Object e = cstruct.newInstance(args);
			arg3 = e;
		} catch (InstantiationException arg9) {
			throw new CodeGenerationException(arg9);
		} catch (IllegalAccessException arg10) {
			throw new CodeGenerationException(arg10);
		} catch (InvocationTargetException arg11) {
			throw new CodeGenerationException(arg11.getTargetException());
		} finally {
			if (!flag) {
				cstruct.setAccessible(flag);
			}

		}

		return arg3;
	}

	public static Constructor getConstructor(Class type, Class[] parameterTypes) {
		try {
			Constructor e = type.getDeclaredConstructor(parameterTypes);
			e.setAccessible(true);
			return e;
		} catch (NoSuchMethodException arg2) {
			throw new CodeGenerationException(arg2);
		}
	}

	public static String[] getNames(Class[] classes) {
		if (classes == null) {
			return null;
		} else {
			String[] names = new String[classes.length];

			for (int i = 0; i < names.length; ++i) {
				names[i] = classes[i].getName();
			}

			return names;
		}
	}

	public static Class[] getClasses(Object[] objects) {
		Class[] classes = new Class[objects.length];

		for (int i = 0; i < objects.length; ++i) {
			classes[i] = objects[i].getClass();
		}

		return classes;
	}

	public static Method findNewInstance(Class iface) {
		Method m = findInterfaceMethod(iface);
		if (!m.getName().equals("newInstance")) {
			throw new IllegalArgumentException(iface + " missing newInstance method");
		} else {
			return m;
		}
	}

	public static Method[] getPropertyMethods(PropertyDescriptor[] properties, boolean read, boolean write) {
		HashSet methods = new HashSet();

		for (int i = 0; i < properties.length; ++i) {
			PropertyDescriptor pd = properties[i];
			if (read) {
				methods.add(pd.getReadMethod());
			}

			if (write) {
				methods.add(pd.getWriteMethod());
			}
		}

		methods.remove((Object) null);
		return (Method[]) ((Method[]) methods.toArray(new Method[methods.size()]));
	}

	public static PropertyDescriptor[] getBeanProperties(Class type) {
		return getPropertiesHelper(type, true, true);
	}

	public static PropertyDescriptor[] getBeanGetters(Class type) {
		return getPropertiesHelper(type, true, false);
	}

	public static PropertyDescriptor[] getBeanSetters(Class type) {
		return getPropertiesHelper(type, false, true);
	}

	private static PropertyDescriptor[] getPropertiesHelper(Class type, boolean read, boolean write) {
		try {
			BeanInfo e = Introspector.getBeanInfo(type, Object.class);
			PropertyDescriptor[] all = e.getPropertyDescriptors();
			if (read && write) {
				return all;
			} else {
				ArrayList properties = new ArrayList(all.length);

				for (int i = 0; i < all.length; ++i) {
					PropertyDescriptor pd = all[i];
					if (read && pd.getReadMethod() != null || write && pd.getWriteMethod() != null) {
						properties.add(pd);
					}
				}

				return (PropertyDescriptor[]) ((PropertyDescriptor[]) properties
						.toArray(new PropertyDescriptor[properties.size()]));
			}
		} catch (IntrospectionException arg7) {
			throw new CodeGenerationException(arg7);
		}
	}

	public static Method findDeclaredMethod(Class type, String methodName, Class[] parameterTypes)
			throws NoSuchMethodException {
		Class cl = type;

		while (cl != null) {
			try {
				return cl.getDeclaredMethod(methodName, parameterTypes);
			} catch (NoSuchMethodException arg4) {
				cl = cl.getSuperclass();
			}
		}

		throw new NoSuchMethodException(methodName);
	}

	public static List addAllMethods(Class type, List list) {
		if (type == Object.class) {
			list.addAll(OBJECT_METHODS);
		} else {
			list.addAll(Arrays.asList(type.getDeclaredMethods()));
		}

		Class superclass = type.getSuperclass();
		if (superclass != null) {
			addAllMethods(superclass, list);
		}

		Class[] interfaces = type.getInterfaces();

		for (int i = 0; i < interfaces.length; ++i) {
			addAllMethods(interfaces[i], list);
		}

		return list;
	}

	public static List addAllInterfaces(Class type, List list) {
		Class superclass = type.getSuperclass();
		if (superclass != null) {
			list.addAll(Arrays.asList(type.getInterfaces()));
			addAllInterfaces(superclass, list);
		}

		return list;
	}

	public static Method findInterfaceMethod(Class iface) {
		if (!iface.isInterface()) {
			throw new IllegalArgumentException(iface + " is not an interface");
		} else {
			Method[] methods = iface.getDeclaredMethods();
			if (methods.length != 1) {
				throw new IllegalArgumentException("expecting exactly 1 method in " + iface);
			} else {
				return methods[0];
			}
		}
	}

	public static Class defineClass(String className, byte[] b, ClassLoader loader) throws Exception {
		return defineClass(className, b, loader, PROTECTION_DOMAIN);
	}

	public static Class defineClass(String className, byte[] b, ClassLoader loader, ProtectionDomain protectionDomain)
			throws Exception {
		Object[] args;
		Class c;
		if (DEFINE_CLASS != null) {
			args = new Object[]{className, b, new Integer(0), new Integer(b.length), protectionDomain};
			c = (Class) DEFINE_CLASS.invoke(loader, args);
		} else {
			if (DEFINE_CLASS_UNSAFE == null) {
				throw new CodeGenerationException(THROWABLE);
			}

			args = new Object[]{className, b, new Integer(0), new Integer(b.length), loader, protectionDomain};
			c = (Class) DEFINE_CLASS_UNSAFE.invoke(UNSAFE, args);
		}

		Class.forName(className, true, loader);
		return c;
	}

	public static int findPackageProtected(Class[] classes) {
		for (int i = 0; i < classes.length; ++i) {
			if (!Modifier.isPublic(classes[i].getModifiers())) {
				return i;
			}
		}

		return 0;
	}

	public static MethodInfo getMethodInfo(Member member, int modifiers) {
      Signature sig = getSignature(member);
      return new 5(member, modifiers, sig);
   }

	public static MethodInfo getMethodInfo(Member member) {
		return getMethodInfo(member, member.getModifiers());
	}

	public static ClassInfo getClassInfo(Class clazz) {
      Type type = Type.getType(clazz);
      Type sc = clazz.getSuperclass() == null?null:Type.getType(clazz.getSuperclass());
      return new 6(type, sc, clazz);
   }

	public static Method[] findMethods(String[] namesAndDescriptors, Method[] methods) {
		HashMap map = new HashMap();

		for (int result = 0; result < methods.length; ++result) {
			Method i = methods[result];
			map.put(i.getName() + Type.getMethodDescriptor(i), i);
		}

		Method[] arg4 = new Method[namesAndDescriptors.length / 2];

		for (int arg5 = 0; arg5 < arg4.length; ++arg5) {
			arg4[arg5] = (Method) map.get(namesAndDescriptors[arg5 * 2] + namesAndDescriptors[arg5 * 2 + 1]);
			if (arg4[arg5] == null) {
				;
			}
		}

		return arg4;
	}

	static {
      Throwable throwable = null;

      ProtectionDomain protectionDomain;
      Method defineClass;
      Method defineClassUnsafe;
      Object unsafe;
      try {
         protectionDomain = getProtectionDomain(ReflectUtils.class);

         try {
            defineClass = (Method)AccessController.doPrivileged(new 1());
            defineClassUnsafe = null;
            unsafe = null;
         } catch (Throwable arg6) {
            throwable = arg6;
            defineClass = null;
            unsafe = AccessController.doPrivileged(new 2());
            Class u = Class.forName("sun.misc.Unsafe");
            defineClassUnsafe = u.getMethod("defineClass", new Class[]{String.class, byte[].class, Integer.TYPE, Integer.TYPE, ClassLoader.class, ProtectionDomain.class});
         }

         AccessController.doPrivileged(new 3());
      } catch (Throwable arg7) {
         if(throwable == null) {
            throwable = arg7;
         }

         protectionDomain = null;
         defineClass = null;
         defineClassUnsafe = null;
         unsafe = null;
      }

      PROTECTION_DOMAIN = protectionDomain;
      DEFINE_CLASS = defineClass;
      DEFINE_CLASS_UNSAFE = defineClassUnsafe;
      UNSAFE = unsafe;
      THROWABLE = throwable;
      CGLIB_PACKAGES = new String[]{"java.lang"};
      primitives.put("byte", Byte.TYPE);
      primitives.put("char", Character.TYPE);
      primitives.put("double", Double.TYPE);
      primitives.put("float", Float.TYPE);
      primitives.put("int", Integer.TYPE);
      primitives.put("long", Long.TYPE);
      primitives.put("short", Short.TYPE);
      primitives.put("boolean", Boolean.TYPE);
      transforms.put("byte", "B");
      transforms.put("char", "C");
      transforms.put("double", "D");
      transforms.put("float", "F");
      transforms.put("int", "I");
      transforms.put("long", "J");
      transforms.put("short", "S");
      transforms.put("boolean", "Z");
   }
}