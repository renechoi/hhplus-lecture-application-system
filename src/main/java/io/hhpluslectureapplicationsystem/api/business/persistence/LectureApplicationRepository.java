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


	@Query("SELECT CASE WHEN COUNT(la) > 0 THEN true ELSE false END " +
		"FROM LectureApplication la " +
		"JOIN la.lecture l " +
		"WHERE la.userId = :userId AND l.lectureExternalId = :lectureExternalId")
	boolean existsByUserIdAndLectureExternalId(@Param("userId") String userId, @Param("lectureExternalId") String lectureExternalId);






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
