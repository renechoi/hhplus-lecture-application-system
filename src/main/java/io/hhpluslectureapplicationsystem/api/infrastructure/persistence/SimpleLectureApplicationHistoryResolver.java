package io.hhpluslectureapplicationsystem.api.infrastructure.persistence;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplicationHistoryInfo;
import io.hhpluslectureapplicationsystem.api.business.model.entity.Lecture;
import io.hhpluslectureapplicationsystem.api.business.model.entity.LectureApplication;
import io.hhpluslectureapplicationsystem.api.business.model.entity.LectureApplicationHistory;
import io.hhpluslectureapplicationsystem.api.business.persistence.LectureApplicationHistoryResolver;
import io.hhpluslectureapplicationsystem.api.business.persistence.LectureApplicationRepository;
import io.hhpluslectureapplicationsystem.api.business.persistence.LectureApplyHistoryRepository;
import io.hhpluslectureapplicationsystem.api.business.persistence.LectureRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/24
 */
@Component
@RequiredArgsConstructor
public class SimpleLectureApplicationHistoryResolver implements LectureApplicationHistoryResolver {
	private final LectureApplyHistoryRepository lectureApplyHistoryRepository;

	private final LectureRepository lectureRepository;
	private final LectureApplicationRepository lectureApplicationRepository;

	@Override
	public List<LectureApplicationHistoryInfo> getApplicationHistoriesByUserId(String userId) {
		List<LectureApplicationHistory> histories = lectureApplyHistoryRepository.findByUserId(userId);

		return histories.stream()
			.filter(LectureApplicationHistory::isSuccess)
			.map(history -> {
			Lecture lecture = lectureRepository.findByLectureExternalId(history.getLectureExternalId()).orElse(null);
			LectureApplication lectureApplication = lectureApplicationRepository.findById(history.getLectureApplicationId()).orElse(null);
			return LectureApplicationHistoryInfo.from(history, lecture, lectureApplication);
		}).collect(Collectors.toList());
	}
}
