package io.hhpluslectureapplicationsystem.testhelpers.parser;

import io.hhpluslectureapplicationsystem.api.application.dto.LectureGeneralResponse;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureGeneralResponses;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public class LectureSearchResponseParser {
	public static LectureGeneralResponse parseSearchResponse(ExtractableResponse<Response> response) {
		return response.as(LectureGeneralResponse.class);
	}

	public static LectureGeneralResponses parseSearchResponses(ExtractableResponse<Response> response) {
		return response.as(LectureGeneralResponses.class);
	}

}
