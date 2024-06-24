package io.hhpluslectureapplicationsystem.api.business.persistence;

import java.util.List;

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplicationHistoryInfo;

/**
 * @author : Rene Choi
 * @since : 2024/06/24
 */
public interface LectureApplicationHistoryResolver {
	List<LectureApplicationHistoryInfo> getApplicationHistoriesByUserId(String userId);
}
