package io.hhpluslectureapplicationsystem.api.business.service;

import java.util.List;

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

	List<LectureGeneralInfo> listSearchLectures();
}
