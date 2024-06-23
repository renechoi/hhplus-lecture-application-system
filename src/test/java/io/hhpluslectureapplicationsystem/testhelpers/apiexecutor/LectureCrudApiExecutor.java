package io.hhpluslectureapplicationsystem.testhelpers.apiexecutor;

import static io.restassured.RestAssured.*;
import static org.springframework.http.MediaType.*;

import io.hhpluslectureapplicationsystem.api.application.dto.LectureRegisterRequest;
import io.hhpluslectureapplicationsystem.util.YmlLoader;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public class LectureCrudApiExecutor extends AbstractRequestExecutor {
	private static final String CONTEXT_PATH = YmlLoader.ymlLoader().getContextPath();
	private static final String URL_PATH = CONTEXT_PATH + "/lectures";

	private static RequestSpecification getRequestSpecification(int port) {
		return given().log().all().port(port).contentType(APPLICATION_JSON_VALUE);
	}

	public static ExtractableResponse<Response> registerLecture(LectureRegisterRequest request) {
		return doPost(getRequestSpecification(DynamicPortHolder.getPort()), URL_PATH, request);
	}

	public static ExtractableResponse<Response> registerLectureWithCreated(LectureRegisterRequest request) {
		return doPostWithCreated(getRequestSpecification(DynamicPortHolder.getPort()), URL_PATH, request);
	}

	public static ExtractableResponse<Response> searchLecture(String lectureId) {
		return doGet(getRequestSpecification(DynamicPortHolder.getPort()), URL_PATH + "/" + lectureId);
	}

	public static ExtractableResponse<Response> searchLectureWithOk(String lectureId) {
		return doGetWithOk(getRequestSpecification(DynamicPortHolder.getPort()), URL_PATH + "/" + lectureId);
	}
}
