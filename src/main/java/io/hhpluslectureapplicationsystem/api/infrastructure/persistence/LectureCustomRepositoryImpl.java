package io.hhpluslectureapplicationsystem.api.infrastructure.persistence;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/28
 */
@Repository
@RequiredArgsConstructor
public class LectureCustomRepositoryImpl implements LectureCustomRepository {

	private final JPAQueryFactory queryFactory;
	private final EntityManager entityManager;


	// @Override
	// public Optional<Lecture> findByLectureExternalIdWithLock(String  lectureExternalId) {
	// 	QLecture lecture = QLecture.lecture;
	//
	// 	Lecture result = queryFactory.selectFrom(lecture)
	// 		.where(lecture.lectureExternalId.eq(lectureExternalId))
	// 		.setLockMode(LockModeType.PESSIMISTIC_WRITE)
	// 		.fetchOne();
	//
	// 	return Optional.ofNullable(result);
	// }
}