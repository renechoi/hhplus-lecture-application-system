package io.hhpluslectureapplicationsystem.common.exception;

import io.hhpluslectureapplicationsystem.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public class LectureNotFoundException extends ServerException{

	public LectureNotFoundException() {
		super(GlobalResponseCode.LECTURE_NOT_FOUND);
	}
	public LectureNotFoundException(GlobalResponseCode code) {
		super(code);
	}


}
