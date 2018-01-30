package org.springframework.core.annotation;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.core.DecoratingProxy;
import org.springframework.core.OrderComparator;

public class AnnotationAwareOrderComparator extends OrderComparator {

	/**
	 * Shared default instance of {@code AnnotationAwareOrderComparator}.
	 */
	public static final AnnotationAwareOrderComparator INSTANCE = new AnnotationAwareOrderComparator();


	/**
	 * This implementation checks for {@link Order @Order} or
	 * {@link javax.annotation.Priority @Priority} on various kinds of
	 * elements, in addition to the {@link org.springframework.core.Ordered}
	 * check in the superclass.
	 */
	protected Integer findOrder(Object obj) {
		// Check for regular Ordered interface
		Integer order = super.findOrder(obj);
		if (order != null) {
			return order;
		}

		// Check for @Order and @Priority on various kinds of elements
		if (obj instanceof Class) {
			return OrderUtils.getOrder((Class<?>) obj);
		}
		else if (obj instanceof Method) {
			Order ann = AnnotationUtils.findAnnotation((Method) obj, Order.class);
			if (ann != null) {
				return ann.value();
			}
		}
		else if (obj instanceof AnnotatedElement) {
			Order ann = AnnotationUtils.getAnnotation((AnnotatedElement) obj, Order.class);
			if (ann != null) {
				return ann.value();
			}
		}
		else if (obj != null) {
			order = OrderUtils.getOrder(obj.getClass());
			if (order == null && obj instanceof DecoratingProxy) {
				order = OrderUtils.getOrder(((DecoratingProxy) obj).getDecoratedClass());
			}
		}

		return order;
	}

	/**
	 * This implementation retrieves an @{@link javax.annotation.Priority}
	 * value, allowing for additional semantics over the regular @{@link Order}
	 * annotation: typically, selecting one object over another in case of
	 * multiple matches but only one object to be returned.
	 */
	public Integer getPriority(Object obj) {
		Integer priority = null;
		if (obj instanceof Class) {
			priority = OrderUtils.getPriority((Class<?>) obj);
		}
		else if (obj != null) {
			priority = OrderUtils.getPriority(obj.getClass());
			if (priority == null && obj instanceof DecoratingProxy) {
				priority = OrderUtils.getOrder(((DecoratingProxy) obj).getDecoratedClass());
			}
		}
		return priority;
	}


	/**
	 * Sort the given List with a default AnnotationAwareOrderComparator.
	 * <p>Optimized to skip sorting for lists with size 0 or 1,
	 * in order to avoid unnecessary array extraction.
	 * @param list the List to sort
	 * @see java.util.Collections#sort(java.util.List, java.util.Comparator)
	 */
	public static void sort(List<?> list) {
		if (list.size() > 1) {
			Collections.sort(list, INSTANCE);
		}
	}

	/**
	 * Sort the given array with a default AnnotationAwareOrderComparator.
	 * <p>Optimized to skip sorting for lists with size 0 or 1,
	 * in order to avoid unnecessary array extraction.
	 * @param array the array to sort
	 * @see java.util.Arrays#sort(Object[], java.util.Comparator)
	 */
	public static void sort(Object[] array) {
		if (array.length > 1) {
			Arrays.sort(array, INSTANCE);
		}
	}

	/**
	 * Sort the given array or List with a default AnnotationAwareOrderComparator,
	 * if necessary. Simply skips sorting when given any other value.
	 * <p>Optimized to skip sorting for lists with size 0 or 1,
	 * in order to avoid unnecessary array extraction.
	 * @param value the array or List to sort
	 * @see java.util.Arrays#sort(Object[], java.util.Comparator)
	 */
	public static void sortIfNecessary(Object value) {
		if (value instanceof Object[]) {
			sort((Object[]) value);
		}
		else if (value instanceof List) {
			sort((List<?>) value);
		}
	}

}