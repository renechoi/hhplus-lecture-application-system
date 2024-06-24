package io.hhpluslectureapplicationsystem.cucumber.steps;

import static io.hhpluslectureapplicationsystem.cucumber.contextholder.LectureApplyContextHolder.*;
import static io.hhpluslectureapplicationsystem.cucumber.contextholder.LectureSearchContextHolder.*;
import static io.hhpluslectureapplicationsystem.cucumber.utils.fieldmatcher.ResponseMatcher.*;
import static io.hhpluslectureapplicationsystem.testhelpers.apiexecutor.LectureApplyApiExecutor.*;
import static io.hhpluslectureapplicationsystem.testhelpers.apiexecutor.LectureCrudApiExecutor.*;
import static io.hhpluslectureapplicationsystem.testhelpers.parser.LectureApplyResponseParser.*;
import static io.hhpluslectureapplicationsystem.testhelpers.parser.LectureSearchResponseParser.*;
import static java.time.LocalDateTime.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplicationStatusRequest;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplicationStatusResponse;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplicationHistoryResponses;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplyRequest;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureGeneralResponse;
import io.hhpluslectureapplicationsystem.cucumber.contextholder.LectureSearchContextHolder;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public class LectureApplyApiStepDef implements En {
	public LectureApplyApiStepDef() {
		LectureSearchContextHolder.initFields();

		Given("특강 신청 페이지에 접속하여 현재 제공되는 특강 목록을 조회한다", this::retrieveAllLecturesAsList);
		When("{string} 아이디로 {string} 특강 신청을 요청하고 성공 응답을 받는다", this::requestLectureApplicationWithSuccessResponse);
		Then("특강 신청 완료 여부를 조회하면 신청 성공 응답을 받고 응답은 다음과 같은 내용을 포함한다.", this::checkApplicationStatusWithSuccessResponse);
		Then("{string} 아이디의 {string} 특강 신청 이력이 생성되었는지 확인한다", this::verifyLectureApplyHistoryIsProperlyCreated);
	}

	private void retrieveAllLecturesAsList() {
		putSearchResponses(parseSearchResponses(listSearchLecturesWithOk()));
	}

	private void requestLectureApplicationWithSuccessResponse(String userId, String lectureTitle) {
		LectureGeneralResponse lecture = getSearchResponseByTitleFromResponses(lectureTitle);
		if (lecture == null) {
			throw new RuntimeException("test 실패! lectureTitle not found" );
		}

		LectureApplyRequest request = LectureApplyRequest.of(userId, lecture.lectureExternalId(), now());

		putLectureApplyResponse(parseLectureApplyResponse(applyForLectureWithOk(request)));
		putLectureApplyRequest(request);
	}
	private void checkApplicationStatusWithSuccessResponse(DataTable dataTable) {
		LectureApplyRequest recentRequest = getMostRecentLectureApplyRequest();
		LectureApplicationStatusRequest statusRequest = LectureApplicationStatusRequest.of(recentRequest.getUserId(), recentRequest.getLectureExternalId());

		LectureApplicationStatusResponse statusResponse = parseLectureApplicationStatusResponse(checkSingleApplicationStatusWithOk(recentRequest.getUserId(), statusRequest));

		putLectureApplicationStatusResponse(statusResponse);

		Map<String, String> expectedData = dataTable.asMaps().get(0);
		assertTrue(matchResponse(expectedData, statusResponse), "status 필드가 정확하지 않습니다.");
	}


	private void verifyLectureApplyHistoryIsProperlyCreated(String userId, String lectureTitle) {
		LectureGeneralResponse lecture = getSearchResponseByTitleFromResponses(lectureTitle);
		if (lecture == null) {
			throw new RuntimeException("test 실패! lectureTitle not found");
		}

		LectureApplicationHistoryResponses historyResponses = parseLectureApplyHistoryResponses(getAllApplicationHistoriesByUserId(userId));
		putLectureApplyHistoryResponses(userId, historyResponses);

		boolean historyExists = historyResponses.getHistories().stream()
			.anyMatch(history -> history.isSameTitle(lectureTitle));

		assertTrue(historyExists, "특강 신청 이력이 생성되지 않았습니다.");
	}


}
