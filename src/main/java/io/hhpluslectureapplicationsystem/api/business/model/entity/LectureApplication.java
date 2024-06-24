package io.hhpluslectureapplicationsystem.api.business.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * 특강 신청을 의미하는 엔티티
 *
 * @author : Rene Choi
 * @since : 2024/06/23
 */
@Entity
public class LectureApplication {

	@Id
	private String lectureApplicationId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lecture_id", nullable = false)
	private Lecture lecture;

	@Column(nullable = false)
	private String userId;
}