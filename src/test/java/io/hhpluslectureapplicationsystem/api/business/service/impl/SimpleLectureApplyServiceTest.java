package io.hhpluslectureapplicationsystem.api.business.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplicationHistoryInfo;
import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplicationInfo;
import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplicationStatusCommand;
import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplicationStatusInfo;
import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplyCommand;
import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplyInfo;
import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureGeneralInfo;
import io.hhpluslectureapplicationsystem.api.business.model.entity.Lecture;
import io.hhpluslectureapplicationsystem.api.business.model.entity.LectureApplication;
import io.hhpluslectureapplicationsystem.api.business.model.entity.LectureStatus;
import io.hhpluslectureapplicationsystem.api.business.operators.pkgenerator.LectureApplicationPkGenerator;
import io.hhpluslectureapplicationsystem.api.business.persistence.LectureApplicationHistoryResolver;
import io.hhpluslectureapplicationsystem.api.business.persistence.LectureApplicationRepository;
import io.hhpluslectureapplicationsystem.api.business.persistence.LectureRepository;
import io.hhpluslectureapplicationsystem.api.business.validation.validators.Validator;
import io.hhpluslectureapplicationsystem.common.exception.LectureNotFoundException;

/**
 * @author : Rene Choi
 * @since : 2024/06/27
 */

@ExtendWith(MockitoExtension.class)
class SimpleLectureApplyServiceTest {

	@Mock
	private LectureRepository lectureRepository;

	@Mock
	private LectureApplicationRepository lectureApplicationRepository;

	@Mock
	private LectureApplicationPkGenerator lectureApplicationPkGenerator;

	@Mock
	private LectureApplicationHistoryResolver historyResolver;

	@Mock
	private Validator<LectureApplyCommand, Lecture> lectureApplyValidator;

	@InjectMocks
	private SimpleLectureApplyService lectureApplyService;

	private Lecture lecture;
	private LectureApplyCommand lectureApplyCommand;
	private LectureApplication lectureApplication;

	@BeforeEach
	void setUp() {

		lecture = Lecture.builder()
			.lectureId(UUID.randomUUID().toString())
			.lectureExternalId("ext123456")
			.title("샘플 강의")
			.description("이것은 샘플 강의입니다.")
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

		lectureApplyCommand = new LectureApplyCommand(
			"user1",
			"ext123456",
			LocalDateTime.now()
		);

		lectureApplication = LectureApplication.builder()
			.lectureApplicationId(UUID.randomUUID().toString())
			.lecture(lecture)
			.userId("user1")
			.applied(true)
			.appliedAt(LocalDateTime.now())
			.requestAt(LocalDateTime.now())
			.build();
	}

	@Test
	@DisplayName("강의 신청 서비스 테스트")
	void testApplyForLecture() {
		when(lectureRepository.findByLectureExternalIdWithLock(any())).thenReturn(Optional.of(lecture));
		when(lectureApplicationPkGenerator.generate(anyString())).thenReturn("generatedId");
		when(lectureApplicationRepository.save(any(LectureApplication.class))).thenReturn(lectureApplication);

		LectureApplyInfo result = lectureApplyService.applyForLecture(lectureApplyCommand);

		assertNotNull(result);
		assertEquals("user1", result.userId());
		verify(lectureRepository, times(1)).findByLectureExternalIdWithLock(any());
		verify(lectureApplicationRepository, times(1)).save(any(LectureApplication.class));
	}

	@Test
	@DisplayName("강의 신청 서비스 - 강의가 존재하지 않는 경우 테스트")
	void testApplyForLecture_LectureNotFound() {
		when(lectureRepository.findByLectureExternalIdWithLock(any())).thenReturn(Optional.empty());

		assertThrows(LectureNotFoundException.class, () -> lectureApplyService.applyForLecture(lectureApplyCommand));

		verify(lectureRepository, times(1)).findByLectureExternalIdWithLock(any());
		verify(lectureApplicationRepository, times(0)).save(any(LectureApplication.class));
	}

	@Test
	@DisplayName("단일 강의 신청 상태 확인 서비스 테스트")
	void testCheckSingleApplicationStatus() {
		when(lectureApplicationRepository.existsByUserIdAndLectureExternalId(anyString(), anyString())).thenReturn(true);

		LectureApplicationStatusCommand command = new LectureApplicationStatusCommand("user1", "ext123456");
		LectureApplicationStatusInfo result = lectureApplyService.checkSingleApplicationStatus(command);

		assertNotNull(result);
		assertTrue(result.applied());
		verify(lectureApplicationRepository, times(1)).existsByUserIdAndLectureExternalId(anyString(), anyString());
	}

	@Test
	@DisplayName("사용자별 모든 강의 신청 이력 조회 서비스 테스트")
	void testGetAllApplicationHistoriesByUserId() {
		LectureApplicationHistoryInfo historyInfo = LectureApplicationHistoryInfo.builder()
			.userId("user1")
			.lecture(LectureGeneralInfo.from(lecture))
			.lectureApplication(LectureApplicationInfo.from(lectureApplication))
			.success(true)
			.appliedAt(LocalDateTime.now())
			.requestAt(LocalDateTime.now())
			.build();

		when(historyResolver.getApplicationHistoriesByUserId(anyString())).thenReturn(List.of(historyInfo));

		List<LectureApplicationHistoryInfo> result = lectureApplyService.getAllApplicationHistoriesByUserId("user1");

		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertEquals(1, result.size());
		verify(historyResolver, times(1)).getApplicationHistoriesByUserId(anyString());
	}
}
