package org.springframework.cglib.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.asm.Label;
import org.springframework.asm.Type;
import org.springframework.cglib.core.Block;
import org.springframework.cglib.core.ClassEmitter;
import org.springframework.cglib.core.CodeEmitter;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.cglib.core.Constants;
import org.springframework.cglib.core.Customizer;
import org.springframework.cglib.core.HashCodeCustomizer;
import org.springframework.cglib.core.Local;
import org.springframework.cglib.core.MethodInfo;
import org.springframework.cglib.core.ObjectSwitchCallback;
import org.springframework.cglib.core.ProcessArrayCallback;
import org.springframework.cglib.core.Signature;
import org.springframework.cglib.core.TypeUtils;
import org.springframework.cglib.core.EmitUtils.1;
import org.springframework.cglib.core.EmitUtils.10;
import org.springframework.cglib.core.EmitUtils.11;
import org.springframework.cglib.core.EmitUtils.12;
import org.springframework.cglib.core.EmitUtils.13;
import org.springframework.cglib.core.EmitUtils.14;
import org.springframework.cglib.core.EmitUtils.15;
import org.springframework.cglib.core.EmitUtils.16;
import org.springframework.cglib.core.EmitUtils.2;
import org.springframework.cglib.core.EmitUtils.3;
import org.springframework.cglib.core.EmitUtils.4;
import org.springframework.cglib.core.EmitUtils.5;
import org.springframework.cglib.core.EmitUtils.6;
import org.springframework.cglib.core.EmitUtils.7;
import org.springframework.cglib.core.EmitUtils.8;
import org.springframework.cglib.core.EmitUtils.9;
import org.springframework.cglib.core.EmitUtils.ArrayDelimiters;
import org.springframework.cglib.core.EmitUtils.ParameterTyper;
import org.springframework.cglib.core.internal.CustomizerRegistry;

public class EmitUtils {
	private static final Signature CSTRUCT_NULL = TypeUtils.parseConstructor("");
	private static final Signature CSTRUCT_THROWABLE = TypeUtils.parseConstructor("Throwable");
	private static final Signature GET_NAME = TypeUtils.parseSignature("String getName()");
	private static final Signature HASH_CODE = TypeUtils.parseSignature("int hashCode()");
	private static final Signature EQUALS = TypeUtils.parseSignature("boolean equals(Object)");
	private static final Signature STRING_LENGTH = TypeUtils.parseSignature("int length()");
	private static final Signature STRING_CHAR_AT = TypeUtils.parseSignature("char charAt(int)");
	private static final Signature FOR_NAME = TypeUtils.parseSignature("Class forName(String)");
	private static final Signature DOUBLE_TO_LONG_BITS = TypeUtils.parseSignature("long doubleToLongBits(double)");
	private static final Signature FLOAT_TO_INT_BITS = TypeUtils.parseSignature("int floatToIntBits(float)");
	private static final Signature TO_STRING = TypeUtils.parseSignature("String toString()");
	private static final Signature APPEND_STRING = TypeUtils.parseSignature("StringBuffer append(String)");
	private static final Signature APPEND_INT = TypeUtils.parseSignature("StringBuffer append(int)");
	private static final Signature APPEND_DOUBLE = TypeUtils.parseSignature("StringBuffer append(double)");
	private static final Signature APPEND_FLOAT = TypeUtils.parseSignature("StringBuffer append(float)");
	private static final Signature APPEND_CHAR = TypeUtils.parseSignature("StringBuffer append(char)");
	private static final Signature APPEND_LONG = TypeUtils.parseSignature("StringBuffer append(long)");
	private static final Signature APPEND_BOOLEAN = TypeUtils.parseSignature("StringBuffer append(boolean)");
	private static final Signature LENGTH = TypeUtils.parseSignature("int length()");
	private static final Signature SET_LENGTH = TypeUtils.parseSignature("void setLength(int)");
	private static final Signature GET_DECLARED_METHOD = TypeUtils
			.parseSignature("java.lang.reflect.Method getDeclaredMethod(String, Class[])");
	public static final ArrayDelimiters DEFAULT_DELIMITERS = new ArrayDelimiters("{", ", ", "}");

	public static void factory_method(ClassEmitter ce, Signature sig) {
		CodeEmitter e = ce.begin_method(1, sig, (Type[]) null);
		e.new_instance_this();
		e.dup();
		e.load_args();
		e.invoke_constructor_this(TypeUtils.parseConstructor(sig.getArgumentTypes()));
		e.return_value();
		e.end_method();
	}

