package io.hhpluslectureapplicationsystem.api.business.service.impl;

import static java.time.LocalDateTime.*;
import static java.util.UUID.*;

import java.util.List;

import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplicationHistoryInfo;
import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplicationStatusCommand;
import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplicationStatusInfo;
import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplyCommand;
import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplyInfo;
import io.hhpluslectureapplicationsystem.api.business.model.entity.Lecture;
import io.hhpluslectureapplicationsystem.api.business.model.entity.LectureApplication;
import io.hhpluslectureapplicationsystem.api.business.operators.pkgenerator.LectureApplicationPkGenerator;
import io.hhpluslectureapplicationsystem.api.business.persistence.LectureApplicationHistoryResolver;
import io.hhpluslectureapplicationsystem.api.business.persistence.LectureApplicationRepository;
import io.hhpluslectureapplicationsystem.api.business.persistence.LectureRepository;
import io.hhpluslectureapplicationsystem.api.business.service.LectureApplyService;
import io.hhpluslectureapplicationsystem.api.business.validation.validators.Validator;
import io.hhpluslectureapplicationsystem.common.annotation.LogLectureApplyTry;
import io.hhpluslectureapplicationsystem.common.exception.LectureNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
@Service
@RequiredArgsConstructor
public class SimpleLectureApplyService implements LectureApplyService {

	private final LectureRepository lectureRepository;
	private final LectureApplicationRepository lectureApplicationRepository;
	private final LectureApplicationPkGenerator lectureApplicationPkGenerator;
	private final LectureApplicationHistoryResolver historyResolver;
	private final Validator<LectureApplyCommand, Lecture> lectureApplyValidator;


	@Override
	@LogLectureApplyTry
	@Transactional
	public LectureApplyInfo applyForLecture(LectureApplyCommand command) {
		Lecture lecture = lectureRepository.findByLectureExternalIdWithLock(command.lectureExternalId()).orElseThrow(LectureNotFoundException::new);

		lectureApplyValidator.validate(command, lecture);

		LectureApplication lectureApplication = command.toEntity()
			.withPk(lectureApplicationPkGenerator.generate(lecture.getTitle()))
			.withSk(randomUUID().toString().substring(0,12))
			.withLecture(lecture)
			.withAppliedAt(now())
			.asApplied()
			.publish();

		lecture.incrementRegisteredCount();

		return LectureApplyInfo.from(lectureApplicationRepository.save(lectureApplication));
	}

	@Override
	@Transactional(readOnly = true)
	public LectureApplicationStatusInfo checkSingleApplicationStatus(LectureApplicationStatusCommand command) {

		boolean userApplied = lectureApplicationRepository.existsByUserIdAndLectureExternalId(command.userId(),command.lectureExternalId());
		return new LectureApplicationStatusInfo(userApplied);
	}

	@Override
	@Transactional(readOnly = true)
	public List<LectureApplicationHistoryInfo> getAllApplicationHistoriesByUserId(String userId) {
		return historyResolver.getApplicationHistoriesByUserId(userId);
	}
}
