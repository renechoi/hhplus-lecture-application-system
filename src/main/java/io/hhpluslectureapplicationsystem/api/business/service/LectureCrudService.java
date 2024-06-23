package io.hhpluslectureapplicationsystem.api.business.service;

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureGeneralInfo;
import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureRegisterCommand;
import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureRegisterInfo;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */

public interface LectureCrudService {

	LectureRegisterInfo register(LectureRegisterCommand command);

	LectureGeneralInfo searchSingleLectureById(String id);
}
