package io.hhpluslectureapplicationsystem.common.model;

import java.util.Arrays;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/25
 */
@Getter
@RequiredArgsConstructor
public enum GlobalResponseCode implements ResponseCapable{

	// 정상 응답
	SUCCESS("0000", "success", HttpStatus.OK),
	CREATED("0001", "success", HttpStatus.CREATED),



	// 예외 응답
	NO_CONTENT("1005", "no content", HttpStatus.NO_CONTENT),
	ILLEGAL_ARGUMENT("1006","illegal argument", HttpStatus.BAD_REQUEST),



	// 실패 응답
	UNKNOWN_ERROR("9000", "unknown error", HttpStatus.INTERNAL_SERVER_ERROR),


	// domain

	LECTURE_NOT_FOUND("15001", "특강을 찾을 수 없습니다", HttpStatus.NOT_FOUND),

	LECTURE_NOT_APPLICABLE("15011", "특강 신청을 할 수 없습니다", HttpStatus.BAD_REQUEST),
	DUPLICATE_APPLICATION("15011", "이미 신청한 사용자입니다", HttpStatus.BAD_REQUEST),
	LECTURE_FULL("15012", "특강 신청이 마감되었습니다", HttpStatus.BAD_REQUEST);

	;

	private final String resultCode;
	private final String resultMessage;
	private final HttpStatus httpStatus;


	public static GlobalResponseCode getResponseCode(String code){
		return Arrays.stream(GlobalResponseCode.values())
			.filter(item->item.hasCodeName(code))
			.findAny()
			.orElse(GlobalResponseCode.UNKNOWN_ERROR);
	}

	public boolean hasCodeName(String code){
		return this.resultCode.equals(code);
	}

}
