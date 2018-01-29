package org.springframework.beans;

import org.springframework.beans.factory.config.BeanMetadataElement;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.core.AttributeAccessor;

public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement {

	String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

	String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;

	int ROLE_APPLICATION = 0;

	int ROLE_SUPPORT = 1;

	int ROLE_INFRASTRUCTURE = 2;

	void setParentName(String parentName);

	String getParentName();

	void setBeanClassName(String beanClassName);

	String getBeanClassName();

	void setScope(String scope);
	
	String getScope();

	void setLazyInit(boolean lazyInit);
	
	boolean isLazyInit();

	void setDependsOn(String... dependsOn);

	String[] getDependsOn();

	void setAutowireCandidate(boolean autowireCandidate);

	void setPrimary(boolean primary);

	boolean isPrimary();

	void setFactoryBeanName(String factoryBeanName);

	String getFactoryBeanName();

	void setFactoryMethodName(String factoryMethodName);

	String getFactoryMethodName();

	ConstructorArgumentValues getConstructorArgumentValues();

	MutablePropertyValues getPropertyValues();

	boolean isSingleton();

	boolean isPrototype();

	boolean isAbstract();

	int getRole();

	String getDescription();

	String getResourceDescription();

	BeanDefinition getOriginatingBeanDefinition();

}