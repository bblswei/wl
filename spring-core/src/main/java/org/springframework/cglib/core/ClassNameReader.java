package org.springframework.cglib.core;

import java.util.ArrayList;
import org.springframework.asm.ClassReader;
import org.springframework.asm.ClassVisitor;
import org.springframework.cglib.core.ClassNameReader.1;
import org.springframework.cglib.core.ClassNameReader.EarlyExitException;

public class ClassNameReader {
	private static final EarlyExitException EARLY_EXIT = new EarlyExitException((1)null);

	public static String getClassName(ClassReader r) {
		return getClassInfo(r)[0];
	}

	public static String[] getClassInfo(ClassReader r) {
      ArrayList array = new ArrayList();

      try {
         r.accept(new 1(327680, (ClassVisitor)null, array), 6);
      } catch (EarlyExitException arg2) {
         ;
      }

      return (String[])((String[])array.toArray(new String[0]));
   }
}