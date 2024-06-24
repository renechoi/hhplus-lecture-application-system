package io.hhpluslectureapplicationsystem.api.business.operators.eventhandler;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import io.hhpluslectureapplicationsystem.api.business.model.event.LectureApplySuccessEvent;
import io.hhpluslectureapplicationsystem.api.business.model.event.LectureApplyTryEvent;
import io.hhpluslectureapplicationsystem.api.business.persistence.LectureApplyHistoryFactory;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/24
 */
@Component
@RequiredArgsConstructor
public class LectureApplyEventHandler {

	private final LectureApplyHistoryFactory lectureApplyHistoryFactory;

	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
	public void handleLectureApplyEvent(LectureApplySuccessEvent event) {
		lectureApplyHistoryFactory.upsertSuccessEvent(event);
	}

	@EventListener
	public void handleLectureApplyTryEvent(LectureApplyTryEvent event) {
		lectureApplyHistoryFactory.saveTryHistory(event);
	}
}