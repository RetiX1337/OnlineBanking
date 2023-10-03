package org.onlinebankingweb.dto.responses;

public record ExceptionResponse(int status, String error, String message) {
}
