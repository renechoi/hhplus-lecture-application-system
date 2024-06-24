package io.hhpluslectureapplicationsystem.api.business.persistence;

import io.hhpluslectureapplicationsystem.api.business.model.event.LectureApplySuccessEvent;
import io.hhpluslectureapplicationsystem.api.business.model.event.LectureApplyTryEvent;

/**
 * @author : Rene Choi
 * @since : 2024/06/24
 */
public interface LectureApplyHistoryFactory {
	void upsertSuccessEvent(LectureApplySuccessEvent event);

	void saveTryHistory(LectureApplyTryEvent event);
}
