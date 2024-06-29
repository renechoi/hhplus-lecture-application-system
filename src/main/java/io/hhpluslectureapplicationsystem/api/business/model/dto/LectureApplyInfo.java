package io.hhpluslectureapplicationsystem.api.business.model.dto;

import java.time.LocalDateTime;

import io.hhpluslectureapplicationsystem.api.business.model.entity.LectureApplication;
import io.hhpluslectureapplicationsystem.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public record LectureApplyInfo(
	String lectureApplicationExternalId,
	LectureGeneralInfo lecture,
	String userId,
	boolean applied,
	LocalDateTime appliedAt

) {
	public static LectureApplyInfo from(LectureApplication lectureApplication) {
		return ObjectMapperBasedVoMapper.convert(lectureApplication, LectureApplyInfo.class);
	}
}
