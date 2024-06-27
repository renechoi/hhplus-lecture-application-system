package io.hhpluslectureapplicationsystem.api.business.model.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author : Rene Choi
 * @since : 2024/06/27
 */

class LectureTest {

	private Lecture lecture;

	@BeforeEach
	void setUp() {
		lecture = Lecture.builder()
			.lectureId("1")
			.lectureExternalId("ext123456")
			.title("샘플 강의")
			.description("샘플 강의입니다.")
			.applicationOpenTime(LocalDateTime.now().minusDays(1))
			.applicationCloseTime(LocalDateTime.now().plusDays(1))
			.lectureStartTime(LocalDateTime.now().plusDays(2))
			.durationMinutes(120)
			.capacity(100)
			.registeredCount(0)
			.location("온라인")
			.instructor("홍길동")
			.status(LectureStatus.ONGOING)
			.build();
	}

	@Test
	@DisplayName("강의 상태 결정 테스트")
	void testDetermineStatus() {
		LocalDateTime startTime = LocalDateTime.now().plusDays(1);
		Lecture updatedLecture = lecture.determineStatus(startTime, 120);
		assertEquals(LectureStatus.UPCOMING, updatedLecture.getStatus());
	}

	@Test
	@DisplayName("강의 종료 시간 계산 테스트")
	void testCalculateLectureEndTime() {
		LocalDateTime expectedEndTime = lecture.getLectureStartTime().plusMinutes(120);
		assertEquals(expectedEndTime, lecture.calculateLectureEndTime());
	}

	@Test
	@DisplayName("등록된 신청자 수 증가 테스트")
	void testIncrementRegisteredCount() {
		lecture.incrementRegisteredCount();
		assertEquals(1, lecture.getRegisteredCount());
	}

	@Test
	@DisplayName("수용 인원 초과 여부 확인 테스트")
	void testIsCapacityExceeded() {
		for (int i = 0; i < lecture.getCapacity(); i++) {
			lecture.incrementRegisteredCount();
		}
		assertTrue(lecture.isCapacityExceeded());
	}

}
