package io.hhpluslectureapplicationsystem.api.interfaces.argumentresolver;

import java.time.LocalDateTime;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import io.hhpluslectureapplicationsystem.api.application.dto.LectureApplyRequest;
import io.hhpluslectureapplicationsystem.common.annotation.RequestTimestamp;

/**
 * @author : Rene Choi
 * @since : 2024/06/25
 */
public class RequestTimestampArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(RequestTimestamp.class) != null &&
			LectureApplyRequest.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		LectureApplyRequest request = (LectureApplyRequest) binderFactory.createBinder(webRequest, null, null)
			.convertIfNecessary(webRequest.getParameterMap(), LectureApplyRequest.class, parameter);
		request.setRequestAt(LocalDateTime.now());
		return request;
	}
}
