package io.hhpluslectureapplicationsystem.api.business.validation.validators;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public interface Validator<T, E> {
	void validate(T t, E e);
}