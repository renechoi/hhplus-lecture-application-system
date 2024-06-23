package io.hhpluslectureapplicationsystem.api.application.facade;

import io.hhpluslectureapplicationsystem.api.application.dto.LectureGeneralResponse;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureRegisterRequest;
import io.hhpluslectureapplicationsystem.api.application.dto.LectureRegisterationResponse;
import io.hhpluslectureapplicationsystem.api.business.service.LectureCrudService;
import io.hhpluslectureapplicationsystem.common.annotation.Facade;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
@Facade
@RequiredArgsConstructor
public class LectureCrudFacade {

	private final LectureCrudService crudService;


	public LectureRegisterationResponse register(LectureRegisterRequest registerRequest) {
		return LectureRegisterationResponse.from(crudService.register(registerRequest.toCommand()));
	}

	public LectureGeneralResponse searchSingleLectureById(String externalId) {
		return LectureGeneralResponse.from(crudService.searchSingleLectureById(externalId));
	}
}
