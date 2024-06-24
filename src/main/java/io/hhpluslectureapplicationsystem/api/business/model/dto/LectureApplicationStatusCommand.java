package io.hhpluslectureapplicationsystem.api.business.model.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public record LectureApplicationStatusCommand(
	String userId,

	String lectureExternalId
) {
}
