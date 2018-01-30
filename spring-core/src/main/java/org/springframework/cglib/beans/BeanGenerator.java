package org.springframework.cglib.beans;

import java.beans.PropertyDescriptor;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Type;
import org.springframework.cglib.beans.BeanGenerator.BeanGeneratorKey;
import org.springframework.cglib.core.AbstractClassGenerator;
import org.springframework.cglib.core.ClassEmitter;
import org.springframework.cglib.core.Constants;
import org.springframework.cglib.core.EmitUtils;
import org.springframework.cglib.core.KeyFactory;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.cglib.core.AbstractClassGenerator.Source;

public class BeanGenerator extends AbstractClassGenerator {
	private static final Source SOURCE = new Source(BeanGenerator.class.getName());
	private static final BeanGeneratorKey KEY_FACTORY = (BeanGeneratorKey) KeyFactory.create(BeanGeneratorKey.class);
	private Class superclass;
	private Map props = new HashMap();
	private boolean classOnly;

	public BeanGenerator() {
		super(SOURCE);
	}

	public void setSuperclass(Class superclass) {
		if (superclass != null && superclass.equals(Object.class)) {
			superclass = null;
		}

		this.superclass = superclass;
	}

	public void addProperty(String name, Class type) {
		if (this.props.containsKey(name)) {
			throw new IllegalArgumentException("Duplicate property name \"" + name + "\"");
		} else {
			this.props.put(name, Type.getType(type));
		}
	}

	protected ClassLoader getDefaultClassLoader() {
		return this.superclass != null ? this.superclass.getClassLoader() : null;
	}

	protected ProtectionDomain getProtectionDomain() {
		return ReflectUtils.getProtectionDomain(this.superclass);
	}

	public Object create() {
		this.classOnly = false;
		return this.createHelper();
	}

	public Object createClass() {
		this.classOnly = true;
		return this.createHelper();
	}

	private Object createHelper() {
		if (this.superclass != null) {
			this.setNamePrefix(this.superclass.getName());
		}

		String superName = this.superclass != null ? this.superclass.getName() : "java.lang.Object";
		Object key = KEY_FACTORY.newInstance(superName, this.props);
		return super.create(key);
	}

	public void generateClass(ClassVisitor v) throws Exception {
		int size = this.props.size();
		String[] names = (String[]) ((String[]) this.props.keySet().toArray(new String[size]));
		Type[] types = new Type[size];

		for (int ce = 0; ce < size; ++ce) {
			types[ce] = (Type) this.props.get(names[ce]);
		}

		ClassEmitter arg5 = new ClassEmitter(v);
		arg5.begin_class(46, 1, this.getClassName(),
				this.superclass != null ? Type.getType(this.superclass) : Constants.TYPE_OBJECT, (Type[]) null,
				(String) null);
		EmitUtils.null_constructor(arg5);
		EmitUtils.add_properties(arg5, names, types);
		arg5.end_class();
	}

	protected Object firstInstance(Class type) {
		return this.classOnly ? type : ReflectUtils.newInstance(type);
	}

	protected Object nextInstance(Object instance) {
		Class protoclass = instance instanceof Class ? (Class) instance : instance.getClass();
		return this.classOnly ? protoclass : ReflectUtils.newInstance(protoclass);
	}

	public static void addProperties(BeanGenerator gen, Map props) {
		Iterator it = props.keySet().iterator();

		while (it.hasNext()) {
			String name = (String) it.next();
			gen.addProperty(name, (Class) props.get(name));
		}

	}

	public static void addProperties(BeanGenerator gen, Class type) {
		addProperties(gen, ReflectUtils.getBeanProperties(type));
	}

	public static void addProperties(BeanGenerator gen, PropertyDescriptor[] descriptors) {
		for (int i = 0; i < descriptors.length; ++i) {
			gen.addProperty(descriptors[i].getName(), descriptors[i].getPropertyType());
		}

	}
}