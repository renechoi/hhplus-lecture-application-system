package io.hhpluslectureapplicationsystem.testhelpers.parser;

import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplicationStatusResponse;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplicationHistoryResponses;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplyResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public class LectureApplyResponseParser {
	public static LectureApplyResponse parseLectureApplyResponse(ExtractableResponse<Response> response) {
		return response.as(LectureApplyResponse.class);
	}

	public static LectureApplicationStatusResponse parseLectureApplicationStatusResponse(ExtractableResponse<Response> response) {
		return response.as(LectureApplicationStatusResponse.class);
	}


	public static LectureApplicationHistoryResponses parseLectureApplyHistoryResponses(ExtractableResponse<Response> response) {
		return response.as(LectureApplicationHistoryResponses.class);
	}


}