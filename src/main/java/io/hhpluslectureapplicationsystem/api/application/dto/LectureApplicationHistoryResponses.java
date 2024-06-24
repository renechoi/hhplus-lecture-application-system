package io.hhpluslectureapplicationsystem.api.application.dto;

import java.util.List;
import java.util.stream.Collectors;

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplicationHistoryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureApplicationHistoryResponses {
	private List<LectureApplicationHistoryResponse> histories;

	public static LectureApplicationHistoryResponses from(List<LectureApplicationHistoryInfo> infos) {
		List<LectureApplicationHistoryResponse> responses = infos.stream()
			.map(LectureApplicationHistoryResponse::from)
			.collect(Collectors.toList());
		return new LectureApplicationHistoryResponses(responses);
	}


}