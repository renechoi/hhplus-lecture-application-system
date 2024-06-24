package io.hhpluslectureapplicationsystem.api.interfaces.controller;

import static org.springframework.http.ResponseEntity.*;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplicationStatusRequest;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplicationStatusResponse;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplicationHistoryResponses;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplyRequest;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplyResponse;
import io.hhpluslectureapplicationsystem.api.application.facade.LectureApplyFacade;
import io.hhpluslectureapplicationsystem.common.annotation.RequestTimestamp;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
@RestController
@RequestMapping("/lectures")
@RequiredArgsConstructor
public class LectureApplyController {
	private final LectureApplyFacade facade;

	@PostMapping("/apply")
	public ResponseEntity<LectureApplyResponse> apply(@Validated @RequestTimestamp @RequestBody LectureApplyRequest request) {
		return ok(facade.applyForLecture(request));
	}

	@GetMapping("/application/{userId}")
	public ResponseEntity<LectureApplicationStatusResponse> checkSingleApplicationStatus(@PathVariable String userId, @Validated @RequestBody LectureApplicationStatusRequest request) {
		return ResponseEntity.ok(facade.checkSingleApplicationStatus(request.withUserId(userId)));
	}


	@GetMapping("/applications/{userId}/history")
	public ResponseEntity<LectureApplicationHistoryResponses> getAllApplicationHistoriesByUserId(@PathVariable String userId) {
		return ResponseEntity.ok(facade.getAllApplicationHistoriesByUserId(userId));
	}
}