package io.hhpluslectureapplicationsystem.cucumber.steps;

import static io.hhpluslectureapplicationsystem.cucumber.contextholder.LectureApplyContextHolder.*;
import static io.hhpluslectureapplicationsystem.cucumber.contextholder.LectureSearchContextHolder.*;
import static io.hhpluslectureapplicationsystem.cucumber.utils.fieldmatcher.ResponseMatcher.*;
import static io.hhpluslectureapplicationsystem.testhelpers.apiexecutor.LectureApplyApiExecutor.*;
import static io.hhpluslectureapplicationsystem.testhelpers.apiexecutor.LectureCrudApiExecutor.*;
import static io.hhpluslectureapplicationsystem.testhelpers.parser.LectureApplyResponseParser.*;
import static io.hhpluslectureapplicationsystem.testhelpers.parser.LectureSearchResponseParser.*;
import static io.hhpluslectureapplicationsystem.util.UrlEncodingHelper.*;
import static java.time.LocalDateTime.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplicationHistoryResponse;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplicationStatusRequest;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplicationStatusResponse;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplicationHistoryResponses;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplyRequest;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureGeneralResponse;
import io.hhpluslectureapplicationsystem.cucumber.contextholder.LectureApplyContextHolder;
import io.hhpluslectureapplicationsystem.cucumber.contextholder.LectureSearchContextHolder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.SneakyThrows;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public class LectureApplyApiStepDef implements En {
	public LectureApplyApiStepDef() {
		LectureSearchContextHolder.initFields();

		Given("특강 신청 페이지에 접속하여 현재 제공되는 특강 목록을 조회한다", this::retrieveAllLecturesAsList);
		When("{string} 아이디로 {string} 특강 신청을 요청하고 성공 응답을 받는다", this::requestLectureApplicationWithSuccessResponse);
		When("{string} 아이디로 {string} 특강 신청을 요청하고 실패 응답을 받는다", this::requestLectureApplicationWithFailResponse);


		Then("특강 신청 완료 여부를 조회하면 신청 성공 응답을 받고 응답은 다음과 같은 내용을 포함한다.", this::checkApplicationStatusWithSuccessResponse);
		Then("{string} 아이디로 {string} 특강 신청 완료 여부를 조회하면 다음과 같이 확인된다", this::checkApplicationStatusWithUserIdWithFailResponse);


		Then("{string} 아이디의 {string} 특강 신청 이력이 생성되었는지 확인한다", this::verifyLectureApplyHistoryIsProperlyCreated);
		Then("이력의 개수는 {int}개로 확인되고 다음과 같은 내용으로 확인되어야 한다", this::verifyLectureApplyHistoryContent);



		// 동시성 테스트 추가
		When("{int}명의 사용자가 동시에 {string} 특강 신청을 요청한다", this::requestLectureApplicationsConcurrently);
		Then("총 이력의 개수는 {int}개로 확인된다", this::verifyTotalHistoryCount);
		Then("{int}명에 대해 {string} 특강 신청 완료 여부를 조회하면 {int}명은 성공했고 {int}명은 실패했음이 확인된다", this::verifyConcurrentApplicationResults);

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


	private void requestLectureApplicationWithFailResponse(String userId, String lectureTitle, DataTable dataTable) {
		LectureGeneralResponse lecture = getSearchResponseByTitleFromResponses(lectureTitle);
		if (lecture == null) {
			throw new RuntimeException("test 실패! lectureTitle not found" );
		}

		LectureApplyRequest request = LectureApplyRequest.of(userId, lecture.lectureExternalId(), now());

		ExtractableResponse<Response> response = applyForLecture(request);
		assertNotEquals(200, response.statusCode());

		String expectedErrorMessage = dataTable.asMaps().get(0).get("execptionMessage");
		String actualErrorMessage = base64Decode(response.header("Response-Message"));
		assertEquals(expectedErrorMessage, actualErrorMessage, "실패 메시지가 기대한 메시지와 일치하지 않습니다.");
	}




	private void checkApplicationStatusWithSuccessResponse(DataTable dataTable) {
		LectureApplyRequest recentRequest = getMostRecentLectureApplyRequest();
		LectureApplicationStatusRequest statusRequest = LectureApplicationStatusRequest.of(recentRequest.getUserId(), recentRequest.getLectureExternalId());

		LectureApplicationStatusResponse statusResponse = parseLectureApplicationStatusResponse(checkSingleApplicationStatusWithOk(recentRequest.getUserId(), statusRequest));

		putLectureApplicationStatusResponse(statusResponse);

		Map<String, String> expectedData = dataTable.asMaps().get(0);
		assertTrue(matchResponse(expectedData, statusResponse), "status 필드가 정확하지 않습니다.");
	}


	private void checkApplicationStatusWithUserIdWithFailResponse(String userId, String lectureTitle, DataTable dataTable) {
		LectureGeneralResponse lecture = getSearchResponseByTitleFromResponses(lectureTitle);
		if (lecture == null) {
			throw new RuntimeException("test 실패! lectureTitle not found" );
		}

		LectureApplicationStatusRequest statusRequest = LectureApplicationStatusRequest.of(userId, lecture.lectureExternalId());
		LectureApplicationStatusResponse statusResponse = parseLectureApplicationStatusResponse(checkSingleApplicationStatusWithOk(userId, statusRequest));

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

	private void verifyLectureApplyHistoryContent(int count, DataTable dataTable) {
		LectureApplicationHistoryResponses historyResponses = getLectureApplyHistoryResponses(getMostRecentLectureApplyRequest().getUserId());

		assertEquals(count, historyResponses.getHistories().size(), "이력의 개수가 기대한 개수가 아닙니다.");

		Map<String, String> expectedData = dataTable.asMaps().get(0);
		LectureApplicationHistoryResponse historyResponse = historyResponses.getHistories().get(0);

		assertEquals(expectedData.get("userId"), historyResponse.userId(), "userId가 일치하지 않습니다.");
		assertEquals(Boolean.parseBoolean(expectedData.get("success")), historyResponse.success(), "success가 일치하지 않습니다.");
		assertEquals(expectedData.get("lectureTitle"), historyResponse.lecture().title(), "lectureTitle이 일치하지 않습니다.");
	}









	private void requestLectureApplicationsConcurrently(int count, String lectureTitle) {
		ExecutorService executorService = Executors.newFixedThreadPool(count);
		CountDownLatch latch = new CountDownLatch(count +1);

		IntStream.range(0, count).forEach(i -> {
			String userId = "user" + (i + 1);
			putConcurrentUserId(i, userId);
			executorService.submit(() -> {
				awaitLatch(latch);
				requestLectureApplicationWithSuccessResponse(userId, lectureTitle);
			});
			latch.countDown();
		});

		executorService.shutdown();
		awaitTermination(executorService, 30, TimeUnit.SECONDS);
	}

	private void verifyTotalHistoryCount(int expectedCount) {
		List<String> allUserIds = fetchAllRecentUserId();

		allUserIds.forEach(userId-> putLectureApplyHistoryResponses(userId, parseLectureApplyHistoryResponses(getAllApplicationHistoriesByUserId(userId))));

		int actualCount = allUserIds.stream()
			.mapToInt(userId -> getLectureApplyHistoryResponses(userId).getHistories().size())
			.sum();

		assertEquals(expectedCount, actualCount, "총 이력의 개수가 기대한 개수와 일치하지 않습니다.");
	}

	private void verifyConcurrentApplicationResults(int total, String lectureTitle, int expectedSuccess, int expectedFailure) {

		LectureGeneralResponse lecture = getSearchResponseByTitleFromResponses(lectureTitle);
		if (lecture == null) {
			throw new RuntimeException("test 실패! lectureTitle not found" );
		}

		long successCount = IntStream.range(0, total)
			.mapToObj(LectureApplyContextHolder::getConcurrentUserId)
			.map(userId -> parseLectureApplicationStatusResponse(checkSingleApplicationStatusWithOk(userId, LectureApplicationStatusRequest.of(userId, lecture.lectureExternalId()))))
			.filter(LectureApplicationStatusResponse::applied)
			.count();

		long failCount = total - successCount;

		assertEquals(expectedSuccess, successCount, "성공한 신청의 개수가 기대한 개수와 일치하지 않습니다.");
		assertEquals(expectedFailure, failCount, "실패한 신청의 개수가 기대한 개수와 일치하지 않습니다.");
	}

	@SneakyThrows
	private void awaitLatch(CountDownLatch latch) {
		latch.await(10, TimeUnit.SECONDS);
	}

	@SneakyThrows
	private void awaitTermination(ExecutorService executorService, long timeout, TimeUnit unit) {
		if (!executorService.awaitTermination(timeout, unit)) {
			executorService.shutdownNow();
		}
	}


}
