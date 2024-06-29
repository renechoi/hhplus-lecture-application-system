package io.hhpluslectureapplicationsystem.api.business.model.dto;

import java.time.LocalDateTime;

import io.hhpluslectureapplicationsystem.api.business.model.entity.LectureApplication;
import io.hhpluslectureapplicationsystem.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public record LectureApplyCommand(
	String userId,

	String lectureExternalId,
	LocalDateTime requestAt
) {
	public LectureApplication toEntity() {
		return ObjectMapperBasedVoMapper.convert(this, LectureApplication.class);

	}
}
