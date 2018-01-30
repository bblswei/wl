package org.springframework.core.env;

import java.util.LinkedHashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class MissingRequiredPropertiesException extends IllegalStateException {

	private final Set<String> missingRequiredProperties = new LinkedHashSet<String>();


	void addMissingRequiredProperty(String key) {
		this.missingRequiredProperties.add(key);
	}

	@Override
	public String getMessage() {
		return "The following properties were declared as required but could not be resolved: " +
				getMissingRequiredProperties();
	}

	/**
	 * Return the set of properties marked as required but not present
	 * upon validation.
	 * @see ConfigurablePropertyResolver#setRequiredProperties(String...)
	 * @see ConfigurablePropertyResolver#validateRequiredProperties()
	 */
	public Set<String> getMissingRequiredProperties() {
		return this.missingRequiredProperties;
	}

}