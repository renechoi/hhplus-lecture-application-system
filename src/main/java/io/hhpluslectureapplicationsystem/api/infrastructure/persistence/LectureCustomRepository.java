package io.hhpluslectureapplicationsystem.api.infrastructure.persistence;

import java.util.Optional;

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplyCommand;
import io.hhpluslectureapplicationsystem.api.business.model.entity.Lecture;

public interface LectureCustomRepository {
    // Optional<Lecture> findByLectureExternalIdWithLock(String  lectureExternalId);
}