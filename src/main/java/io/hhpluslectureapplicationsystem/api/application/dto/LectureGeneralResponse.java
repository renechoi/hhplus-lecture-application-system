package io.hhpluslectureapplicationsystem.api.application.dto;

import java.time.LocalDateTime;

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureGeneralInfo;
import io.hhpluslectureapplicationsystem.api.business.model.entity.LectureStatus;
import io.hhpluslectureapplicationsystem.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public record LectureGeneralResponse(
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
	public static LectureGeneralResponse from(LectureGeneralInfo info) {
		return ObjectMapperBasedVoMapper.convert(info, LectureGeneralResponse.class);
	}
}
