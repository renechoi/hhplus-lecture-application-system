package io.hhpluslectureapplicationsystem.api.business.model.entity;

import java.time.LocalDateTime;

/**
 * 특강의 상태를 나타내는 enum
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public enum LectureStatus {
	UPCOMING, // 특강이 계획되었고 등록이 가능합니다.
	ONGOING, // 특강이 현재 진행 중입니다.
	COMPLETED, // 특강이 완료되었습니다.
	CANCELLED; // 특강이 취소되어 진행되지 않습니다.

	public static LectureStatus determineStatus(LocalDateTime startTime, int durationMinutes) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime endTime = startTime.plusMinutes(durationMinutes);

		if (now.isBefore(startTime)) {
			return UPCOMING;
		} else if (now.isAfter(endTime)) {
			return COMPLETED;
		} else {
			return ONGOING;
		}
	}
}