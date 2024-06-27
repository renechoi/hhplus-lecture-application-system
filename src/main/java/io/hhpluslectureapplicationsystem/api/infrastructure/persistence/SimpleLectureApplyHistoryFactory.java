package io.hhpluslectureapplicationsystem.api.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.hhpluslectureapplicationsystem.api.business.model.entity.LectureApplicationHistory;
import io.hhpluslectureapplicationsystem.api.business.model.event.LectureApplySuccessEvent;
import io.hhpluslectureapplicationsystem.api.business.model.event.LectureApplyTryEvent;
import io.hhpluslectureapplicationsystem.api.business.persistence.LectureApplyHistoryFactory;
import io.hhpluslectureapplicationsystem.api.business.persistence.LectureApplyHistoryRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/24
 */
@Component
@RequiredArgsConstructor
public class SimpleLectureApplyHistoryFactory implements LectureApplyHistoryFactory {
	private final LectureApplyHistoryRepository historyRepository;


	/**
	 * 특강 신청 ----> || 이력 생성 (비동기) || ---> [ 신청이 완료 - 이력 생성 ]
	 * 1) 성공 이벤트가 먼저 도달하는 경우 -> 이력이 존재하지 않을 것이므로 새로 생성하여 저장
	 * 2) 시도 이벤트가 먼저 도달하는 경우 -> 이미 저장된 시도 이력이 존재할 것이므로 해당 이벤트를 조회하여 덮어씀
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void upsertSuccessEvent(LectureApplySuccessEvent event){
		LectureApplicationHistory history = historyRepository
			.findByUserIdAndLectureExternalIdAndRequestAtWithLock(event.getUserId(), event.getLectureExternalId(), event.getRequestAt())
			.map(existingHistory -> existingHistory.updateSuccess(event.isSuccess()))
			.map(existingHistory -> existingHistory.updateLectureApplicationInfoWithSuccessEvent(event))
			.orElseGet(event::toEntity);

		historyRepository.save(history);
	}

	/**
	 * 1) 성공 이벤트가 먼저 도달하는 경우 -> 이미 이력이 생성되었을 것이므로 조회하여 존재한다면 별도로 저장할 필요 없이 return
	 * 2) 시도 이벤트가 먼저 도달하는 경우 -> 언제나 저장
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveTryHistory(LectureApplyTryEvent event) {

		Optional<LectureApplicationHistory> optionalHistory = historyRepository
			.findByUserIdAndLectureExternalIdAndRequestAtWithLock(event.getUserId(), event.getLectureExternalId(), event.getRequestAt());

		if (optionalHistory.isPresent()){
			return;
		}

		historyRepository.save(event.toEntity());
	}
}
