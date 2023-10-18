package org.onlinebankingweb.exceptionhandlers.rest;

import com.auth0.jwt.exceptions.TokenExpiredException;
import org.onlinebanking.core.domain.exceptions.ServiceException;
import org.onlinebanking.core.domain.exceptions.EntityNotFoundException;
import org.onlinebanking.core.domain.exceptions.FailedTransactionException;
import org.onlinebankingweb.dto.responses.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "org.onlinebankingweb.controllers.rest")
public class RestExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.NOT_FOUND.value(), "Entity not found",
                e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException e) {
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.FORBIDDEN.value(), "Access denied",
                e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ExceptionResponse> handleServiceException(ServiceException e) {
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FailedTransactionException.class)
    public ResponseEntity<ExceptionResponse> handleFailedTransactionException(FailedTransactionException e) {
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.CONFLICT.value(),
                "Failed transaction", e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ExceptionResponse> handleTokenExpiredException(TokenExpiredException e) {
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.FORBIDDEN.value(),
                "Token has expired", e.getMessage()), HttpStatus.FORBIDDEN);
    }
}
