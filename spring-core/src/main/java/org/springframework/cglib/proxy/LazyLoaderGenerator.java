package org.springframework.cglib.proxy;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.springframework.asm.Label;
import org.springframework.asm.Type;
import org.springframework.cglib.core.ClassEmitter;
import org.springframework.cglib.core.CodeEmitter;
import org.springframework.cglib.core.Constants;
import org.springframework.cglib.core.MethodInfo;
import org.springframework.cglib.core.Signature;
import org.springframework.cglib.core.TypeUtils;
import org.springframework.cglib.proxy.CallbackGenerator;
import org.springframework.cglib.proxy.CallbackGenerator.Context;

class LazyLoaderGenerator implements CallbackGenerator {
	public static final LazyLoaderGenerator INSTANCE = new LazyLoaderGenerator();
	private static final Signature LOAD_OBJECT = TypeUtils.parseSignature("Object loadObject()");
	private static final Type LAZY_LOADER = TypeUtils.parseType("org.springframework.cglib.proxy.LazyLoader");

	public void generate(ClassEmitter ce, Context context, List methods) {
		HashSet indexes = new HashSet();
		Iterator it = methods.iterator();

		CodeEmitter e;
		while (it.hasNext()) {
			MethodInfo index = (MethodInfo) it.next();
			if (!TypeUtils.isProtected(index.getModifiers())) {
				int delegate = context.getIndex(index);
				indexes.add(new Integer(delegate));
				e = context.beginMethod(ce, index);
				e.load_this();
				e.dup();
				e.invoke_virtual_this(this.loadMethod(delegate));
				e.checkcast(index.getClassInfo().getType());
				e.load_args();
				e.invoke(index);
				e.return_value();
				e.end_method();
			}
		}

		it = indexes.iterator();

		while (it.hasNext()) {
			int index1 = ((Integer) it.next()).intValue();
			String delegate1 = "CGLIB$LAZY_LOADER_" + index1;
			ce.declare_field(2, delegate1, Constants.TYPE_OBJECT, (Object) null);
			e = ce.begin_method(50, this.loadMethod(index1), (Type[]) null);
			e.load_this();
			e.getfield(delegate1);
			e.dup();
			Label end = e.make_label();
			e.ifnonnull(end);
			e.pop();
			e.load_this();
			context.emitCallback(e, index1);
			e.invoke_interface(LAZY_LOADER, LOAD_OBJECT);
			e.dup_x1();
			e.putfield(delegate1);
			e.mark(end);
			e.return_value();
			e.end_method();
		}

	}

	private Signature loadMethod(int index) {
		return new Signature("CGLIB$LOAD_PRIVATE_" + index, Constants.TYPE_OBJECT, Constants.TYPES_EMPTY);
	}

	public void generateStatic(CodeEmitter e, Context context, List methods) {
	}
}