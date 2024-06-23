package io.hhpluslectureapplicationsystem.api.interfaces.controller;

import static java.net.URI.*;
import static org.springframework.http.ResponseEntity.*;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.hhpluslectureapplicationsystem.api.application.dto.LectureGeneralResponse;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureRegisterRequest;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureRegisterationResponse;
import io.hhpluslectureapplicationsystem.api.application.facade.LectureCrudFacade;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
@RestController
@RequestMapping("/lectures")
@RequiredArgsConstructor
public class LectureCrudController {
	private final LectureCrudFacade facade;

	@PostMapping
	public ResponseEntity<LectureRegisterationResponse> register(@Validated @RequestBody LectureRegisterRequest registerRequest) {
		LectureRegisterationResponse response = facade.register(registerRequest);
		return created(create("/lectures/" + response.lectureExternalId())).body(response);
	}

	@GetMapping("/{externalId}")
	public ResponseEntity<LectureGeneralResponse> searchSingleLecture(@PathVariable String externalId) {
		return ok(facade.searchSingleLectureById(externalId));
	}
}
