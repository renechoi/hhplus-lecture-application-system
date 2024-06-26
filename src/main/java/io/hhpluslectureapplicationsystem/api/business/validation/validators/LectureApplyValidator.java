package io.hhpluslectureapplicationsystem.api.business.validation.validators;

import static io.hhpluslectureapplicationsystem.common.model.GlobalResponseCode.*;

import org.springframework.stereotype.Component;

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplyCommand;
import io.hhpluslectureapplicationsystem.api.business.model.entity.Lecture;
import io.hhpluslectureapplicationsystem.api.business.validation.specification.LectureCapacitySpecification;
import io.hhpluslectureapplicationsystem.api.business.validation.specification.UniqueUserApplicationSpecification;
import io.hhpluslectureapplicationsystem.common.exception.LectureNotApplicableException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
@Component
@RequiredArgsConstructor
public class LectureApplyValidator implements Validator<LectureApplyCommand, Lecture> {

	private final UniqueUserApplicationSpecification uniqueUserApplicationSpecification;
	private final LectureCapacitySpecification capacitySpecification;

	@Override
	public void validate(LectureApplyCommand command, Lecture lecture) {
		if (uniqueUserApplicationSpecification.isNotSatisfiedBy(command, lecture)) {
			throw new LectureNotApplicableException(DUPLICATE_APPLICATION);
		}
		if (capacitySpecification.isNotSatisfiedBy(command, lecture)) {
			throw new LectureNotApplicableException(LECTURE_FULL);
		}
	}
}