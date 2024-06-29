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

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureGeneralInfo;
import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureRegisterCommand;
import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureRegisterInfo;
import io.hhpluslectureapplicationsystem.api.business.model.entity.Lecture;
import io.hhpluslectureapplicationsystem.api.business.model.entity.LectureStatus;
import io.hhpluslectureapplicationsystem.api.business.operators.pkgenerator.LecturePkGenerator;
import io.hhpluslectureapplicationsystem.api.business.persistence.LectureRepository;
import io.hhpluslectureapplicationsystem.common.exception.LectureNotFoundException;

/**
 * @author : Rene Choi
 * @since : 2024/06/27
 */
@ExtendWith(MockitoExtension.class)
class SimpleLectureCrudServiceTest {

	@Mock
	private LectureRepository lectureRepository;

	@Mock
	private LecturePkGenerator lecturePkGenerator;

	@InjectMocks
	private SimpleLectureCrudService lectureCrudService;

	private Lecture lecture;
	private LectureRegisterCommand lectureRegisterCommand;

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

		lectureRegisterCommand = new LectureRegisterCommand(
			"샘플 강의",
			"이것은 샘플 강의입니다.",
			LocalDateTime.now().minusDays(1),
			LocalDateTime.now().plusDays(1),
			LocalDateTime.now().plusDays(2),
			120,
			100,
			"온라인",
			"홍길동"
		);
	}

	@Test
	@DisplayName("강의 등록 서비스 테스트")
	void testRegister() {
		when(lecturePkGenerator.generate(anyString())).thenReturn("generatedId");
		when(lectureRepository.save(any(Lecture.class))).thenReturn(lecture);

		LectureRegisterInfo result = lectureCrudService.register(lectureRegisterCommand);

		assertNotNull(result);
		assertEquals("샘플 강의", result.title());
		verify(lectureRepository, times(1)).save(any(Lecture.class));
	}

	@Test
	@DisplayName("단일 강의 조회 서비스 테스트")
	void testSearchSingleLectureById() {
		when(lectureRepository.findByLectureExternalId(anyString())).thenReturn(Optional.of(lecture));

		LectureGeneralInfo result = lectureCrudService.searchSingleLectureById("ext123456");

		assertNotNull(result);
		assertEquals("샘플 강의", result.title());
		verify(lectureRepository, times(1)).findByLectureExternalId(anyString());
	}

	@Test
	@DisplayName("단일 강의 조회 서비스 실패 테스트")
	void testSearchSingleLectureById_NotFound() {
		when(lectureRepository.findByLectureExternalId(anyString())).thenReturn(Optional.empty());

		assertThrows(LectureNotFoundException.class, () -> lectureCrudService.searchSingleLectureById("nonexistentId"));

		verify(lectureRepository, times(1)).findByLectureExternalId(anyString());
	}

	@Test
	@DisplayName("모든 강의 조회 서비스 테스트")
	void testListSearchLectures() {
		when(lectureRepository.findAll()).thenReturn(List.of(lecture));

		List<LectureGeneralInfo> result = lectureCrudService.listSearchLectures();

		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertEquals(1, result.size());
		verify(lectureRepository, times(1)).findAll();
	}
}
