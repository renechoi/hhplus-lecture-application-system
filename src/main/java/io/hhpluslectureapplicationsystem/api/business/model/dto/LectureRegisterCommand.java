package io.hhpluslectureapplicationsystem.api.business.model.dto;

import static io.hhpluslectureapplicationsystem.common.mapper.LectureEntityMapper.*;

import java.time.LocalDateTime;

import io.hhpluslectureapplicationsystem.api.business.model.entity.Lecture;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public record LectureRegisterCommand(
	String title,
	String description,
	LocalDateTime startTime,
	int durationMinutes,
	int capacity,
	String location,
	String instructor
) {

	public Lecture toEntity(String id) {
		return LECTURE_ENTITY_MAPPER.toEntity(this, id)
			.withPk(id)
			.determineStatus(startTime, durationMinutes);
	}
}
