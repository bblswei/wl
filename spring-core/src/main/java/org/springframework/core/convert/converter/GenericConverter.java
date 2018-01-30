package org.springframework.core.convert.converter;

import java.util.Set;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.Assert;

public interface GenericConverter {

	/**
	 * Return the source and target types that this converter can convert between.
	 * <p>Each entry is a convertible source-to-target type pair.
	 * <p>For {@link ConditionalConverter conditional converters} this method may return
	 * {@code null} to indicate all source-to-target pairs should be considered.
	 */
	Set<ConvertiblePair> getConvertibleTypes();

	/**
	 * Convert the source object to the targetType described by the {@code TypeDescriptor}.
	 * @param source the source object to convert (may be {@code null})
	 * @param sourceType the type descriptor of the field we are converting from
	 * @param targetType the type descriptor of the field we are converting to
	 * @return the converted object
	 */
	Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType);


	/**
	 * Holder for a source-to-target class pair.
	 */
	final class ConvertiblePair {

		private final Class<?> sourceType;

		private final Class<?> targetType;

		/**
		 * Create a new source-to-target pair.
		 * @param sourceType the source type
		 * @param targetType the target type
		 */
		public ConvertiblePair(Class<?> sourceType, Class<?> targetType) {
			Assert.notNull(sourceType, "Source type must not be null");
			Assert.notNull(targetType, "Target type must not be null");
			this.sourceType = sourceType;
			this.targetType = targetType;
		}

		public Class<?> getSourceType() {
			return this.sourceType;
		}

		public Class<?> getTargetType() {
			return this.targetType;
		}

		@Override
		public boolean equals(Object other) {
			if (this == other) {
				return true;
			}
			if (other == null || other.getClass() != ConvertiblePair.class) {
				return false;
			}
			ConvertiblePair otherPair = (ConvertiblePair) other;
			return (this.sourceType == otherPair.sourceType && this.targetType == otherPair.targetType);
		}

		@Override
		public int hashCode() {
			return (this.sourceType.hashCode() * 31 + this.targetType.hashCode());
		}

		@Override
		public String toString() {
			return (this.sourceType.getName() + " -> " + this.targetType.getName());
		}
	}

}