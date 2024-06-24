package io.hhpluslectureapplicationsystem.api.business.validation.validators;

import org.springframework.stereotype.Component;

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplyCommand;
import io.hhpluslectureapplicationsystem.api.business.model.entity.Lecture;
import io.hhpluslectureapplicationsystem.api.business.validation.specification.LectureCapacitySpecification;
import io.hhpluslectureapplicationsystem.api.business.validation.specification.UniqueUserApplicationSpecification;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
@Component
@RequiredArgsConstructor
public class LectureApplyValidator implements Validator<LectureApplyCommand, Lecture> {

	private final LectureCapacitySpecification capacitySpecification;
	private final UniqueUserApplicationSpecification userApplicationSpecification;

	@Override
	public void validate(LectureApplyCommand command, Lecture lecture) {
		if (userApplicationSpecification.isNotSatisfiedBy(command, lecture)) {
			throw new IllegalArgumentException("이미 신청한 사용자입니다.");
		}
		if (capacitySpecification.isNotSatisfiedBy(command, lecture)) {
			throw new IllegalArgumentException("특강 신청이 마감되었습니다.");
		}
	}
}