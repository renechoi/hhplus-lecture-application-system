package io.hhpluslectureapplicationsystem.api.business.validation.specification;

import java.time.LocalDate;

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
public class UniqueUserApplicationSpecification implements Specification<LectureApplyCommand, Lecture> {

	private final LectureApplicationRepository applicationRepository;

	@Override
	public boolean isSatisfiedBy(LectureApplyCommand command, Lecture lecture) {
		LocalDate requestDate = command.requestAt().toLocalDate();
		return !applicationRepository.existsByUserIdAndLectureAndRequestDate(command.userId(), lecture, requestDate);
	}
}