	public static void null_constructor(ClassEmitter ce) {
		CodeEmitter e = ce.begin_method(1, CSTRUCT_NULL, (Type[]) null);
		e.load_this();
		e.super_invoke_constructor();
		e.return_value();
		e.end_method();
	}

	public static void process_array(CodeEmitter e, Type type, ProcessArrayCallback callback) {
		Type componentType = TypeUtils.getComponentType(type);
		Local array = e.make_local();
		Local loopvar = e.make_local(Type.INT_TYPE);
		Label loopbody = e.make_label();
		Label checkloop = e.make_label();
		e.store_local(array);
		e.push(0);
		e.store_local(loopvar);
		e.goTo(checkloop);
		e.mark(loopbody);
		e.load_local(array);
		e.load_local(loopvar);
		e.array_load(componentType);
		callback.processElement(componentType);
		e.iinc(loopvar, 1);
		e.mark(checkloop);
		e.load_local(loopvar);
		e.load_local(array);
		e.arraylength();
		e.if_icmp(155, loopbody);
	}

	public static void process_arrays(CodeEmitter e, Type type, ProcessArrayCallback callback) {
		Type componentType = TypeUtils.getComponentType(type);
		Local array1 = e.make_local();
		Local array2 = e.make_local();
		Local loopvar = e.make_local(Type.INT_TYPE);
		Label loopbody = e.make_label();
		Label checkloop = e.make_label();
		e.store_local(array1);
		e.store_local(array2);
		e.push(0);
		e.store_local(loopvar);
		e.goTo(checkloop);
		e.mark(loopbody);
		e.load_local(array1);
		e.load_local(loopvar);
		e.array_load(componentType);
		e.load_local(array2);
		e.load_local(loopvar);
		e.array_load(componentType);
		callback.processElement(componentType);
		e.iinc(loopvar, 1);
		e.mark(checkloop);
		e.load_local(loopvar);
		e.load_local(array1);
		e.arraylength();
		e.if_icmp(155, loopbody);
	}

	public static void string_switch(CodeEmitter e, String[] strings, int switchStyle, ObjectSwitchCallback callback) {
		try {
			switch (switchStyle) {
				case 0 :
					string_switch_trie(e, strings, callback);
					break;
				case 1 :
					string_switch_hash(e, strings, callback, false);
					break;
				case 2 :
					string_switch_hash(e, strings, callback, true);
					break;
				default :
					throw new IllegalArgumentException("unknown switch style " + switchStyle);
			}

		} catch (RuntimeException arg4) {
			throw arg4;
		} catch (Error arg5) {
			throw arg5;
		} catch (Exception arg6) {
			throw new CodeGenerationException(arg6);
		}
	}

	private static void string_switch_trie(CodeEmitter e, String[] strings, ObjectSwitchCallback callback) throws Exception {
      Label def = e.make_label();
      Label end = e.make_label();
      Map buckets = CollectionUtils.bucket(Arrays.asList(strings), new 1());
      e.dup();
      e.invoke_virtual(Constants.TYPE_STRING, STRING_LENGTH);
      e.process_switch(getSwitchKeys(buckets), new 2(buckets, e, callback, def, end));
      e.mark(def);
      e.pop();
      callback.processDefault();
      e.mark(end);
   }

	private static void stringSwitchHelper(CodeEmitter e, List strings, ObjectSwitchCallback callback, Label def, Label end, int index) throws Exception {
      int len = ((String)strings.get(0)).length();
      Map buckets = CollectionUtils.bucket(strings, new 3(index));
      e.dup();
      e.push(index);
      e.invoke_virtual(Constants.TYPE_STRING, STRING_CHAR_AT);
      e.process_switch(getSwitchKeys(buckets), new 4(buckets, index, len, e, callback, end, def));
   }

	static int[] getSwitchKeys(Map buckets) {
		int[] keys = new int[buckets.size()];
		int index = 0;

		for (Iterator it = buckets.keySet().iterator(); it
				.hasNext(); keys[index++] = ((Integer) it.next()).intValue()) {
			;
		}

		Arrays.sort(keys);
		return keys;
	}

	private static void string_switch_hash(CodeEmitter e, String[] strings, ObjectSwitchCallback callback, boolean skipEquals) throws Exception {
      Map buckets = CollectionUtils.bucket(Arrays.asList(strings), new 5());
      Label def = e.make_label();
      Label end = e.make_label();
      e.dup();
      e.invoke_virtual(Constants.TYPE_OBJECT, HASH_CODE);
      e.process_switch(getSwitchKeys(buckets), new 6(buckets, skipEquals, e, callback, end, def));
      e.mark(def);
      callback.processDefault();
      e.mark(end);
   }

