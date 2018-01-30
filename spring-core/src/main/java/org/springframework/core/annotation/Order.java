package org.springframework.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.core.Ordered;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface Order {

	/**
	 * The order value.
	 * <p>Default is {@link Ordered#LOWEST_PRECEDENCE}.
	 * @see Ordered#getOrder()
	 */
	int value() default Ordered.LOWEST_PRECEDENCE;

}
