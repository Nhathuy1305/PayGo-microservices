package com.main.apigateway.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class FeignAspects {

    @Pointcut("execution(* com.main.apigateway.client.PaymentClient.handlePaymentRequest(..))")
    public void modifyPaymentRequest() {
    }

    @Around("modifyPaymentRequest()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        final var args = joinPoint.getArgs();
        final var paymentRequest = (com.main.apigateway.models.dto.request.PaymentRequest) args[0];
//        UserDet

    }
}
