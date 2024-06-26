package io.hhpluslectureapplicationsystem.api.application.dto;

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplicationStatusInfo;
import io.hhpluslectureapplicationsystem.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public record LectureApplicationStatusResponse(
	boolean applied
) {
	public static LectureApplicationStatusResponse from(LectureApplicationStatusInfo info) {
		return ObjectMapperBasedVoMapper.convert(info, LectureApplicationStatusResponse.class);
	}
}
