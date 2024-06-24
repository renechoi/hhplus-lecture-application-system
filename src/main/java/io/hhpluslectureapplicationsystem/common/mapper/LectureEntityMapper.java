package io.hhpluslectureapplicationsystem.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureRegisterCommand;
import io.hhpluslectureapplicationsystem.api.business.model.entity.Lecture;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */

@Mapper
public interface LectureEntityMapper {
	LectureEntityMapper LECTURE_ENTITY_MAPPER = Mappers.getMapper(LectureEntityMapper.class);

	@Mapping(source = "id", target = "lectureId")
	Lecture toEntity(LectureRegisterCommand command, String id);

}