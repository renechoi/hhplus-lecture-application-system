package io.hhpluslectureapplicationsystem.api.business.validation.specification;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplyCommand;
import io.hhpluslectureapplicationsystem.api.business.model.entity.Lecture;
import io.hhpluslectureapplicationsystem.api.business.model.entity.LectureStatus;
import io.hhpluslectureapplicationsystem.api.business.persistence.LectureApplicationRepository;

/**
 * @author : Rene Choi
 * @since : 2024/06/27
 */
@ExtendWith(MockitoExtension.class)
class UniqueUserApplicationSpecificationTest {

	@Mock
	private LectureApplicationRepository applicationRepository;

	@InjectMocks
	private UniqueUserApplicationSpecification specification;

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
	@DisplayName("사용자가 동일한 날짜에 동일한 강의에 이미 신청했는지 확인하는 테스트")
	void testIsSatisfiedByWhenApplicationExists() {
		LectureApplyCommand command = new LectureApplyCommand("user1", "ext123456", LocalDateTime.now());

		when(applicationRepository.existsByUserIdAndLectureAndRequestDate(anyString(), any(Lecture.class), any(LocalDate.class)))
			.thenReturn(true);

		boolean result = specification.isSatisfiedBy(command, lecture);

		assertFalse(result, "사용자가 동일한 날짜에 동일한 강의에 이미 신청한 경우 결과는 false여야 합니다.");
	}

	@Test
	@DisplayName("사용자가 동일한 날짜에 동일한 강의에 신청하지 않은 경우 확인하는 테스트")
	void testIsSatisfiedByWhenApplicationDoesNotExist() {
		LectureApplyCommand command = new LectureApplyCommand("user1", "ext123456", LocalDateTime.now());

		when(applicationRepository.existsByUserIdAndLectureAndRequestDate(anyString(), any(Lecture.class), any(LocalDate.class)))
			.thenReturn(false);

		boolean result = specification.isSatisfiedBy(command, lecture);

		assertTrue(result, "사용자가 동일한 날짜에 동일한 강의에 신청하지 않은 경우 결과는 true여야 합니다.");
	}
}