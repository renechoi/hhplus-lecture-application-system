package io.hhpluslectureapplicationsystem.testhelpers.parser;

import io.hhpluslectureapplicationsystem.api.application.dto.LectureRegisterationResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public class LectureRegisterationResponseParser {
	public static LectureRegisterationResponse parseLectureRegisterationResponse(ExtractableResponse<Response> response) {
		return response.as(LectureRegisterationResponse.class);
	}
}