	public static void load_class_this(CodeEmitter e) {
		load_class_helper(e, e.getClassEmitter().getClassType());
	}

	public static void load_class(CodeEmitter e, Type type) {
		if (TypeUtils.isPrimitive(type)) {
			if (type == Type.VOID_TYPE) {
				throw new IllegalArgumentException("cannot load void type");
			}

			e.getstatic(TypeUtils.getBoxedType(type), "TYPE", Constants.TYPE_CLASS);
		} else {
			load_class_helper(e, type);
		}

	}

	private static void load_class_helper(CodeEmitter e, Type type) {
		if (e.isStaticHook()) {
			e.push(TypeUtils.emulateClassGetName(type));
			e.invoke_static(Constants.TYPE_CLASS, FOR_NAME);
		} else {
			ClassEmitter ce = e.getClassEmitter();
			String typeName = TypeUtils.emulateClassGetName(type);
			String fieldName = "CGLIB$load_class$" + TypeUtils.escapeType(typeName);
			if (!ce.isFieldDeclared(fieldName)) {
				ce.declare_field(26, fieldName, Constants.TYPE_CLASS, (Object) null);
				CodeEmitter hook = ce.getStaticHook();
				hook.push(typeName);
				hook.invoke_static(Constants.TYPE_CLASS, FOR_NAME);
				hook.putstatic(ce.getClassType(), fieldName, Constants.TYPE_CLASS);
			}

			e.getfield(fieldName);
		}

	}

	public static void push_array(CodeEmitter e, Object[] array) {
		e.push(array.length);
		e.newarray(Type.getType(remapComponentType(array.getClass().getComponentType())));

		for (int i = 0; i < array.length; ++i) {
			e.dup();
			e.push(i);
			push_object(e, array[i]);
			e.aastore();
		}

	}

	private static Class remapComponentType(Class componentType) {
		return componentType.equals(Type.class) ? Class.class : componentType;
	}

	public static void push_object(CodeEmitter e, Object obj) {
		if (obj == null) {
			e.aconst_null();
		} else {
			Class type = obj.getClass();
			if (type.isArray()) {
				push_array(e, (Object[]) ((Object[]) obj));
			} else if (obj instanceof String) {
				e.push((String) obj);
			} else if (obj instanceof Type) {
				load_class(e, (Type) obj);
			} else if (obj instanceof Class) {
				load_class(e, Type.getType((Class) obj));
			} else if (obj instanceof BigInteger) {
				e.new_instance(Constants.TYPE_BIG_INTEGER);
				e.dup();
				e.push(obj.toString());
				e.invoke_constructor(Constants.TYPE_BIG_INTEGER);
			} else {
				if (!(obj instanceof BigDecimal)) {
					throw new IllegalArgumentException("unknown type: " + obj.getClass());
				}

				e.new_instance(Constants.TYPE_BIG_DECIMAL);
				e.dup();
				e.push(obj.toString());
				e.invoke_constructor(Constants.TYPE_BIG_DECIMAL);
			}
		}

	}

	@Deprecated
	public static void hash_code(CodeEmitter e, Type type, int multiplier, Customizer customizer) {
		hash_code(e, type, multiplier, CustomizerRegistry.singleton(customizer));
	}

	public static void hash_code(CodeEmitter e, Type type, int multiplier, CustomizerRegistry registry) {
		if (TypeUtils.isArray(type)) {
			hash_array(e, type, multiplier, registry);
		} else {
			e.swap(Type.INT_TYPE, type);
			e.push(multiplier);
			e.math(104, Type.INT_TYPE);
			e.swap(type, Type.INT_TYPE);
			if (TypeUtils.isPrimitive(type)) {
				hash_primitive(e, type);
			} else {
				hash_object(e, type, registry);
			}

			e.math(96, Type.INT_TYPE);
		}

	}

	private static void hash_array(CodeEmitter e, Type type, int multiplier, CustomizerRegistry registry) {
      Label skip = e.make_label();
      Label end = e.make_label();
      e.dup();
      e.ifnull(skip);
      process_array(e, type, new 7(e, multiplier, registry));
      e.goTo(end);
      e.mark(skip);
      e.pop();
      e.mark(end);
   }

