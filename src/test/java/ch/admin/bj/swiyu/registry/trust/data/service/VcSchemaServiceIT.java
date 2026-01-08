/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */
package ch.admin.bj.swiyu.registry.trust.data.service;

import ch.admin.bj.swiyu.registry.trust.data.common.exception.*;
import ch.admin.bj.swiyu.registry.trust.data.domain.*;
import ch.admin.bj.swiyu.registry.trust.data.test.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.jdbc.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.context.annotation.*;
import org.springframework.test.context.*;
import org.springframework.test.context.transaction.*;
import org.testcontainers.junit.jupiter.*;

@ActiveProfiles("test")
@DataJpaTest
@Import({ VcSchemaService.class })
@Testcontainers
@ContextConfiguration(initializers = PostgreSQLContainerInitializer.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VcSchemaServiceIT {

    @Autowired
    private VcSchemaEntityRepository vcSchemaEntityRepository;

    @Autowired
    private VcSchemaService vcSchemaService;

    private static void commit() {
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    @BeforeEach
    void setUp() {
        vcSchemaEntityRepository.deleteAllInBatch();
    }

    @ParameterizedTest
    @MethodSource("ch.admin.bj.swiyu.registry.trust.data.test.VcSchemaTestData#vcIdentifierPaths")
    void getVcSchema(String testPath) throws Exception {
        // GIVEN
        var testData = VcSchemaTestData.vcSchema(testPath, VcSchemaStatus.ACTIVATED);
        vcSchemaEntityRepository.save(testData);
        commit();
        // WHEN
        var result = vcSchemaService.getVcSchema(testPath);
        // THEN
        Assertions.assertNotNull(result);
        Assertions.assertEquals(testData.getFile(), result);
    }

    @Test
    void getVcSchema_isDeactivatedAndThrows() {
        // GIVEN
        var testPath = "test";
        var testData = VcSchemaTestData.vcSchema(testPath, VcSchemaStatus.DEACTIVATED);
        vcSchemaEntityRepository.save(testData);
        commit();
        // WHEN / THEN
        Assertions.assertThrows(ResourceDeactivatedException.class, () -> vcSchemaService.getVcSchema(testPath));
    }

    @ParameterizedTest
    @MethodSource("ch.admin.bj.swiyu.registry.trust.data.test.VcSchemaTestData#vcIdentifierPaths")
    void getVcSchema_throwNotFound(String testPath) {
        // GIVEN
        VcSchemaTestData.vcSchema(testPath, VcSchemaStatus.ACTIVATED);
        // WHEN / THEN
        Assertions.assertThrows(ResourceNotFoundException.class, () -> vcSchemaService.getVcSchema(testPath));
    }
}
