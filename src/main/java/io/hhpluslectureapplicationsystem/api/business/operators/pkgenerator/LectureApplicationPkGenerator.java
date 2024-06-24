package io.hhpluslectureapplicationsystem.api.business.operators.pkgenerator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.ibm.icu.text.Transliterator;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
@Component
public class LectureApplicationPkGenerator implements PkGenerator{
	private final String midfix = "-LA-";
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
