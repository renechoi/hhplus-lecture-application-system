package io.hhpluslectureapplicationsystem.api.business.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import io.hhpluslectureapplicationsystem.api.business.model.entity.LectureApplicationHistory;
import jakarta.persistence.LockModeType;

/**
 * @author : Rene Choi
 * @since : 2024/06/24
 */
public interface LectureApplyHistoryRepository extends JpaRepository<LectureApplicationHistory, Long> {
	List<LectureApplicationHistory> findByUserId(String userId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT lah FROM LectureApplicationHistory lah WHERE lah.userId = :userId AND lah.lectureExternalId = :lectureExternalId AND lah.requestAt = :requestAt")
	Optional<LectureApplicationHistory> findByUserIdAndLectureExternalIdAndRequestAtWithLock(String userId, String lectureExternalId, LocalDateTime requestAt);
}
