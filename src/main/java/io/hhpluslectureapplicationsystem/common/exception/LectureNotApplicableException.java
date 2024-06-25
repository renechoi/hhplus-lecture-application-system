package io.hhpluslectureapplicationsystem.common.exception;

import io.hhpluslectureapplicationsystem.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/06/25
 */
public class LectureNotApplicableException extends ServerException{

	public LectureNotApplicableException() {
		super(GlobalResponseCode.LECTURE_NOT_APPLICABLE);
	}

	public LectureNotApplicableException(GlobalResponseCode code) {
		super(code);
	}
}
