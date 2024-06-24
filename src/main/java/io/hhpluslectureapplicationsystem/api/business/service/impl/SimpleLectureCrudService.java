package io.hhpluslectureapplicationsystem.api.business.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureGeneralInfo;
import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureRegisterCommand;
import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureRegisterInfo;
import io.hhpluslectureapplicationsystem.api.business.operators.pkgenerator.LecturePkGenerator;
import io.hhpluslectureapplicationsystem.api.business.service.LectureCrudService;
import io.hhpluslectureapplicationsystem.api.business.persistence.LectureRepository;
import io.hhpluslectureapplicationsystem.common.exception.LectureNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
@Service
@RequiredArgsConstructor
public class SimpleLectureCrudService implements LectureCrudService {

	private final LectureRepository lectureRepository;
	private final LecturePkGenerator lecturePkGenerator;

	@Override
	@Transactional
	public LectureRegisterInfo register(LectureRegisterCommand command) {
		return LectureRegisterInfo.from(lectureRepository.save(command.toEntity(lecturePkGenerator.generate(command.title()))));
	}

	@Override
	@Transactional(readOnly = true)
	public LectureGeneralInfo searchSingleLectureById(String externalId) {
		return LectureGeneralInfo.from(lectureRepository.findByLectureExternalId(externalId).orElseThrow(LectureNotFoundException::new));
	}

	@Override
	@Transactional(readOnly = true)
	public List<LectureGeneralInfo> listSearchLectures() {
		return lectureRepository.findAll().stream()
			.map(LectureGeneralInfo::from)
			.collect(Collectors.toList());
	}

}
