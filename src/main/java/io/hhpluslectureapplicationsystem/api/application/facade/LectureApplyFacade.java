package io.hhpluslectureapplicationsystem.api.application.facade;

import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplicationStatusRequest;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplicationStatusResponse;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplicationHistoryResponses;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplyRequest;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplyResponse;
import io.hhpluslectureapplicationsystem.api.business.service.LectureApplyService;
import io.hhpluslectureapplicationsystem.common.annotation.Facade;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
@Facade
@RequiredArgsConstructor
public class LectureApplyFacade {
	private final LectureApplyService applyService;

	public LectureApplyResponse applyForLecture(LectureApplyRequest request) {
		return LectureApplyResponse.from(applyService.applyForLecture(request.toCommand()));
	}

	public LectureApplicationStatusResponse checkSingleApplicationStatus(LectureApplicationStatusRequest request) {
		return LectureApplicationStatusResponse.from(applyService.checkSingleApplicationStatus(request.toCommand()));
	}

	public LectureApplicationHistoryResponses getAllApplicationHistoriesByUserId(String userId) {
		return LectureApplicationHistoryResponses.from(applyService.getAllApplicationHistoriesByUserId(userId));
	}
}
