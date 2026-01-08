/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data.infrastructure.web.controller;

import static ch.admin.bj.swiyu.registry.trust.data.domain.datastore.DatastoreStatus.*;
import static ch.admin.bj.swiyu.registry.trust.data.test.VcEntityTestData.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.*;

import ch.admin.bj.swiyu.registry.trust.data.domain.*;
import ch.admin.bj.swiyu.registry.trust.data.domain.datastore.*;
import ch.admin.bj.swiyu.registry.trust.data.test.*;
import com.fasterxml.jackson.databind.*;
import java.net.*;
import java.nio.charset.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.web.server.*;
import org.springframework.http.*;
import org.springframework.test.context.*;
import org.springframework.web.client.*;
import org.testcontainers.junit.jupiter.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
@ContextConfiguration(initializers = PostgreSQLContainerInitializer.class)
class TrustStatementV1ControllerIT {

    private static final String DID =
        "did:tdw:QmbF3pVGdkZkpZCmVhV2cntPAKcRUrdtq7Qrc2RgE6GUgr:identifier-reg-d.trust-infra.swiyu.admin.ch:api:v1:did:8a4c76f7-c255-41dd-ad87-f05daddd5a07";

    @LocalServerPort
    private int port;

    @Autowired
    private VcEntityRepository vcEntityRepository;

    @Autowired
    private DataStoreEntityRepository dataStoreEntityRepository;

    private RestClient restClient;

    private VcEntity metadataVc;
    private VcEntity identityVc;
    private VcEntity issuanceVc;
    private VcEntity verificationVc;

    @BeforeEach
    void setUp() throws Exception {
        vcEntityRepository.deleteAllInBatch();
        dataStoreEntityRepository.deleteAllInBatch();
        // setup some vc entries with different vc types
        var id1 = dataStoreEntityRepository.save(datastoreEntity(ACTIVE)).getId();
        var id2 = dataStoreEntityRepository.save(datastoreEntity(ACTIVE)).getId();
        var id3 = dataStoreEntityRepository.save(datastoreEntity(ACTIVE)).getId();
        var id4 = dataStoreEntityRepository.save(datastoreEntity(ACTIVE)).getId();
        metadataVc = vcEntityRepository.save(vcMetadataV1(id1, DID));
        identityVc = vcEntityRepository.save(vcIdentityV1(id2, DID));
        issuanceVc = vcEntityRepository.save(vcIssuanceV1(id3, DID, "https://schema/dummy-example"));
        verificationVc = vcEntityRepository.save(vcVerificationV1(id4, DID, "https://schema/dummy-example"));
        restClient = RestClient.create("http://localhost:" + port);
    }

    @Test
    void getTrustStatementsForDid() {
        // GIVEN
        var uri = URI.create("/api/v1/truststatements/" + DID);
        // WHEN (a request to legacy metadata v1 endpoint)
        var result = restClient.get().uri(uri).retrieve().toEntity(JsonNode.class);
        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        var json = result.getBody();
        assertThat(json).isNotNull();
        assertThat(json.isArray()).isTrue();
        assertThat(json).hasSize(1);
        assertThat(json.get(0).asText()).isEqualTo(metadataVc.getRawVc());
    }

    @Test
    void getIdentityTrustStatementsForDid() {
        // GIVEN
        var uri = URI.create("/api/v1/truststatements/identity/" + DID);
        // WHEN
        var result = restClient.get().uri(uri).retrieve().toEntity(JsonNode.class);
        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        var json = result.getBody();
        assertThat(json).isNotNull();
        assertThat(json.isArray()).isTrue();
        assertThat(json).hasSize(1);
        assertThat(json.get(0).asText()).isEqualTo(identityVc.getRawVc());
    }

    @Test
    void getTrustStatementsForIssuanceVcSchema() {
        // GIVEN (a request to /issuance with url as parameter)
        var vcSchemaIdentifier = issuanceVc.getVcPayload().get("canIssue").asText();
        var uri = URI.create(
            "/api/v1/truststatements/issuance?vcSchemaId=" +
            URLEncoder.encode(vcSchemaIdentifier, StandardCharsets.UTF_8)
        );
        // WHEN
        var result = restClient.get().uri(uri).retrieve().toEntity(JsonNode.class);

        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        var json = result.getBody();
        assertThat(json).isNotNull();
        assertThat(json.isArray()).isTrue();
        assertThat(json).hasSize(1);
        assertThat(json.get(0).asText()).isEqualTo(issuanceVc.getRawVc());
    }

    @Test
    void getTrustStatementsForVerificationVcSchema() {
        // GIVEN (a request to /verification with url as parameter)
        var vcSchemaIdentifier = verificationVc.getVcPayload().get("canVerify").asText();
        var uri = URI.create(
            "/api/v1/truststatements/verification?vcSchemaId=" +
            URLEncoder.encode(vcSchemaIdentifier, StandardCharsets.UTF_8)
        );
        // WHEN
        var result = restClient.get().uri(uri).retrieve().toEntity(JsonNode.class);

        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        var json = result.getBody();
        assertThat(json).isNotNull();
        assertThat(json.isArray()).isTrue();
        assertThat(json).hasSize(1);
        assertThat(json.get(0).asText()).isEqualTo(verificationVc.getRawVc());
    }
}
