package io.hhpluslectureapplicationsystem.cucumber.contextholder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.hhpluslectureapplicationsystem.api.application.dto.LectureRegisterRequest;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureRegisterationResponse;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */public class LectureRegisterContextHolder {

	private static final ConcurrentHashMap<String, LectureRegisterationResponse> lectureResponseMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, LectureRegisterRequest> lectureRequestMap = new ConcurrentHashMap<>();
	private static final AtomicReference<String> mostRecentLectureResponseId = new AtomicReference<>();
	private static final AtomicReference<String> mostRecentLectureRequestId = new AtomicReference<>();

	public static void initFields() {
		lectureResponseMap.clear();
		lectureRequestMap.clear();
		mostRecentLectureResponseId.set(null);
		mostRecentLectureRequestId.set(null);
	}



	public static void putLectureRegisterationResponse(LectureRegisterationResponse response) {
		lectureResponseMap.put(response.lectureExternalId(), response);
		mostRecentLectureResponseId.set(response.lectureExternalId());
	}

	public static LectureRegisterationResponse getLectureRegisterationResponse(String id) {
		return lectureResponseMap.get(id);
	}

	public static void putLectureRegisterRequest(LectureRegisterRequest request) {
		lectureRequestMap.put(request.getTitle(), request);
		mostRecentLectureRequestId.set(request.getTitle());
	}

	public static LectureRegisterRequest getLectureRegisterRequest(String requestTitle) {
		return lectureRequestMap.get(requestTitle);
	}

	public static LectureRegisterRequest getMostRecentLectureRegisterRequest() {
		String recentRequestTitle = mostRecentLectureRequestId.get();
		return recentRequestTitle != null ? getLectureRegisterRequest(recentRequestTitle) : null;
	}

	public static LectureRegisterationResponse getMostRecentLectureRegisterationResponse() {
		String recentLectureId = mostRecentLectureResponseId.get();
		return recentLectureId != null ? getLectureRegisterationResponse(recentLectureId) : null;
	}

	public static LectureRegisterationResponse getMostRecentLectureRegisterationResponseByRequest() {
		String recentRequest = mostRecentLectureRequestId.get();
		return recentRequest != null ? getLectureRegisterationResponse(recentRequest) : null;
	}
}