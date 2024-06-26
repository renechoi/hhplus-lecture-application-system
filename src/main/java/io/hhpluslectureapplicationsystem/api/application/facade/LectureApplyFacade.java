package io.hhpluslectureapplicationsystem.api.application.facade;

import java.util.concurrent.TimeUnit;

import org.springframework.dao.OptimisticLockingFailureException;

import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplicationStatusRequest;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplicationStatusResponse;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplicationHistoryResponses;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplyRequest;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplyResponse;
import io.hhpluslectureapplicationsystem.api.business.service.LectureApplyService;
import io.hhpluslectureapplicationsystem.common.annotation.Facade;
import io.hhpluslectureapplicationsystem.common.exception.LectureNotApplicableException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
@Facade
@RequiredArgsConstructor
public class LectureApplyFacade {
	private final LectureApplyService applyService;

	private static final int MAX_RETRY_COUNT = 3;
	private static final long RETRY_DELAY_MS = 100;

	public LectureApplyResponse applyForLecture(LectureApplyRequest request) {
		int retryCount = 0;
		while (retryCount < MAX_RETRY_COUNT) {
			try {
				return LectureApplyResponse.from(applyService.applyForLecture(request.toCommand()));
			} catch (OptimisticLockingFailureException e) {
				retryCount++;
				if (retryCount == MAX_RETRY_COUNT) {
					throw e;
				}
				try {
					TimeUnit.MILLISECONDS.sleep(RETRY_DELAY_MS);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					throw new LectureNotApplicableException();
				}
			}
		}
		throw new LectureNotApplicableException();
	}


	public LectureApplicationStatusResponse checkSingleApplicationStatus(LectureApplicationStatusRequest request) {
		return LectureApplicationStatusResponse.from(applyService.checkSingleApplicationStatus(request.toCommand()));
	}

	public LectureApplicationHistoryResponses getAllApplicationHistoriesByUserId(String userId) {
		return LectureApplicationHistoryResponses.from(applyService.getAllApplicationHistoriesByUserId(userId));
	}
}
