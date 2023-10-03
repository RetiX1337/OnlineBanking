package org.onlinebankingweb.exceptionhandlers.rest;

import org.onlinebanking.core.domain.exceptions.DAOException;
import org.onlinebanking.core.domain.exceptions.EntityNotFoundException;
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

    @ExceptionHandler(DAOException.class)
    public ResponseEntity<ExceptionResponse> handleDAOException(DAOException e) {
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}