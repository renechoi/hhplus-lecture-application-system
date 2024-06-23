package io.hhpluslectureapplicationsystem.api.business.model.dto;

import java.time.LocalDateTime;
import java.util.Optional;

import io.hhpluslectureapplicationsystem.api.business.model.entity.Lecture;
import io.hhpluslectureapplicationsystem.api.business.model.entity.LectureStatus;
import io.hhpluslectureapplicationsystem.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public record LectureGeneralInfo(
	String lectureId,
	Long version,
	String title,
	String description,
	LocalDateTime startTime,
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
