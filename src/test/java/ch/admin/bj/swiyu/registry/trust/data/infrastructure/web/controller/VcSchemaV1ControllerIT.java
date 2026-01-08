/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data.infrastructure.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.admin.bj.swiyu.registry.trust.data.domain.*;
import ch.admin.bj.swiyu.registry.trust.data.test.*;
import jakarta.transaction.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.jdbc.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.test.context.*;
import org.springframework.test.context.transaction.*;
import org.springframework.test.web.servlet.*;
import org.testcontainers.junit.jupiter.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Testcontainers
@Transactional
@ContextConfiguration(initializers = PostgreSQLContainerInitializer.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VcSchemaV1ControllerIT {

    static final String BASE_URL = "/api/v1/vc-schema/";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private VcSchemaEntityRepository vcSchemaEntityRepository;

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
        mvc
            .perform(get(BASE_URL + testPath))
            // THEN
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().bytes(testData.getFile().getBytes()));
    }

    @ParameterizedTest
    @MethodSource("ch.admin.bj.swiyu.registry.trust.data.test.VcSchemaTestData#vcIdentifierPaths")
    void getVcSchema_isDeactivatedThrowsGone(String testPath) throws Exception {
        // GIVEN
        var testData = VcSchemaTestData.vcSchema(testPath, VcSchemaStatus.DEACTIVATED);
        vcSchemaEntityRepository.save(testData);
        commit();

        // WHEN
        mvc
            .perform(get(BASE_URL + testPath))
            // THEN
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isGone())
            .andExpect(jsonPath("$.status").value("GONE"));
    }

    @ParameterizedTest
    @MethodSource("ch.admin.bj.swiyu.registry.trust.data.test.VcSchemaTestData#vcIdentifierPaths")
    void getVcSchema_throwsNotFound(String testPath) throws Exception {
        // GIVEN
        VcSchemaTestData.vcSchema(testPath, VcSchemaStatus.ACTIVATED);
        // WHEN
        mvc
            .perform(get(BASE_URL + testPath))
            // THEN
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value("NOT_FOUND"));
    }
}
