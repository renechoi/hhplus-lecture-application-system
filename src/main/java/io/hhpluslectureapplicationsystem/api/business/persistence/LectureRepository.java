package io.hhpluslectureapplicationsystem.api.business.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.hhpluslectureapplicationsystem.api.business.model.entity.Lecture;
import jakarta.persistence.LockModeType;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public interface LectureRepository extends JpaRepository<Lecture, String> {
	Optional<Lecture> findByLectureExternalId(String externalId);


	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT l FROM Lecture l WHERE l.lectureExternalId = :externalId")
	Optional<Lecture> findByLectureExternalIdForUpdate(@Param("externalId") String externalId);


}
