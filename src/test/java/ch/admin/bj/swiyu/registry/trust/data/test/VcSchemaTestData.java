/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */
package ch.admin.bj.swiyu.registry.trust.data.test;

import ch.admin.bj.swiyu.registry.trust.data.domain.*;
import java.net.*;
import java.util.stream.*;
import lombok.experimental.*;
import org.junit.jupiter.params.provider.*;

@UtilityClass
public class VcSchemaTestData {

    private static Stream<Arguments> vcIdentifierPaths() /* NOSONAR */{
        return Stream.of(
            Arguments.of("test"),
            Arguments.of("test/path"),
            Arguments.of("test.ext"),
            Arguments.of("test/test.ext"),
            Arguments.of("urn:vct:test")
        );
    }

    public static VcSchema vcSchema(String path, VcSchemaStatus status) {
        return new VcSchema(
            null,
            path,
            """
            {
                "vct":"%s"
            }""".formatted(URI.create("https://mock.registry/api/v1/vc-schema/").resolve(path).toString()),
            status
        );
    }
}
