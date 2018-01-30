package org.springframework.cglib.transform.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.asm.Type;
import org.springframework.cglib.core.CodeEmitter;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.cglib.core.Constants;
import org.springframework.cglib.core.EmitUtils;
import org.springframework.cglib.core.Signature;
import org.springframework.cglib.core.TypeUtils;
import org.springframework.cglib.transform.ClassEmitterTransformer;
import org.springframework.cglib.transform.impl.FieldProviderTransformer.1;
import org.springframework.cglib.transform.impl.FieldProviderTransformer.2;
import org.springframework.cglib.transform.impl.FieldProviderTransformer.3;
import org.springframework.cglib.transform.impl.FieldProviderTransformer.4;

public class FieldProviderTransformer extends ClassEmitterTransformer {
	private static final String FIELD_NAMES = "CGLIB$FIELD_NAMES";
	private static final String FIELD_TYPES = "CGLIB$FIELD_TYPES";
	private static final Type FIELD_PROVIDER = TypeUtils
			.parseType("org.springframework.cglib.transform.impl.FieldProvider");
	private static final Type ILLEGAL_ARGUMENT_EXCEPTION = TypeUtils.parseType("IllegalArgumentException");
	private static final Signature PROVIDER_GET = TypeUtils.parseSignature("Object getField(String)");
	private static final Signature PROVIDER_SET = TypeUtils.parseSignature("void setField(String, Object)");
	private static final Signature PROVIDER_SET_BY_INDEX = TypeUtils.parseSignature("void setField(int, Object)");
	private static final Signature PROVIDER_GET_BY_INDEX = TypeUtils.parseSignature("Object getField(int)");
	private static final Signature PROVIDER_GET_TYPES = TypeUtils.parseSignature("Class[] getFieldTypes()");
	private static final Signature PROVIDER_GET_NAMES = TypeUtils.parseSignature("String[] getFieldNames()");
	private int access;
	private Map fields;

	public void begin_class(int version, int access, String className, Type superType, Type[] interfaces,
			String sourceFile) {
		if (!TypeUtils.isAbstract(access)) {
			interfaces = TypeUtils.add(interfaces, FIELD_PROVIDER);
		}

		this.access = access;
		this.fields = new HashMap();
		super.begin_class(version, access, className, superType, interfaces, sourceFile);
	}

	public void declare_field(int access, String name, Type type, Object value) {
		super.declare_field(access, name, type, value);
		if (!TypeUtils.isStatic(access)) {
			this.fields.put(name, type);
		}

	}

	public void end_class() {
		if (!TypeUtils.isInterface(this.access)) {
			try {
				this.generate();
			} catch (RuntimeException arg1) {
				throw arg1;
			} catch (Exception arg2) {
				throw new CodeGenerationException(arg2);
			}
		}

		super.end_class();
	}

	private void generate() throws Exception {
		String[] names = (String[]) ((String[]) this.fields.keySet().toArray(new String[this.fields.size()]));
		int[] indexes = new int[names.length];

		for (int i = 0; i < indexes.length; indexes[i] = i++) {
			;
		}

		super.declare_field(26, "CGLIB$FIELD_NAMES", Constants.TYPE_STRING_ARRAY, (Object) null);
		super.declare_field(26, "CGLIB$FIELD_TYPES", Constants.TYPE_CLASS_ARRAY, (Object) null);
		this.initFieldProvider(names);
		this.getNames();
		this.getTypes();
		this.getField(names);
		this.setField(names);
		this.setByIndex(names, indexes);
		this.getByIndex(names, indexes);
	}

	private void initFieldProvider(String[] names) {
		CodeEmitter e = this.getStaticHook();
		EmitUtils.push_object(e, names);
		e.putstatic(this.getClassType(), "CGLIB$FIELD_NAMES", Constants.TYPE_STRING_ARRAY);
		e.push(names.length);
		e.newarray(Constants.TYPE_CLASS);
		e.dup();

		for (int i = 0; i < names.length; ++i) {
			e.dup();
			e.push(i);
			Type type = (Type) this.fields.get(names[i]);
			EmitUtils.load_class(e, type);
			e.aastore();
		}

		e.putstatic(this.getClassType(), "CGLIB$FIELD_TYPES", Constants.TYPE_CLASS_ARRAY);
	}

	private void getNames() {
		CodeEmitter e = super.begin_method(1, PROVIDER_GET_NAMES, (Type[]) null);
		e.getstatic(this.getClassType(), "CGLIB$FIELD_NAMES", Constants.TYPE_STRING_ARRAY);
		e.return_value();
		e.end_method();
	}

	private void getTypes() {
		CodeEmitter e = super.begin_method(1, PROVIDER_GET_TYPES, (Type[]) null);
		e.getstatic(this.getClassType(), "CGLIB$FIELD_TYPES", Constants.TYPE_CLASS_ARRAY);
		e.return_value();
		e.end_method();
	}

	private void setByIndex(String[] names, int[] indexes) throws Exception {
      CodeEmitter e = super.begin_method(1, PROVIDER_SET_BY_INDEX, (Type[])null);
      e.load_this();
      e.load_arg(1);
      e.load_arg(0);
      e.process_switch(indexes, new 1(this, names, e));
      e.end_method();
   }

	private void getByIndex(String[] names, int[] indexes) throws Exception {
      CodeEmitter e = super.begin_method(1, PROVIDER_GET_BY_INDEX, (Type[])null);
      e.load_this();
      e.load_arg(0);
      e.process_switch(indexes, new 2(this, names, e));
      e.end_method();
   }

	private void getField(String[] names) throws Exception {
      CodeEmitter e = this.begin_method(1, PROVIDER_GET, (Type[])null);
      e.load_this();
      e.load_arg(0);
      EmitUtils.string_switch(e, names, 1, new 3(this, e));
      e.end_method();
   }

	private void setField(String[] names) throws Exception {
      CodeEmitter e = this.begin_method(1, PROVIDER_SET, (Type[])null);
      e.load_this();
      e.load_arg(1);
      e.load_arg(0);
      EmitUtils.string_switch(e, names, 1, new 4(this, e));
      e.end_method();
   }
}