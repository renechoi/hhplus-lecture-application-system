package io.hhpluslectureapplicationsystem.api.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import io.hhpluslectureapplicationsystem.api.business.model.entity.Lecture;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public interface LectureRepository extends JpaRepository<Lecture, String> {
}
