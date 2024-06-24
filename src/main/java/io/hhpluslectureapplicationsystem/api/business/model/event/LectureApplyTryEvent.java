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
public class LectureApplyTryEvent extends ApplicationEvent {
	private final String userId;
	private final String lectureExternalId;
	private final LocalDateTime requestAt;

	public LectureApplyTryEvent(Object source, String userId, String lectureExternalId, LocalDateTime requestAt) {
		super(source);
		this.userId = userId;
		this.lectureExternalId = lectureExternalId;
		this.requestAt = requestAt;
	}

	public LectureApplicationHistory toEntity(){
		return ObjectMapperBasedVoMapper.convert(this, LectureApplicationHistory.class);
	}

}