package io.hhpluslectureapplicationsystem.api.application.dto;

import java.util.List;
import java.util.stream.Collectors;

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureGeneralInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LectureGeneralResponses {

	private List<LectureGeneralResponse> lectures;

	public static LectureGeneralResponses from(List<LectureGeneralInfo> infos) {
		List<LectureGeneralResponse> responses = infos.stream()
			.map(LectureGeneralResponse::from)
			.collect(Collectors.toList());
		return new LectureGeneralResponses(responses);
	}
}