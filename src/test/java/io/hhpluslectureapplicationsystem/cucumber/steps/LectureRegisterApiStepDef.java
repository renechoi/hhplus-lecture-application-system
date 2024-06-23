package io.hhpluslectureapplicationsystem.cucumber.steps;

import static io.hhpluslectureapplicationsystem.cucumber.contextholder.LectureRegisterContextHolder.*;
import static io.hhpluslectureapplicationsystem.cucumber.utils.fieldmatcher.ResponseMatcher.*;
import static io.hhpluslectureapplicationsystem.testhelpers.apiexecutor.LectureCrudApiExecutor.*;
import static io.hhpluslectureapplicationsystem.testhelpers.parser.LectureRegisterationResponseParser.*;
import static io.hhpluslectureapplicationsystem.util.CustomFieldMapper.*;
import static io.hhpluslectureapplicationsystem.util.FieldMapper.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureRegisterRequest;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureRegisterationResponse;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */

public class LectureRegisterApiStepDef implements En {

	public LectureRegisterApiStepDef() {
		initFields();
		Given("다음과 같은 특강 정보가 주어지고 등록을 요청하면 성공 응답을 받는다", this::givenLectureInfoWithDataTableAndRegisterWithSuccessResponse);
		And("등록된 특강의 정보를 조회하면 아래와 같은 정보가 확인되어야 한다", this::verifyLectureInfo);

		Given("다음과 같은 필수 필드가 누락된 특강 정보가 주어지고 등록을 요청하면 실패 응답을 받는다", this::givenLectureInfoWithMissingFieldsAndRegisterWithFailureResponse);



	}

	private void givenLectureInfoWithDataTableAndRegisterWithSuccessResponse(DataTable dataTable) {
		Map<String, String> lectureInfoMap = dataTable.asMaps().get(0);

		LectureRegisterRequest lectureRegisterRequest = applyCustomMappings(updateFields(new LectureRegisterRequest(), lectureInfoMap), lectureInfoMap);

		putLectureRegisterationResponse(parseLectureRegisterationResponse(registerLectureWithCreated(lectureRegisterRequest)));
		putLectureRegisterRequest(lectureRegisterRequest);
	}

	private void verifyLectureInfo(DataTable dataTable) {
		List<Map<String, String>> expectedLectures = dataTable.asMaps(String.class, String.class);
		LectureRegisterationResponse actualResponse = getMostRecentLectureRegisterationResponse();
		assertNotNull(actualResponse, "등록된 특강의 응답이 존재하지 않습니다.");

		boolean matchFound = expectedLectures.stream().anyMatch(expectedLecture -> matchResponse(expectedLecture, parseLectureRegisterationResponse(searchLectureWithOk(actualResponse.lectureId()))));

		assertTrue(matchFound, "기대한 특강 정보가 일치하지 않습니다.");
	}


	private void givenLectureInfoWithMissingFieldsAndRegisterWithFailureResponse(DataTable dataTable) {
		Map<String, String> lectureInfoMap = dataTable.asMaps().get(0);

		LectureRegisterRequest lectureRegisterRequest = applyCustomMappings(updateFields(new LectureRegisterRequest(), lectureInfoMap), lectureInfoMap);

		assertEquals(400, registerLecture(lectureRegisterRequest).statusCode());
	}





}