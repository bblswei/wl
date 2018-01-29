package org.springframework.core.annotation;

import org.springframework.core.NestedRuntimeException;

@SuppressWarnings("serial")
public class AnnotationConfigurationException extends NestedRuntimeException {

	/**
	 * Construct a new {@code AnnotationConfigurationException} with the
	 * supplied message.
	 * @param message the detail message
	 */
	public AnnotationConfigurationException(String message) {
		super(message);
	}

	/**
	 * Construct a new {@code AnnotationConfigurationException} with the
	 * supplied message and cause.
	 * @param message the detail message
	 * @param cause the root cause
	 */
	public AnnotationConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

}