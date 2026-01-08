/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */
package ch.admin.bj.swiyu.registry.trust.data.service;

import static ch.admin.bj.swiyu.registry.trust.data.test.NonComplianceListTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.admin.bj.swiyu.registry.trust.data.api.NonCompliantActorDto;
import ch.admin.bj.swiyu.registry.trust.data.domain.NonComplianceList;
import ch.admin.bj.swiyu.registry.trust.data.domain.NonComplianceListRepository;
import ch.admin.bj.swiyu.registry.trust.data.test.PostgreSQLContainerInitializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TestTransaction;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@Testcontainers
@DataJpaTest
@Import({ NonComplianceListService.class, JacksonAutoConfiguration.class })
@ContextConfiguration(initializers = PostgreSQLContainerInitializer.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NonComplianceListServiceIT {

    @Autowired
    private NonComplianceListRepository nonComplianceListRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NonComplianceListService nonComplianceListService;

    private static void commit() {
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    @BeforeEach
    void setUp() {
        nonComplianceListRepository.deleteAllInBatch();
    }

    @Test
    void getNonCompliantActors_shouldReturnListWithMaxPublishedAt() {
        // given
        var oldList = new NonComplianceList(
            UUID.randomUUID(),
            Instant.parse("2025-10-20T10:00:00Z"),
            NON_COMPLIANCE_LIST_PAYLOAD_1
        );
        var latestList = new NonComplianceList(
            UUID.randomUUID(),
            Instant.parse("2025-10-21T11:00:00Z"),
            NON_COMPLIANCE_LIST_PAYLOAD_2
        );
        var oldestList = new NonComplianceList(
            UUID.randomUUID(),
            Instant.parse("2024-06-14T07:00:00Z"),
            NON_COMPLIANCE_LIST_PAYLOAD_1
        );

        nonComplianceListRepository.saveAll(List.of(oldList, latestList, oldestList));
        commit();

        // when
        var nonCompliantActors = nonComplianceListService.getNonCompliantActors();

        // then it should be the actors from the latest list (5 entries)
        assertThat(nonCompliantActors.nonCompliantActors()).hasSize(5);

        // ... and verify specific DID, known to be only in the latest payload
        assertThat(nonCompliantActors.nonCompliantActors())
            .extracting(NonCompliantActorDto::did)
            .contains("did:tdw:alpha567");
    }

    @Test
    void getNonCompliantActors_corruptPayloadShouldThrowRuntimeException() {
        // given
        var list = new NonComplianceList(UUID.randomUUID(), Instant.parse("2025-10-20T10:00:00Z"), "corrupt-payload");

        nonComplianceListRepository.saveAll(List.of(list));
        commit();

        // when
        Exception exception = assertThrows(IllegalStateException.class, () ->
            nonComplianceListService.getNonCompliantActors()
        );

        // then
        assertEquals("Error while trying to deserialize NonComplianceList payload", exception.getMessage());
    }
}
