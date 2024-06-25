package io.hhpluslectureapplicationsystem.api.business.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.hhpluslectureapplicationsystem.api.business.model.entity.Lecture;
import io.hhpluslectureapplicationsystem.api.business.model.entity.LectureApplication;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public interface LectureApplicationRepository extends JpaRepository<LectureApplication, String> {
	Optional<LectureApplication> findLectureApplicationByUserIdAndLecture(String userId, Lecture lecture);
	boolean existsByUserIdAndLectureLectureExternalId(String userId, String lectureExternalId);
	Optional<LectureApplication> findByUserIdAndLectureLectureExternalId(String userId, String lectureExternalId);

	boolean existsByUserIdAndLecture(String userId, Lecture lecture);

	long countByLecture(Lecture lecture);

	List<LectureApplication> findByUserId(String userId);


	// todo -> refactoring
	@Query("SELECT CASE WHEN COUNT(la) > 0 THEN TRUE ELSE FALSE END " +
		"FROM LectureApplication la " +
		"WHERE la.userId = :userId " +
		"AND la.lecture = :lecture " +
		"AND DATE(la.requestAt) = :requestDate")
	boolean existsByUserIdAndLectureAndRequestDate(
		@Param("userId") String userId,
		@Param("lecture") Lecture lecture,
		@Param("requestDate") LocalDate requestDate);
}
