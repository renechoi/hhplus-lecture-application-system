package io.hhpluslectureapplicationsystem.api.business.model.entity;

import java.time.LocalDateTime;

import io.hhpluslectureapplicationsystem.api.business.model.event.LectureApplySuccessEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/24
 */
@Entity
@Getter
@NoArgsConstructor
public class LectureApplicationHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Version
	private Long version;

	private String userId;
	private String lectureExternalId;
	private String lectureApplicationId;
	private boolean success;

	private LocalDateTime appliedAt;
	private LocalDateTime requestAt;

	public LectureApplicationHistory updateSuccess(boolean success){
		this.success = success;
		return this;
	}

	public LectureApplicationHistory updateLectureApplicationInfoWithSuccessEvent(LectureApplySuccessEvent event) {
		this.appliedAt = event.getAppliedAt();
		this.lectureApplicationId = event.getLectureApplicationId();;
		return this;
	}
}