	private static void hash_object(CodeEmitter e, Type type, CustomizerRegistry registry) {
		Label skip = e.make_label();
		Label end = e.make_label();
		e.dup();
		e.ifnull(skip);
		boolean customHashCode = false;
		Iterator arg5 = registry.get(HashCodeCustomizer.class).iterator();

		while (arg5.hasNext()) {
			HashCodeCustomizer customizer = (HashCodeCustomizer) arg5.next();
			if (customizer.customize(e, type)) {
				customHashCode = true;
				break;
			}
		}

		if (!customHashCode) {
			arg5 = registry.get(Customizer.class).iterator();

			while (arg5.hasNext()) {
				Customizer customizer1 = (Customizer) arg5.next();
				customizer1.customize(e, type);
			}

			e.invoke_virtual(Constants.TYPE_OBJECT, HASH_CODE);
		}

		e.goTo(end);
		e.mark(skip);
		e.pop();
		e.push(0);
		e.mark(end);
	}

	private static void hash_primitive(CodeEmitter e, Type type) {
		switch (type.getSort()) {
			case 1 :
				e.push(1);
				e.math(130, Type.INT_TYPE);
			case 2 :
			case 3 :
			case 4 :
			case 5 :
			default :
				break;
			case 6 :
				e.invoke_static(Constants.TYPE_FLOAT, FLOAT_TO_INT_BITS);
				break;
			case 8 :
				e.invoke_static(Constants.TYPE_DOUBLE, DOUBLE_TO_LONG_BITS);
			case 7 :
				hash_long(e);
		}

	}

	private static void hash_long(CodeEmitter e) {
		e.dup2();
		e.push(32);
		e.math(124, Type.LONG_TYPE);
		e.math(130, Type.LONG_TYPE);
		e.cast_numeric(Type.LONG_TYPE, Type.INT_TYPE);
	}

	@Deprecated
	public static void not_equals(CodeEmitter e, Type type, Label notEquals, Customizer customizer) {
		not_equals(e, type, notEquals, CustomizerRegistry.singleton(customizer));
	}

	public static void not_equals(CodeEmitter e, Type type, Label notEquals, CustomizerRegistry registry) {
      (new 8(e, notEquals, registry)).processElement(type);
   }

	private static void not_equals_helper(CodeEmitter e, Type type, Label notEquals, CustomizerRegistry registry,
			ProcessArrayCallback callback) {
		if (TypeUtils.isPrimitive(type)) {
			e.if_cmp(type, 154, notEquals);
		} else {
			Label end = e.make_label();
			nullcmp(e, notEquals, end);
			if (TypeUtils.isArray(type)) {
				Label customizers1 = e.make_label();
				e.dup2();
				e.arraylength();
				e.swap();
				e.arraylength();
				e.if_icmp(153, customizers1);
				e.pop2();
				e.goTo(notEquals);
				e.mark(customizers1);
				process_arrays(e, type, callback);
			} else {
				List customizers = registry.get(Customizer.class);
				if (!customizers.isEmpty()) {
					Iterator arg6 = customizers.iterator();

					Customizer customizer;
					while (arg6.hasNext()) {
						customizer = (Customizer) arg6.next();
						customizer.customize(e, type);
					}

					e.swap();
					arg6 = customizers.iterator();

					while (arg6.hasNext()) {
						customizer = (Customizer) arg6.next();
						customizer.customize(e, type);
					}
				}

				e.invoke_virtual(Constants.TYPE_OBJECT, EQUALS);
				e.if_jump(153, notEquals);
			}

			e.mark(end);
		}

	}

	private static void nullcmp(CodeEmitter e, Label oneNull, Label bothNull) {
		e.dup2();
		Label nonNull = e.make_label();
		Label oneNullHelper = e.make_label();
		Label end = e.make_label();
		e.ifnonnull(nonNull);
		e.ifnonnull(oneNullHelper);
		e.pop2();
		e.goTo(bothNull);
		e.mark(nonNull);
		e.ifnull(oneNullHelper);
		e.goTo(end);
		e.mark(oneNullHelper);
		e.pop2();
		e.goTo(oneNull);
		e.mark(end);
	}

	@Deprecated
	public static void append_string(CodeEmitter e, Type type, ArrayDelimiters delims, Customizer customizer) {
		append_string(e, type, delims, CustomizerRegistry.singleton(customizer));
	}

