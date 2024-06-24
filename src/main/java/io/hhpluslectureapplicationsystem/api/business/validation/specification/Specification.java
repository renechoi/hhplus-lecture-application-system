package io.hhpluslectureapplicationsystem.api.business.validation.specification;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public interface Specification<T, E> {
	boolean isSatisfiedBy(T t, E e);

	default boolean isNotSatisfiedBy(T t, E e) {
		return !isSatisfiedBy(t, e);
	}
}