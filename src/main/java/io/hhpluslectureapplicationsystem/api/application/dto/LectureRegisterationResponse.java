package io.hhpluslectureapplicationsystem.api.application.dto;

import java.time.LocalDateTime;

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureRegisterInfo;
import io.hhpluslectureapplicationsystem.api.business.model.entity.LectureStatus;
import io.hhpluslectureapplicationsystem.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public record LectureRegisterationResponse(
	String lectureId,
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
	public static LectureRegisterationResponse from(LectureRegisterInfo info) {
		return ObjectMapperBasedVoMapper.convert(info, LectureRegisterationResponse.class);
	}
}
