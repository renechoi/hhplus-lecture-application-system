package io.hhpluslectureapplicationsystem.api.business.operators;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.ibm.icu.text.Transliterator;

import org.springframework.stereotype.Component;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
@Component
public class LecturePkGenerator implements PkGenerator{
	private final String midfix = "-LC-";
	private final Transliterator transliterator = Transliterator.getInstance("Any-Latin; NFD; [:Nonspacing Mark:] Remove; NFC");


	@Override
	public String generate(String uniqueParam) {
		String sanitizedParam = processUniqueParam(uniqueParam);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
		String timeSuffix = LocalDateTime.now().format(formatter);
		return sanitizedParam + midfix + timeSuffix;
	}

	private String processUniqueParam(String uniqueParam) {
		String sanitizedParam = transliterator.transliterate(uniqueParam)
			.replaceAll("\\s+", "")
			.trim();
		return sanitizedParam.length() > 12 ? sanitizedParam.substring(0, 12) : sanitizedParam;
	}

}
