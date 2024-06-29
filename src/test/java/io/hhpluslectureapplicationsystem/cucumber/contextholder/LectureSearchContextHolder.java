package io.hhpluslectureapplicationsystem.cucumber.contextholder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.hhpluslectureapplicationsystem.api.application.dto.LectureGeneralResponse;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureGeneralResponses;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public class LectureSearchContextHolder implements TestDtoContextHolder {
	private static final ConcurrentHashMap<String, LectureGeneralResponse> searchResponseMap = new ConcurrentHashMap<>();
	private static final AtomicReference<String> mostRecentSearchResponseId = new AtomicReference<>();
	private static final AtomicReference<LectureGeneralResponses> mostRecentSearchResponses = new AtomicReference<>();

	public static void initFields() {
		searchResponseMap.clear();
		mostRecentSearchResponseId.set(null);
		mostRecentSearchResponses.set(null);
	}

	public static void putSearchResponse(LectureGeneralResponse response) {
		searchResponseMap.put(response.lectureExternalId(), response);
		mostRecentSearchResponseId.set(response.lectureExternalId());
	}

	public static LectureGeneralResponse getSearchResponse(String id) {
		return searchResponseMap.get(id);
	}

	public static LectureGeneralResponse getMostRecentSearchResponse() {
		String recentSearchId = mostRecentSearchResponseId.get();
		return recentSearchId != null ? getSearchResponse(recentSearchId) : null;
	}

	public static void putSearchResponses(LectureGeneralResponses responses) {
		mostRecentSearchResponses.set(responses);
	}

	public static LectureGeneralResponses getMostRecentSearchResponses() {
		return mostRecentSearchResponses.get();
	}


	public static LectureGeneralResponse getSearchResponseByTitle(String title) {
		return searchResponseMap.values().stream()
			.filter(response -> response.title().equals(title))
			.findFirst()
			.orElse(null);
	}

	public static LectureGeneralResponse getSearchResponseByTitleFromResponses(String title) {
		LectureGeneralResponses responses = mostRecentSearchResponses.get();
		if (responses == null || responses.getLectures() == null) {
			return null;
		}

		return responses.getLectures().stream()
			.filter(response -> response.title().equals(title))
			.findFirst()
			.orElse(null);
	}
}