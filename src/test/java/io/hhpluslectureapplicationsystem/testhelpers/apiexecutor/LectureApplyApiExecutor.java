package io.hhpluslectureapplicationsystem.testhelpers.apiexecutor;

import static io.restassured.RestAssured.*;
import static org.springframework.http.MediaType.*;

import java.util.Map;

import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplicationStatusRequest;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplyRequest;
import io.hhpluslectureapplicationsystem.util.YmlLoader;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public class LectureApplyApiExecutor extends AbstractRequestExecutor {
	private static final String CONTEXT_PATH = YmlLoader.ymlLoader().getContextPath();
	private static final String APPLY_URL_PATH = CONTEXT_PATH + "/lectures/apply";
	private static final String STATUS_URL_PATH = CONTEXT_PATH + "/lectures/application";
	private static final String HISTORY_URL_PATH = CONTEXT_PATH + "/lectures/applications";


	private static RequestSpecification getRequestSpecification(int port) {
		return given().log().all().port(port).contentType(APPLICATION_JSON_VALUE);
	}

	public static ExtractableResponse<Response> applyForLecture(LectureApplyRequest request) {
		return doPost(getRequestSpecification(DynamicPortHolder.getPort()), APPLY_URL_PATH, request);
	}

	public static ExtractableResponse<Response> applyForLectureWithOk(LectureApplyRequest request) {
		return doPostWithOk(getRequestSpecification(DynamicPortHolder.getPort()), APPLY_URL_PATH, request);
	}

	public static ExtractableResponse<Response> checkSingleApplicationStatus(String userId, LectureApplicationStatusRequest request) {
		return doGet(getRequestSpecification(DynamicPortHolder.getPort()).body(request), STATUS_URL_PATH + "/" + userId);
	}

	public static ExtractableResponse<Response> checkSingleApplicationStatusWithOk(String userId, LectureApplicationStatusRequest request) {
		return doGetWithOk(getRequestSpecification(DynamicPortHolder.getPort()).body(request), STATUS_URL_PATH + "/" + userId);
	}



	public static ExtractableResponse<Response> getAllApplicationHistoriesByUserId(String userId) {
		return doGetWithOk(getRequestSpecification(DynamicPortHolder.getPort()), HISTORY_URL_PATH + "/" + userId + "/history");
	}

}