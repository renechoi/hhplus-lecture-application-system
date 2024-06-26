package io.hhpluslectureapplicationsystem.api.business.validation.specification;

import org.springframework.stereotype.Component;

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplyCommand;
import io.hhpluslectureapplicationsystem.api.business.model.entity.Lecture;
import io.hhpluslectureapplicationsystem.api.business.persistence.LectureApplicationRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
@Component
@RequiredArgsConstructor
public class LectureCapacitySpecification implements Specification<LectureApplyCommand, Lecture> {

	private final LectureApplicationRepository applicationRepository;

	@Override
	public boolean isSatisfiedBy(LectureApplyCommand command, Lecture lecture) {
		// return applicationRepository.countByLecture(lecture) < lecture.getCapacity();
		return !lecture.isCapacityExceeded();
	}
}