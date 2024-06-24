package io.hhpluslectureapplicationsystem.api.business.model.entity;

import java.time.LocalDateTime;

import org.springframework.data.domain.AbstractAggregateRoot;

import io.hhpluslectureapplicationsystem.api.business.model.event.LectureApplySuccessEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 특강 신청을 의미하는 엔티티
 *
 * @author : Rene Choi
 * @since : 2024/06/23
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
// @EntityListeners(LectureApplicationListener.class)
public class LectureApplication extends AbstractAggregateRoot<LectureApplication> {

	@Id
	private String lectureApplicationId;

	@Column(nullable = false, unique = true)
	private String lectureApplicationExternalId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lecture_id", nullable = false)
	private Lecture lecture;

	@Column(nullable = false)
	private String userId;

	private boolean applied;
	private LocalDateTime appliedAt;
	private LocalDateTime requestAt;



	public LectureApplication publish(){
		this.registerEvent(new LectureApplySuccessEvent(
			this,
			this.userId,
			this.getLectureExternalId(),
			this.lectureApplicationId,
			this.appliedAt,
			this.requestAt
		));
		return this;
	}

	public LectureApplication withPk(String id) {
		this.lectureApplicationId = id;
		return this;
	}

	public LectureApplication withSk(String externalId) {
		this.lectureApplicationExternalId = externalId;
		return this;
	}

	public LectureApplication withAppliedAt(LocalDateTime localDateTime) {
		this.appliedAt = localDateTime;
		return this;
	}

	public LectureApplication withLecture(Lecture lecture) {
		this.lecture = lecture;
		return this;
	}

	public LectureApplication asApplied() {
		this.applied = true;
		return this;
	}


	public String getLectureId() {
		return this.lecture != null ? this.lecture.getLectureId() : null;
	}

	public String getLectureExternalId() {
		return this.lecture != null ? this.lecture.getLectureExternalId() : null;
	}


}