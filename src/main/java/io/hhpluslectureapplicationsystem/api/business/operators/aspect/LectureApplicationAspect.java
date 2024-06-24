package io.hhpluslectureapplicationsystem.api.business.operators.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureApplyCommand;
import io.hhpluslectureapplicationsystem.api.business.model.event.LectureApplyTryEvent;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/24
 */
@Aspect
@Component
@RequiredArgsConstructor
public class LectureApplicationAspect {


	private final ApplicationEventPublisher eventPublisher;

	@Before("@annotation(io.hhpluslectureapplicationsystem.common.annotation.LogLectureApplyTry)")
	public void logLectureApplyTry(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		LectureApplyCommand command = (LectureApplyCommand) args[0];
		eventPublisher.publishEvent(new LectureApplyTryEvent(this, command.userId(), command.lectureExternalId(), command.requestAt()));
	}
}
