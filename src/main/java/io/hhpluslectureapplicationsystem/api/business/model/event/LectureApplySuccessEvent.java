package io.hhpluslectureapplicationsystem.api.business.model.event;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEvent;

import io.hhpluslectureapplicationsystem.api.business.model.entity.LectureApplicationHistory;
import io.hhpluslectureapplicationsystem.common.mapper.ObjectMapperBasedVoMapper;
import lombok.Getter;

/**
 * @author : Rene Choi
 * @since : 2024/06/24
 */
@Getter
public class LectureApplySuccessEvent extends ApplicationEvent {
	private final String userId;
	private final String lectureExternalId;
	private final String lectureApplicationId;
	private final boolean success;
	private final LocalDateTime appliedAt;
	private final LocalDateTime requestAt;


	public static LectureApplySuccessEvent of(Object source, String userId, String lectureId, String lectureApplicationId, LocalDateTime appliedAt, LocalDateTime reqeustAt){
		return new LectureApplySuccessEvent(source, userId, lectureId, lectureApplicationId, appliedAt, reqeustAt);
	}


	public LectureApplySuccessEvent(Object source, String userId, String lectureExternalId, String lectureApplicationId, LocalDateTime appliedAt, LocalDateTime requestAt) {
		super(source);
		this.userId = userId;
		this.lectureExternalId = lectureExternalId;
		this.lectureApplicationId = lectureApplicationId;
		this.success = true;
		this.appliedAt = appliedAt;
		this.requestAt = requestAt;
	}

	public LectureApplicationHistory toEntity(){
		return ObjectMapperBasedVoMapper.convert(this, LectureApplicationHistory.class);
	}
}