package io.hhpluslectureapplicationsystem.api.business.model.dto;

import java.time.LocalDateTime;

import io.hhpluslectureapplicationsystem.api.business.model.entity.Lecture;
import io.hhpluslectureapplicationsystem.api.business.model.entity.LectureStatus;
import io.hhpluslectureapplicationsystem.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public record LectureGeneralInfo(
	String lectureExternalId,
	Long version,
	String title,
	String description,
	LocalDateTime applicationOpenTime,
	LocalDateTime applicationCloseTime,
	LocalDateTime lectureStartTime,
	int durationMinutes,
	int capacity,
	String location,
	String instructor,
	LectureStatus status
) {
	public static LectureGeneralInfo from(Lecture lecture) {
		return ObjectMapperBasedVoMapper.convert(lecture, LectureGeneralInfo.class);
	}
}