	public static void append_string(CodeEmitter e, Type type, ArrayDelimiters delims, CustomizerRegistry registry) {
      ArrayDelimiters d = delims != null?delims:DEFAULT_DELIMITERS;
      9 callback = new 9(e, d, registry);
      append_string_helper(e, type, d, registry, callback);
   }

	private static void append_string_helper(CodeEmitter e, Type type, ArrayDelimiters delims,
			CustomizerRegistry registry, ProcessArrayCallback callback) {
		Label skip = e.make_label();
		Label end = e.make_label();
		if (TypeUtils.isPrimitive(type)) {
			switch (type.getSort()) {
				case 1 :
					e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_BOOLEAN);
					break;
				case 2 :
					e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_CHAR);
					break;
				case 3 :
				case 4 :
				case 5 :
					e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_INT);
					break;
				case 6 :
					e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_FLOAT);
					break;
				case 7 :
					e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_LONG);
					break;
				case 8 :
					e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_DOUBLE);
			}
		} else if (TypeUtils.isArray(type)) {
			e.dup();
			e.ifnull(skip);
			e.swap();
			if (delims != null && ArrayDelimiters.access$600(delims) != null
					&& !"".equals(ArrayDelimiters.access$600(delims))) {
				e.push(ArrayDelimiters.access$600(delims));
				e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_STRING);
				e.swap();
			}

			process_array(e, type, callback);
			shrinkStringBuffer(e, 2);
			if (delims != null && ArrayDelimiters.access$700(delims) != null
					&& !"".equals(ArrayDelimiters.access$700(delims))) {
				e.push(ArrayDelimiters.access$700(delims));
				e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_STRING);
			}
		} else {
			e.dup();
			e.ifnull(skip);
			Iterator arg6 = registry.get(Customizer.class).iterator();

			while (arg6.hasNext()) {
				Customizer customizer = (Customizer) arg6.next();
				customizer.customize(e, type);
			}

			e.invoke_virtual(Constants.TYPE_OBJECT, TO_STRING);
			e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_STRING);
		}

		e.goTo(end);
		e.mark(skip);
		e.pop();
		e.push("null");
		e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_STRING);
		e.mark(end);
	}

	private static void shrinkStringBuffer(CodeEmitter e, int amt) {
		e.dup();
		e.dup();
		e.invoke_virtual(Constants.TYPE_STRING_BUFFER, LENGTH);
		e.push(amt);
		e.math(100, Type.INT_TYPE);
		e.invoke_virtual(Constants.TYPE_STRING_BUFFER, SET_LENGTH);
	}

	public static void load_method(CodeEmitter e, MethodInfo method) {
		load_class(e, method.getClassInfo().getType());
		e.push(method.getSignature().getName());
		push_object(e, method.getSignature().getArgumentTypes());
		e.invoke_virtual(Constants.TYPE_CLASS, GET_DECLARED_METHOD);
	}

	public static void method_switch(CodeEmitter e, List methods, ObjectSwitchCallback callback) {
		member_switch_helper(e, methods, callback, true);
	}

	public static void constructor_switch(CodeEmitter e, List constructors, ObjectSwitchCallback callback) {
		member_switch_helper(e, constructors, callback, false);
	}

	private static void member_switch_helper(CodeEmitter e, List members, ObjectSwitchCallback callback, boolean useName) {
      try {
         HashMap ex = new HashMap();
         10 cached = new 10(ex);
         Label def = e.make_label();
         Label end = e.make_label();
         if(useName) {
            e.swap();
            Map buckets = CollectionUtils.bucket(members, new 11());
            String[] names = (String[])((String[])buckets.keySet().toArray(new String[buckets.size()]));
            string_switch(e, names, 1, new 12(e, buckets, callback, cached, def, end));
         } else {
            member_helper_size(e, members, callback, cached, def, end);
         }

         e.mark(def);
         e.pop();
         callback.processDefault();
         e.mark(end);
      } catch (RuntimeException arg9) {
         throw arg9;
      } catch (Error arg10) {
         throw arg10;
      } catch (Exception arg11) {
         throw new CodeGenerationException(arg11);
      }
   }

	private static void member_helper_size(CodeEmitter e, List members, ObjectSwitchCallback callback, ParameterTyper typer, Label def, Label end) throws Exception {
      Map buckets = CollectionUtils.bucket(members, new 13(typer));
      e.dup();
      e.arraylength();
      e.process_switch(getSwitchKeys(buckets), new 14(buckets, e, callback, typer, def, end));
   }

	private static void member_helper_type(CodeEmitter e, List members, ObjectSwitchCallback callback, ParameterTyper typer, Label def, Label end, BitSet checked) throws Exception {
      int index;
      if(members.size() == 1) {
         MethodInfo example = (MethodInfo)members.get(0);
         Type[] buckets = typer.getParameterTypes(example);

         for(index = 0; index < buckets.length; ++index) {
            if(checked == null || !checked.get(index)) {
               e.dup();
               e.aaload(index);
               e.invoke_virtual(Constants.TYPE_CLASS, GET_NAME);
               e.push(TypeUtils.emulateClassGetName(buckets[index]));
               e.invoke_virtual(Constants.TYPE_OBJECT, EQUALS);
               e.if_jump(153, def);
            }
         }

         e.pop();
         callback.processCase(example, end);
      } else {
         Type[] arg12 = typer.getParameterTypes((MethodInfo)members.get(0));
         Map arg13 = null;
         index = -1;

         for(int fbuckets = 0; fbuckets < arg12.length; ++fbuckets) {
            Map test = CollectionUtils.bucket(members, new 15(typer, fbuckets));
            if(arg13 == null || test.size() > arg13.size()) {
               arg13 = test;
               index = fbuckets;
            }
         }

         if(arg13 != null && arg13.size() != 1) {
            checked.set(index);
            e.dup();
            e.aaload(index);
            e.invoke_virtual(Constants.TYPE_CLASS, GET_NAME);
            String[] names = (String[])((String[])arg13.keySet().toArray(new String[arg13.size()]));
            string_switch(e, names, 1, new 16(e, arg13, callback, typer, def, end, checked));
         } else {
            e.goTo(def);
         }
      }

   }

	public static void wrap_throwable(Block block, Type wrapper) {
		CodeEmitter e = block.getCodeEmitter();
		e.catch_exception(block, Constants.TYPE_THROWABLE);
		e.new_instance(wrapper);
		e.dup_x1();
		e.swap();
		e.invoke_constructor(wrapper, CSTRUCT_THROWABLE);
		e.athrow();
	}

	public static void add_properties(ClassEmitter ce, String[] names, Type[] types) {
		for (int i = 0; i < names.length; ++i) {
			String fieldName = "$cglib_prop_" + names[i];
			ce.declare_field(2, fieldName, types[i], (Object) null);
			add_property(ce, names[i], types[i], fieldName);
		}

	}

	public static void add_property(ClassEmitter ce, String name, Type type, String fieldName) {
		String property = TypeUtils.upperFirst(name);
		CodeEmitter e = ce.begin_method(1, new Signature("get" + property, type, Constants.TYPES_EMPTY), (Type[]) null);
		e.load_this();
		e.getfield(fieldName);
		e.return_value();
		e.end_method();
		e = ce.begin_method(1, new Signature("set" + property, Type.VOID_TYPE, new Type[]{type}), (Type[]) null);
		e.load_this();
		e.load_arg(0);
		e.putfield(fieldName);
		e.return_value();
		e.end_method();
	}

	public static void wrap_undeclared_throwable(CodeEmitter e, Block handler, Type[] exceptions, Type wrapper) {
		Object set = exceptions == null ? Collections.EMPTY_SET : new HashSet(Arrays.asList(exceptions));
		if (!((Set) set).contains(Constants.TYPE_THROWABLE)) {
			boolean needThrow = exceptions != null;
			if (!((Set) set).contains(Constants.TYPE_RUNTIME_EXCEPTION)) {
				e.catch_exception(handler, Constants.TYPE_RUNTIME_EXCEPTION);
				needThrow = true;
			}

			if (!((Set) set).contains(Constants.TYPE_ERROR)) {
				e.catch_exception(handler, Constants.TYPE_ERROR);
				needThrow = true;
			}

			if (exceptions != null) {
				for (int i = 0; i < exceptions.length; ++i) {
					e.catch_exception(handler, exceptions[i]);
				}
			}

			if (needThrow) {
				e.athrow();
			}

			e.catch_exception(handler, Constants.TYPE_THROWABLE);
			e.new_instance(wrapper);
			e.dup_x1();
			e.swap();
			e.invoke_constructor(wrapper, CSTRUCT_THROWABLE);
			e.athrow();
		}
	}

	public static CodeEmitter begin_method(ClassEmitter e, MethodInfo method) {
		return begin_method(e, method, method.getModifiers());
	}

	public static CodeEmitter begin_method(ClassEmitter e, MethodInfo method, int access) {
		return e.begin_method(access, method.getSignature(), method.getExceptionTypes());
	}
}