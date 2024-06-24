package io.hhpluslectureapplicationsystem.api.business.model.dto;

import java.time.LocalDateTime;

import io.hhpluslectureapplicationsystem.api.business.model.entity.Lecture;
import io.hhpluslectureapplicationsystem.api.business.model.entity.LectureApplication;
import io.hhpluslectureapplicationsystem.api.business.model.entity.LectureApplicationHistory;
import lombok.Builder;

/**
 * @author : Rene Choi
 * @since : 2024/06/24
 */
@Builder
public record LectureApplicationHistoryInfo(
	Long id,
	Long version,
	String userId,
	LectureGeneralInfo lecture,
	LectureApplicationInfo lectureApplication,
	boolean success,
	LocalDateTime requestAt,
	LocalDateTime appliedAt
) {
	public static LectureApplicationHistoryInfo from(LectureApplicationHistory history, Lecture lecture, LectureApplication lectureApplication) {
		return LectureApplicationHistoryInfo.builder()
			.id(history.getId())
			.version(history.getVersion())
			.userId(history.getUserId())
			.lecture(LectureGeneralInfo.from(lecture))
			.lectureApplication(LectureApplicationInfo.from(lectureApplication))
			.success(history.isSuccess())
			.appliedAt(history.getAppliedAt())
			.build();
	}
}
