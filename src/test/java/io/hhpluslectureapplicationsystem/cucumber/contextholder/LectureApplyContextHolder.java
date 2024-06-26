package io.hhpluslectureapplicationsystem.cucumber.contextholder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplicationStatusRequest;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplicationStatusResponse;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplicationHistoryResponses;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplyRequest;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplyResponse;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public class LectureApplyContextHolder {

	private static final ConcurrentHashMap<String, LectureApplyResponse> applyResponseMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, LectureApplyRequest> applyRequestMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, LectureApplicationStatusResponse> statusResponseMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, LectureApplicationStatusRequest> statusRequestMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, LectureApplicationHistoryResponses> historyResponseMap = new ConcurrentHashMap<>();
	private static final AtomicReference<String> mostRecentApplyResponseId = new AtomicReference<>();
	private static final AtomicReference<String> mostRecentApplyRequestId = new AtomicReference<>();
	private static final AtomicReference<String> mostRecentHistoryResponseId = new AtomicReference<>();

	private static final ConcurrentHashMap<Integer, String> concurrentUserIds = new ConcurrentHashMap<>();


	public static void initFields() {
		applyResponseMap.clear();
		applyRequestMap.clear();
		statusResponseMap.clear();
		statusRequestMap.clear();
		historyResponseMap.clear();
		mostRecentApplyResponseId.set(null);
		mostRecentApplyRequestId.set(null);
		mostRecentHistoryResponseId.set(null);
		concurrentUserIds.clear();
	}

	public static void putLectureApplyResponse(LectureApplyResponse response) {
		applyResponseMap.put(response.lectureApplicationExternalId(), response);
		mostRecentApplyResponseId.set(response.message());
	}

	public static LectureApplyResponse getLectureApplyResponse(String message) {
		return applyResponseMap.get(message);
	}

	public static void putLectureApplyRequest(LectureApplyRequest request) {
		applyRequestMap.put(request.getUserId(), request);
		mostRecentApplyRequestId.set(request.getUserId());
	}

	public static LectureApplyRequest getLectureApplyRequest(String userId) {
		return applyRequestMap.get(userId);
	}

	public static LectureApplicationStatusResponse putLectureApplicationStatusResponse(LectureApplicationStatusResponse response) {
		statusResponseMap.put(response.toString(), response);
		return response;
	}

	public static LectureApplicationStatusResponse getLectureApplicationStatusResponse(String responseStr) {
		return statusResponseMap.get(responseStr);
	}

	public static void putLectureApplicationStatusRequest(LectureApplicationStatusRequest request) {
		statusRequestMap.put(request.getUserId(), request);
	}

	public static LectureApplicationStatusRequest getLectureApplicationStatusRequest(String userId) {
		return statusRequestMap.get(userId);
	}

	public static LectureApplyRequest getMostRecentLectureApplyRequest() {
		String recentRequestUserId = mostRecentApplyRequestId.get();
		return recentRequestUserId != null ? getLectureApplyRequest(recentRequestUserId) : null;
	}

	public static LectureApplyResponse getMostRecentLectureApplyResponse() {
		String recentResponseMessage = mostRecentApplyResponseId.get();
		return recentResponseMessage != null ? getLectureApplyResponse(recentResponseMessage) : null;
	}

	public static void putLectureApplyHistoryResponses(String userId, LectureApplicationHistoryResponses response) {
		historyResponseMap.put(userId, response);
		mostRecentHistoryResponseId.set(userId);
	}

	public static LectureApplicationHistoryResponses getLectureApplyHistoryResponses(String userId) {
		return historyResponseMap.get(userId);
	}

	public static LectureApplicationHistoryResponses getMostRecentLectureApplyHistoryResponses() {
		String recentHistoryUserId = mostRecentHistoryResponseId.get();
		return recentHistoryUserId != null ? getLectureApplyHistoryResponses(recentHistoryUserId) : null;
	}


	public static void putConcurrentUserId(int index, String userId) {
		concurrentUserIds.put(index, userId);
	}

	public static String getConcurrentUserId(int index) {
		return concurrentUserIds.get(index);
	}

	public static int getConcurrentUserCount() {
		return concurrentUserIds.size();
	}

	public static List<String> fetchAllRecentUserId() {
		return new ArrayList<>(concurrentUserIds.values());
	}
}