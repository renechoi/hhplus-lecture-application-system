package io.hhpluslectureapplicationsystem.api.application.dto;

import java.time.LocalDateTime;

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplicationHistoryInfo;
import io.hhpluslectureapplicationsystem.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/06/24
 */
public record LectureApplicationHistoryResponse(

	Long id,
	Long version,

	String userId,
	LectureGeneralResponse lecture,
	LectureApplicationResponse lectureApplication,
	boolean success,
	LocalDateTime requestAt,
	LocalDateTime appliedAt
	) {
	public static LectureApplicationHistoryResponse from(LectureApplicationHistoryInfo history) {
		return ObjectMapperBasedVoMapper.convert(history, LectureApplicationHistoryResponse.class);
	}

	public boolean isSameTitle(String lectureTitle) {
		return this.lecture !=null && this.lecture.isSameTitle(lectureTitle);
	}
}
