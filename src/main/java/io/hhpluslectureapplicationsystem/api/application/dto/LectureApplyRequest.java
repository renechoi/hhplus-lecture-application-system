package io.hhpluslectureapplicationsystem.api.application.dto;

import java.time.LocalDateTime;

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplyCommand;
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
public class LectureApplyRequest {
	@NotBlank(message = "사용자 ID는 필수 항목입니다")
	private String userId;

	@NotBlank(message = "강의 대체키 ID는 필수 항목입니다")
	private String lectureExternalId;

	private LocalDateTime requestAt;

	public LectureApplyCommand toCommand() {
		return ObjectMapperBasedVoMapper.convert(this, LectureApplyCommand.class);
	}
}

