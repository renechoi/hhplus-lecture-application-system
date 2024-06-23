package io.hhpluslectureapplicationsystem.api.business.operators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */

@Slf4j
class LecturePkGeneratorTest {
	private final LecturePkGenerator generator = new LecturePkGenerator();

	@Test
	@DisplayName("한국어 문자가 포함된 제목으로 PK를 생성할 때 ASCII 문자만 포함하는지 확인")
	void testGenerateWithKoreanCharacters() {
		// Given: 한국어 문자가 포함된 제목
		String title = "항해 플러스";

		// When: 제목을 이용하여 PK를 생성할 때
		String pk = generator.generate(title);
		log.info("생성된 PK: {}", pk);

		// Then: 생성된 PK가 ASCII 문자만 포함하는지 확인
		assertTrue(isAscii(pk), "생성된 PK에 비ASCII 문자가 포함되어 있습니다.");
	}

	@Test
	@DisplayName("한국어, 영어, 중국어가 혼합된 제목으로 PK를 생성할 때 ASCII 문자만 포함하는지 확인")
	void testGenerateWithMixedCharacters() {
		// Given: 한국어, 영어, 중국어가 혼합된 제목
		String title = "한글 English 混合";

		// When: 제목을 이용하여 PK를 생성할 때
		String pk = generator.generate(title);
		log.info("생성된 PK: {}", pk);

		// Then: 생성된 PK가 ASCII 문자만 포함하는지 확인
		assertTrue(isAscii(pk), "생성된 PK에 비ASCII 문자가 포함되어 있습니다.");

	}

	private boolean isAscii(String str) {
		return Pattern.matches("\\A\\p{ASCII}*\\z", str);
	}
}