/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data.common.exception;

import java.text.*;

public class ResourceDeactivatedException extends RuntimeException {

    private static final MessageFormat ERR_MESSAGE = new MessageFormat(
        "Resource with id ''{0}'' of type ''{1}'' is deactivated"
    );

    public ResourceDeactivatedException(String id, ResourceType type) {
        super(ERR_MESSAGE.format(new String[] { id, type.name() }));
    }
}
