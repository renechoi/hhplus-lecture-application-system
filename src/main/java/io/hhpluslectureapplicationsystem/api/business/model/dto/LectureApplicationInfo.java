package io.hhpluslectureapplicationsystem.api.business.model.dto;

import java.time.LocalDateTime;

import io.hhpluslectureapplicationsystem.api.business.model.entity.Lecture;
import io.hhpluslectureapplicationsystem.api.business.model.entity.LectureApplication;
import io.hhpluslectureapplicationsystem.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/06/24
 */
public record LectureApplicationInfo(
	String lectureApplicationId,
	String lectureApplicationExternalId,
	Lecture lecture,
	String userId,
	boolean applied,
	LocalDateTime appliedAt
	) {
	public static LectureApplicationInfo from(LectureApplication lectureApplication) {
		return ObjectMapperBasedVoMapper.convert(lectureApplication, LectureApplicationInfo.class);
	}
}
