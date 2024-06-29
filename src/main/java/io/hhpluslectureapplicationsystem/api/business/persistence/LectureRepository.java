package io.hhpluslectureapplicationsystem.api.business.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.hhpluslectureapplicationsystem.api.business.model.entity.Lecture;
import io.hhpluslectureapplicationsystem.api.infrastructure.persistence.LectureCustomRepository;
import jakarta.persistence.LockModeType;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public interface LectureRepository extends JpaRepository<Lecture, String>, LectureCustomRepository {
	Optional<Lecture> findByLectureExternalId(String externalId);


	/**
	 * <p>
	 * 주어진 외부 ID로 강의를 조회하며, PESSIMISTIC_WRITE 락을 사용하여 동시성 문제를 방지합니다.
	 * 이 메서드는 데드락을 방지하기 위해 @Lock 애노테이션을 사용합니다.
	 * 기술적 결정에서 QueryDSL을 사용하지 않고 다음과 같은 이유로 @Lock 애노테이션을 사용하였습니다.
	 * </p>
	 * <p>
	 * 1. 트랜잭션 관리의 단순화: @Lock 애노테이션은 JPA와 스프링 데이터 JPA가 트랜잭션과 락을 자동으로 관리하도록 합니다.
	 * 2. 안정성과 일관성: @Lock 애노테이션은 스프링 데이터 JPA와의 더 나은 통합으로 인해 락 설정이 일관되고 안정적입니다.
	 * </p>
	 *
	 * @param externalId 조회할 강의의 외부 ID
	 * @return 외부 ID에 해당하는 강의의 Optional 객체
	 */
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT l FROM Lecture l WHERE l.lectureExternalId = :externalId")
	Optional<Lecture> findByLectureExternalIdWithLock(@Param("externalId") String externalId);


}
