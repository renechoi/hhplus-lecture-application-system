package io.hhpluslectureapplicationsystem.api.business.model.dto;

import io.hhpluslectureapplicationsystem.api.business.model.entity.LectureApplication;
import io.hhpluslectureapplicationsystem.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public record LectureApplicationStatusInfo(
	boolean applied
) {

	public static LectureApplicationStatusInfo from(LectureApplication lectureApplication) {
		return ObjectMapperBasedVoMapper.convert(lectureApplication, LectureApplicationStatusInfo.class);
	}
}
