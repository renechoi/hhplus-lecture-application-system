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
	public static LectureGeneralResponse from(LectureGeneralInfo info) {
		return ObjectMapperBasedVoMapper.convert(info, LectureGeneralResponse.class);
	}

	public boolean isSameTitle(String lectureTitle) {
		return lectureTitle.equals(this.title);
	}
}
