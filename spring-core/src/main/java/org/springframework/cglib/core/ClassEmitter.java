package org.springframework.cglib.core;

import java.util.HashMap;
import java.util.Map;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.FieldVisitor;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Type;
import org.springframework.cglib.core.ClassInfo;
import org.springframework.cglib.core.CodeEmitter;
import org.springframework.cglib.core.Constants;
import org.springframework.cglib.core.Signature;
import org.springframework.cglib.core.TypeUtils;
import org.springframework.cglib.core.ClassEmitter.1;
import org.springframework.cglib.core.ClassEmitter.2;
import org.springframework.cglib.core.ClassEmitter.3;
import org.springframework.cglib.core.ClassEmitter.FieldInfo;
import org.springframework.cglib.transform.ClassTransformer;

public class ClassEmitter extends ClassTransformer {
	private ClassInfo classInfo;
	private Map fieldInfo;
	private static int hookCounter;
	private MethodVisitor rawStaticInit;
	private CodeEmitter staticInit;
	private CodeEmitter staticHook;
	private Signature staticHookSig;

	public ClassEmitter(ClassVisitor cv) {
		this.setTarget(cv);
	}

	public ClassEmitter() {
		super(327680);
	}

	public void setTarget(ClassVisitor cv) {
		this.cv = cv;
		this.fieldInfo = new HashMap();
		this.staticInit = this.staticHook = null;
		this.staticHookSig = null;
	}

	private static synchronized int getNextHook() {
		return ++hookCounter;
	}

	public ClassInfo getClassInfo() {
		return this.classInfo;
	}

	public void begin_class(int version, int access, String className, Type superType, Type[] interfaces, String source) {
      Type classType = Type.getType("L" + className.replace('.', '/') + ";");
      this.classInfo = new 1(this, classType, superType, interfaces, access);
      this.cv.visit(version, access, this.classInfo.getType().getInternalName(), (String)null, this.classInfo.getSuperType().getInternalName(), TypeUtils.toInternalNames(interfaces));
      if(source != null) {
         this.cv.visitSource(source, (String)null);
      }

      this.init();
   }

	public CodeEmitter getStaticHook() {
		if (TypeUtils.isInterface(this.getAccess())) {
			throw new IllegalStateException("static hook is invalid for this class");
		} else {
			if (this.staticHook == null) {
				this.staticHookSig = new Signature("CGLIB$STATICHOOK" + getNextHook(), "()V");
				this.staticHook = this.begin_method(8, this.staticHookSig, (Type[]) null);
				if (this.staticInit != null) {
					this.staticInit.invoke_static_this(this.staticHookSig);
				}
			}

			return this.staticHook;
		}
	}

	protected void init() {
	}

	public int getAccess() {
		return this.classInfo.getModifiers();
	}

	public Type getClassType() {
		return this.classInfo.getType();
	}

	public Type getSuperType() {
		return this.classInfo.getSuperType();
	}

	public void end_class() {
		if (this.staticHook != null && this.staticInit == null) {
			this.begin_static();
		}

		if (this.staticInit != null) {
			this.staticHook.return_value();
			this.staticHook.end_method();
			this.rawStaticInit.visitInsn(177);
			this.rawStaticInit.visitMaxs(0, 0);
			this.staticInit = this.staticHook = null;
			this.staticHookSig = null;
		}

		this.cv.visitEnd();
	}

	public CodeEmitter begin_method(int access, Signature sig, Type[] exceptions) {
      if(this.classInfo == null) {
         throw new IllegalStateException("classInfo is null! " + this);
      } else {
         MethodVisitor v = this.cv.visitMethod(access, sig.getName(), sig.getDescriptor(), (String)null, TypeUtils.toInternalNames(exceptions));
         if(sig.equals(Constants.SIG_STATIC) && !TypeUtils.isInterface(this.getAccess())) {
            this.rawStaticInit = v;
            2 wrapped = new 2(this, 327680, v);
            this.staticInit = new CodeEmitter(this, wrapped, access, sig, exceptions);
            if(this.staticHook == null) {
               this.getStaticHook();
            } else {
               this.staticInit.invoke_static_this(this.staticHookSig);
            }

            return this.staticInit;
         } else {
            return (CodeEmitter)(sig.equals(this.staticHookSig)?new 3(this, this, v, access, sig, exceptions):new CodeEmitter(this, v, access, sig, exceptions));
         }
      }
   }

	public CodeEmitter begin_static() {
		return this.begin_method(8, Constants.SIG_STATIC, (Type[]) null);
	}

	public void declare_field(int access, String name, Type type, Object value) {
		FieldInfo existing = (FieldInfo) this.fieldInfo.get(name);
		FieldInfo info = new FieldInfo(access, name, type, value);
		if (existing != null) {
			if (!info.equals(existing)) {
				throw new IllegalArgumentException("Field \"" + name + "\" has been declared differently");
			}
		} else {
			this.fieldInfo.put(name, info);
			this.cv.visitField(access, name, type.getDescriptor(), (String) null, value);
		}

	}

	boolean isFieldDeclared(String name) {
		return this.fieldInfo.get(name) != null;
	}

	FieldInfo getFieldInfo(String name) {
		FieldInfo field = (FieldInfo) this.fieldInfo.get(name);
		if (field == null) {
			throw new IllegalArgumentException(
					"Field " + name + " is not declared in " + this.getClassType().getClassName());
		} else {
			return field;
		}
	}

	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		this.begin_class(version, access, name.replace('/', '.'), TypeUtils.fromInternalName(superName),
				TypeUtils.fromInternalNames(interfaces), (String) null);
	}

	public void visitEnd() {
		this.end_class();
	}

	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		this.declare_field(access, name, Type.getType(desc), value);
		return null;
	}

	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		return this.begin_method(access, new Signature(name, desc), TypeUtils.fromInternalNames(exceptions));
	}
}