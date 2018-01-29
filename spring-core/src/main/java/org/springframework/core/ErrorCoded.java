package org.springframework.core;

@Deprecated
public interface ErrorCoded {

	/**
	 * Return the error code associated with this failure.
	 * The GUI can render this any way it pleases, allowing for localization etc.
	 * @return a String error code associated with this failure,
	 * or {@code null} if not error-coded
	 */
	String getErrorCode();

}