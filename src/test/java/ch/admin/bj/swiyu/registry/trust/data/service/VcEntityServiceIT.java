/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */
package ch.admin.bj.swiyu.registry.trust.data.service;

import static ch.admin.bj.swiyu.registry.trust.data.domain.datastore.DatastoreStatus.*;
import static ch.admin.bj.swiyu.registry.trust.data.test.VcEntityTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import ch.admin.bj.swiyu.registry.trust.data.domain.*;
import ch.admin.bj.swiyu.registry.trust.data.domain.datastore.*;
import ch.admin.bj.swiyu.registry.trust.data.test.*;
import java.util.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.jdbc.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.context.annotation.*;
import org.springframework.test.context.*;
import org.springframework.test.context.transaction.*;
import org.testcontainers.junit.jupiter.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
@DataJpaTest
@Import({ VcEntityService.class })
@ContextConfiguration(initializers = PostgreSQLContainerInitializer.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VcEntityServiceIT {

    @Autowired
    private VcEntityRepository vcEntityRepository;

    @Autowired
    private DataStoreEntityRepository dataStoreEntityRepository;

    @Autowired
    private VcEntityService vcEntityService;

    private static void commit() {
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    @BeforeEach
    void setUp() {
        vcEntityRepository.deleteAllInBatch();
        dataStoreEntityRepository.deleteAllInBatch();
    }

    /**
     * Tests the basic retrieval of VCs when filterActive is false and validates the correct repository method is called,
     * results are mapped to raw VCs and single item list is returned.
     */
    @Test
    void getTrustStatementsForDid() throws Exception {
        // GIVEN
        var datastoreEntityId = dataStoreEntityRepository.save(datastoreEntity(ACTIVE)).getId();
        vcEntityRepository.save(vcMetadataV1(datastoreEntityId, "did1"));
        commit();
        // WHEN
        var result = vcEntityService.getTrustStatementsForDid("did1", false);
        // THEN
        assertEquals(1, result.size());
    }

    /**
     * Tests retrieval with filterActive set to true, ensuring time limits are considered and validates time filtering
     * logic and correct repository method usage.
     */
    @Test
    void getTrustStatementsForDid_TrustStatements_ActiveOnly() throws Exception {
        // GIVEN
        var datastore1 = dataStoreEntityRepository.save(datastoreEntity(ACTIVE)).getId();
        var datastore2 = dataStoreEntityRepository.save(datastoreEntity(DEACTIVATED)).getId();
        vcEntityRepository.saveAll(List.of(vcMetadataV1(datastore1, "did1"), vcMetadataV1(datastore2, "did1")));
        commit();
        // WHEN
        var result = vcEntityService.getTrustStatementsForDid("did1", true);
        // THEN
        assertEquals(1, result.size());
    }

    /**
     * Tests handling of empty results when no VCs are found and validates proper handling of empty lists and correct
     * repository method call.
     */
    @Test
    void getTrustStatementsForDid_NoResults() throws Exception {
        // GIVEN
        var datastoreEntityId = dataStoreEntityRepository.save(datastoreEntity(ACTIVE)).getId();
        vcEntityRepository.save(VcEntityTestData.vcMetadataV1(datastoreEntityId));
        commit();
        // WHEN
        var result = vcEntityService.getTrustStatementsForDid("anotherdid", false);
        // THEN
        assertEquals(0, result.size());
    }

    @Test
    void getTrustStatementsForIssuanceVcSchema() throws Exception {
        // GIVEN (a couple of different trust statements)
        var datastore1 = dataStoreEntityRepository.save(datastoreEntity(ACTIVE)).getId();
        var datastore2 = dataStoreEntityRepository.save(datastoreEntity(ACTIVE)).getId();
        var datastore3 = dataStoreEntityRepository.save(datastoreEntity(ACTIVE)).getId();
        vcEntityRepository.save(vcMetadataV1(datastore1, "did1"));
        vcEntityRepository.save(vcVerificationV1(datastore3, "did1", "https://schema/dummy-example"));
        var vc = vcEntityRepository.save(vcIssuanceV1(datastore2, "did1", "https://schema/dummy-example"));
        commit();
        // WHEN (querying only for specific issuance statements)
        var result = vcEntityService.getTrustStatementsForIssuanceVcSchema("https://schema/dummy-example", true);
        // THEN
        assertEquals(1, result.size());
        assertThat(result.getFirst()).isEqualTo(vc.getRawVc());
    }

    @Test
    void getTrustStatementsForVerificationVcSchema() throws Exception {
        // GIVEN (a couple of different trust statements)
        var datastore1 = dataStoreEntityRepository.save(datastoreEntity(ACTIVE)).getId();
        var datastore2 = dataStoreEntityRepository.save(datastoreEntity(ACTIVE)).getId();
        var datastore3 = dataStoreEntityRepository.save(datastoreEntity(ACTIVE)).getId();
        vcEntityRepository.save(vcMetadataV1(datastore1, "did1"));
        vcEntityRepository.save(vcIssuanceV1(datastore2, "did1", "https://schema/dummy-example"));
        var vc = vcEntityRepository.save(vcVerificationV1(datastore3, "did1", "https://schema/dummy-example"));
        commit();
        // WHEN (querying only for specific verification statements)
        var result = vcEntityService.getTrustStatementsForVerificationVcSchema("https://schema/dummy-example", true);
        // THEN
        assertEquals(1, result.size());
        assertThat(result.getFirst()).isEqualTo(vc.getRawVc());
    }
}
