package com.example.yuvraj.EventTracker.Exception;

import com.example.yuvraj.EventTracker.dto.ApiResponse;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiResponse<Void>> handleDuplicateKeyError(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        logError("DataIntegrityViolationException", ex, request);

        ApiResponse<Void> response = new ApiResponse<>(
                false,
                "Username Already Exists: " + ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(
            AuthenticationException ex,
            HttpServletRequest request) {

        logError("AuthenticationException", ex, request);

        ApiResponse<Void> resp = new ApiResponse<>(
                false,
                "Authentication failed: " + ex.getMessage(),
                null
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(resp);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request) {

        logError("AccessDeniedException", ex, request);

        ApiResponse<Void> resp = new ApiResponse<>(
                false,
                "Access denied: " + ex.getMessage(),
                null
        );
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(resp);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Map<String, String>> handleJwt(
            JwtException ex,
            HttpServletRequest request) {

        logError("JwtException", ex, request);

        Map<String, String> body = Map.of(
                "error",   "unauthorized",
                "message", ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAllOtherExceptions(
            Exception ex,
            HttpServletRequest request
    ) {
        logError("UnhandledException", ex, request);

        String path = request.getRequestURI();
        ApiResponse<Void> resp = new ApiResponse<>(
                false,
                "Unexpected error at " + path + " : " + ex.getMessage(),
                null
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(resp);
    }

    private void logError(String exceptionType, Exception ex, HttpServletRequest request) {
        StringBuilder errorLog = new StringBuilder();
        errorLog.append("\n=== ERROR OCCURRED ===\n");
        errorLog.append("Timestamp: ").append(LocalDateTime.now()).append("\n");
        errorLog.append("Exception Type: ").append(exceptionType).append("\n");
        errorLog.append("Exception Message: ").append(ex.getMessage()).append("\n");
        errorLog.append("Exception Class: ").append(ex.getClass().getName()).append("\n");

        // Request details
        errorLog.append("Request Details:\n");
        errorLog.append("  Method: ").append(request.getMethod()).append("\n");
        errorLog.append("  URI: ").append(request.getRequestURI()).append("\n");
        errorLog.append("  Query String: ").append(request.getQueryString()).append("\n");
        errorLog.append("  Remote Address: ").append(request.getRemoteAddr()).append("\n");
        errorLog.append("  User Agent: ").append(request.getHeader("User-Agent")).append("\n");

        // Request headers (excluding sensitive ones)
        errorLog.append("  Headers:\n");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);

            // Mask sensitive headers
            if (isSensitiveHeader(headerName)) {
                headerValue = "***MASKED***";
            }

            errorLog.append("    ").append(headerName).append(": ").append(headerValue).append("\n");
        }

        // Request parameters
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (!parameterMap.isEmpty()) {
            errorLog.append("  Parameters:\n");
            parameterMap.forEach((key, values) -> {
                errorLog.append("    ").append(key).append(": ").append(String.join(", ", values)).append("\n");
            });
        }

        // Stack trace
        errorLog.append("Stack Trace:\n");
        StackTraceElement[] stackTrace = ex.getStackTrace();
        for (int i = 0; i < Math.min(stackTrace.length, 10); i++) { // Limit to first 10 stack frames
            errorLog.append("  ").append(stackTrace[i].toString()).append("\n");
        }

        // Caused by information
        Throwable cause = ex.getCause();
        if (cause != null) {
            errorLog.append("Caused by: ").append(cause.getClass().getName())
                    .append(": ").append(cause.getMessage()).append("\n");
        }

        errorLog.append("======================");

        log.error(errorLog.toString());
    }

    private boolean isSensitiveHeader(String headerName) {
        String lowerHeaderName = headerName.toLowerCase();
        return lowerHeaderName.contains("authorization") ||
                lowerHeaderName.contains("cookie") ||
                lowerHeaderName.contains("token") ||
                lowerHeaderName.contains("password");
    }
}
