/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data.infrastructure.web.controller;

import ch.admin.bj.swiyu.registry.trust.data.api.*;
import ch.admin.bj.swiyu.registry.trust.data.common.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.*;
import lombok.extern.slf4j.*;
import org.springframework.core.*;
import org.springframework.core.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;
import org.springframework.web.servlet.mvc.method.annotation.*;

/**
 * Default Exception handler for exceptions that are common for all controllers.
 */
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ApiErrorDto handleResourceNotFoundException(final ResourceNotFoundException exception) {
        return new ApiErrorDto(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(ResourceDeactivatedException.class)
    @ResponseStatus(HttpStatus.GONE)
    protected ApiErrorDto handleResourceDeactivatedException(final ResourceDeactivatedException exception) {
        return new ApiErrorDto(HttpStatus.GONE, exception.getMessage());
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected void handleBrokenPipeException(IOException ex) {
        if (ex.getMessage() != null && ex.getMessage().contains("Broken pipe")) {
            // This is most likely a wrapped client abort exception meaning the client has already disconnected
            // Because there's no point in returning a response null is returned
            log.debug("Client aborted connection", ex);
        }
        log.error("Unhandled IO exception occurred", ex);
    }

    @ExceptionHandler(MultipartException.class)
    protected ApiErrorDto handleUnexpectedStreamClosing(MultipartException ex) {
        if (ex.getMessage() != null && ex.getMessage().contains("Stream ended unexpectedly")) {
            log.debug("Stream ended unexpectedly", ex);
            return new ApiErrorDto(HttpStatus.BAD_REQUEST);
        }
        log.error("Unhandled MultipartException exception occurred", ex);
        return new ApiErrorDto(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected void handleUnexpectedErrors(final Exception exception, HttpServletRequest request) {
        log.error("Detected unhandled exception for URL {}", request.getRequestURL(), exception);
    }
}
