package io.hhpluslectureapplicationsystem.api.application.dto;

import java.time.LocalDateTime;

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplyInfo;
import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureGeneralInfo;
import io.hhpluslectureapplicationsystem.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public record LectureApplyResponse(
	boolean success,
	String message,
	String lectureApplicationExternalId,
	LectureGeneralInfo lecture,
	String userId,
	boolean applied,
	LocalDateTime appliedAt
) {
	public static LectureApplyResponse from(LectureApplyInfo info) {
		return ObjectMapperBasedVoMapper.convert(info, LectureApplyResponse.class);
	}
}
