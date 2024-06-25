package io.hhpluslectureapplicationsystem.common.exception;

import io.hhpluslectureapplicationsystem.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
public class LectureApplicationNotFoundException extends ServerException{
	public LectureApplicationNotFoundException() {
		super(GlobalResponseCode.UNKNOWN_ERROR);
	}

	public LectureApplicationNotFoundException(GlobalResponseCode code) {
		super(code);
	}
}
