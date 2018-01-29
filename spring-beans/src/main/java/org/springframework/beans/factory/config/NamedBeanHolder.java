package org.springframework.beans.factory.config;

import org.springframework.beans.factory.NamedBean;
import org.springframework.util.Assert;

public class NamedBeanHolder<T> implements NamedBean {

	private final String beanName;

	private final T beanInstance;


	/**
	 * Create a new holder for the given bean name plus instance.
	 * @param beanName the name of the bean
	 * @param beanInstance the corresponding bean instance
	 */
	public NamedBeanHolder(String beanName, T beanInstance) {
		Assert.notNull(beanName, "Bean name must not be null");
		this.beanName = beanName;
		this.beanInstance = beanInstance;
	}


	/**
	 * Return the name of the bean (never {@code null}).
	 */
	@Override
	public String getBeanName() {
		return this.beanName;
	}

	/**
	 * Return the corresponding bean instance (can be {@code null}).
	 */
	public T getBeanInstance() {
		return this.beanInstance;
	}

}