package io.hhpluslectureapplicationsystem.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.hhpluslectureapplicationsystem.api.interfaces.argumentresolver.RequestTimestampArgumentResolver;

/**
 * @author : Rene Choi
 * @since : 2024/06/25
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new RequestTimestampArgumentResolver());
	}
}