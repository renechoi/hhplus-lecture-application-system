package io.hhpluslectureapplicationsystem.api.business.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplicationHistoryInfo;
import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplicationStatusCommand;
import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplicationStatusInfo;
import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplyCommand;
import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplyInfo;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public interface LectureApplyService {
	LectureApplyInfo applyForLecture(LectureApplyCommand command);

	LectureApplicationStatusInfo checkSingleApplicationStatus(LectureApplicationStatusCommand command);

	@Transactional(readOnly = true)
	List<LectureApplicationHistoryInfo> getAllApplicationHistoriesByUserId(String userId);
}
