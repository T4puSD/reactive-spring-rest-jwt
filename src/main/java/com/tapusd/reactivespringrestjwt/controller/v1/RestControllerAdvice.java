package com.tapusd.reactivespringrestjwt.controller.v1;


import com.tapusd.reactivespringrestjwt.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class RestControllerAdvice extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = IllegalArgumentException.class)
    public Mono<ResponseEntity<Object>> handleIllegalArgumentException(IllegalArgumentException ex,
                                                                       ServerWebExchange exchange) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        return handleExceptionInternal(ex,
                new ErrorResponse(badRequest.value(), ex.getMessage()), null, badRequest, exchange);
    }
}
