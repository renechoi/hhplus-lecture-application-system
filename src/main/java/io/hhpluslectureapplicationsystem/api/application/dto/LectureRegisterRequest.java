package io.hhpluslectureapplicationsystem.api.application.dto;

import java.time.LocalDateTime;


import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureRegisterCommand;
import io.hhpluslectureapplicationsystem.common.mapper.ObjectMapperBasedVoMapper;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LectureRegisterRequest {

	@NotBlank(message = "제목은 필수 항목입니다")
	private String title;

	@NotBlank(message = "설명은 필수 항목입니다")
	private String description;

	@NotNull(message = "신청 시작 시간은 필수 항목입니다")
	private LocalDateTime applicationOpenTime;

	@NotNull(message = "신청 종료 시간은 필수 항목입니다")
	private LocalDateTime applicationCloseTime;

	@NotNull(message = "강의 시작 시간은 필수 항목입니다")
	private LocalDateTime lectureStartTime;

	@Min(value = 1, message = "강의 시간은 최소 1분이어야 합니다")
	private int durationMinutes;

	@Min(value = 1, message = "수용 인원은 최소 1명이어야 합니다")
	private int capacity;

	@NotBlank(message = "위치는 필수 항목입니다")
	private String location;

	@NotBlank(message = "강사는 필수 항목입니다")
	private String instructor;

	public LectureRegisterCommand toCommand() {
		return ObjectMapperBasedVoMapper.convert(this, LectureRegisterCommand.class);
	}
}