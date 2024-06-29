package io.hhpluslectureapplicationsystem.api.application.dto;

import java.time.LocalDateTime;

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplicationInfo;
import io.hhpluslectureapplicationsystem.api.business.model.entity.Lecture;
import io.hhpluslectureapplicationsystem.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/06/24
 */
public record LectureApplicationResponse(
	String lectureApplicationId,
	String lectureApplicationExternalId,
	Lecture lecture,
	String userId,
	boolean applied,
	LocalDateTime appliedAt
) {

	public static LectureApplicationResponse from(LectureApplicationInfo lectureApplication) {
		return ObjectMapperBasedVoMapper.convert(lectureApplication, LectureApplicationResponse.class);
	}
}
