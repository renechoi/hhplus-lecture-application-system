package io.hhpluslectureapplicationsystem.api.business.model.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
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

	@Version
	private Long version;

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
	private String location;

	@Column(nullable = false)
	private String  instructor;

	@Enumerated(EnumType.STRING)
	private LectureStatus status;


	public Lecture withPk(String id) {
		this.lectureId = id;
		return this;
	}

	public Lecture determineStatus(LocalDateTime startTime, int durationMinutes) {
		this.status = LectureStatus.determineStatus(startTime, durationMinutes);
		return this;
	}
}
