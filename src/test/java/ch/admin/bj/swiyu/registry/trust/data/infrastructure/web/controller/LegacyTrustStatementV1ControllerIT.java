/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data.infrastructure.web.controller;

import static ch.admin.bj.swiyu.registry.trust.data.domain.datastore.DatastoreStatus.ACTIVE;
import static ch.admin.bj.swiyu.registry.trust.data.domain.datastore.DatastoreStatus.DEACTIVATED;
import static ch.admin.bj.swiyu.registry.trust.data.test.VcEntityTestData.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.admin.bj.swiyu.registry.trust.data.domain.VcEntityRepository;
import ch.admin.bj.swiyu.registry.trust.data.domain.datastore.DataStoreEntityRepository;
import ch.admin.bj.swiyu.registry.trust.data.test.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.*;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
@ContextConfiguration(initializers = PostgreSQLContainerInitializer.class)
class LegacyTrustStatementV1ControllerIT {

    private static final String ENTRY_TS_URL = "/api/v1/truststatements/";

    private static final String DID =
        "did:tdw:QmbF3pVGdkZkpZCmVhV2cntPAKcRUrdtq7Qrc2RgE6GUgr:identifier-reg-d.trust-infra.swiyu.admin.ch:api:v1:did:8a4c76f7-c255-41dd-ad87-f05daddd5a07";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private VcEntityRepository vcEntityRepository;

    @Autowired
    private DataStoreEntityRepository dataStoreEntityRepository;

    private String expectedRawVc;

    @BeforeEach
    void setUp() throws Exception {
        vcEntityRepository.deleteAllInBatch();
        dataStoreEntityRepository.deleteAllInBatch();
        // setup some vc entries with different statuses
        var id1 = dataStoreEntityRepository.save(datastoreEntity(ACTIVE)).getId();
        var id2 = dataStoreEntityRepository.save(datastoreEntity(ACTIVE)).getId();
        var id3 = dataStoreEntityRepository.save(datastoreEntity(DEACTIVATED)).getId();
        var id4 = dataStoreEntityRepository.save(datastoreEntity(DEACTIVATED)).getId();
        expectedRawVc = vcEntityRepository.save(vcMetadataV1(id1, DID)).getRawVc();
        vcEntityRepository.save(vcIdentityV1(id2, DID));
        vcEntityRepository.save(vcMetadataV1(id3, DID));
        vcEntityRepository.save(vcMetadataV1(id4, DID));
    }

    @Test
    void getVCEntries_noDataKnown_response() throws Exception {
        mvc
            .perform(get(ENTRY_TS_URL + "did:unknown"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void getVCEntries_noExplicitFilter_response() throws Exception {
        mvc
            .perform(get(ENTRY_TS_URL + DID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", Matchers.hasSize(1)))
            .andExpect(jsonPath("$", Matchers.containsInAnyOrder(expectedRawVc)));
    }

    @Test
    void getVCEntries_explicitActiveFilterTrue_response() throws Exception {
        mvc
            .perform(get(ENTRY_TS_URL + DID + "?filterActive=true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", Matchers.hasSize(1)))
            .andExpect(jsonPath("$", Matchers.containsInAnyOrder(expectedRawVc)));
    }

    @Test
    void getVCEntries_explicitActiveFilterFalse_response() throws Exception {
        mvc
            .perform(get(ENTRY_TS_URL + DID + "?filterActive=false"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", Matchers.hasSize(3)));
    }
}
