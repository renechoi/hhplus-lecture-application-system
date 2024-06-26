package io.hhpluslectureapplicationsystem.api.business.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Lecture {

	@Id
	private String lectureId;

	@Column(nullable = false, unique = true)
	private String lectureExternalId;

	// @Version
	// private Long version;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private LocalDateTime applicationOpenTime;

	@Column(nullable = false)
	private LocalDateTime applicationCloseTime;

	@Column(nullable = false)
	private LocalDateTime lectureStartTime;

	@Column(nullable = false)
	private int durationMinutes;

	@Column(nullable = false)
	private int capacity;

	@Column(nullable = false)
	private int registeredCount = 0; // 등록된 신청자 수

	@Column(nullable = false)
	private String location;

	@Column(nullable = false)
	private String  instructor;

	@Enumerated(EnumType.STRING)
	private LectureStatus status;

	@PrePersist
	private void onCreate() {
		this.lectureExternalId = UUID.randomUUID().toString().substring(0,12);
	}

	public Lecture withPk(String id) {
		this.lectureId = id;
		return this;
	}

	public Lecture determineStatus(LocalDateTime startTime, int durationMinutes) {
		this.status = LectureStatus.determineStatus(startTime, durationMinutes);
		return this;
	}

	public LocalDateTime calculateLectureEndTime() {
		return this.lectureStartTime.plusMinutes(durationMinutes);
	}

	// 신청자 수 증가
	public void incrementRegisteredCount() {
		this.registeredCount++;
	}

	// 수용 인원 초과 여부 확인
	@JsonIgnore
	public boolean isCapacityExceeded() {
		return this.registeredCount >= this.capacity;
	}
}
