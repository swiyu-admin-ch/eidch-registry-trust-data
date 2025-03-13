/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bit.eid.datastore.shared.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiError {

    private HttpStatus status;
    private String message;

    ApiError(HttpStatus status) {
        this.status = status;
        this.message = status.getReasonPhrase();
    }

    ApiError(HttpStatus status, Throwable exception) {
        this.status = status;
        this.message = exception.getMessage();
    }

    ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
