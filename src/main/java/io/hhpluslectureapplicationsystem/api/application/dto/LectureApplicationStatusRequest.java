package io.hhpluslectureapplicationsystem.api.application.dto;

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplicationStatusCommand;
import io.hhpluslectureapplicationsystem.common.mapper.ObjectMapperBasedVoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Builder
public class LectureApplicationStatusRequest {
	private String userId;

	@NotBlank(message = "강의 대체키 ID는 필수 항목입니다")
	private String lectureExternalId;

	public LectureApplicationStatusRequest withUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public LectureApplicationStatusCommand toCommand() {
		return ObjectMapperBasedVoMapper.convert(this, LectureApplicationStatusCommand.class);
	}
}