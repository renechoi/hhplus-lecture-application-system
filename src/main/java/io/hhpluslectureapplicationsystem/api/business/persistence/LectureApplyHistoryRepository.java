package io.hhpluslectureapplicationsystem.api.business.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.hhpluslectureapplicationsystem.api.business.model.entity.LectureApplicationHistory;

/**
 * @author : Rene Choi
 * @since : 2024/06/24
 */
public interface LectureApplyHistoryRepository extends JpaRepository<LectureApplicationHistory, Long> {
	List<LectureApplicationHistory> findByUserId(String userId);

	Optional<LectureApplicationHistory> findByUserIdAndLectureExternalIdAndRequestAt(String userId, String lectureExternalId, LocalDateTime requestAt);
}
