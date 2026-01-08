/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */
package ch.admin.bj.swiyu.registry.trust.data.test;

import static java.util.Map.*;

import ch.admin.bj.swiyu.registry.trust.data.domain.*;
import ch.admin.bj.swiyu.registry.trust.data.domain.datastore.*;
import com.authlete.sd.*;
import com.fasterxml.jackson.databind.*;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jwt.*;
import java.security.*;
import java.security.interfaces.*;
import java.time.*;
import java.time.temporal.*;
import java.util.*;
import lombok.experimental.*;

@UtilityClass
public class VcEntityTestData {

    public static DatastoreEntity datastoreEntity(DatastoreStatus status) {
        return new DatastoreEntity(status);
    }

    public static VcEntity vcMetadataV1(UUID datastoreEntityId) throws Exception {
        return vcMetadataV1(
            datastoreEntityId,
            "did:tdw:2EADBEEF0000000000000000000000000000000000000000000000000000000000000000000000000000000000000:identifier-data-service-d.bit.admin.ch:api:v1:did:%s".formatted(
                    datastoreEntityId
                )
        );
    }

    public static VcEntity vcMetadataV1(UUID datastoreEntityId, String did) throws Exception {
        var jwt = createSignedJwt(
            did,
            "TrustStatementMetadataV1",
            Map.of(
                "prefLang",
                "de-CH",
                "orgName",
                Map.of(
                    "en",
                    "Example Organization",
                    "de-CH",
                    "Beispielorganisation",
                    "fr-CH",
                    "Exemple d'organisation",
                    "it-CH",
                    "Organizzazione di esempio",
                    "rm-CH",
                    "organisaziun exemplarica"
                )
            )
        );
        return vcEntity(datastoreEntityId, jwt);
    }

    public static VcEntity vcIssuanceV1(UUID datastoreEntityId, String subject, String canIssue) throws Exception {
        var jwt = createSignedJwt(
            subject,
            "TrustStatementIssuanceV1",
            Map.of("prefLang", "de-CH", "canIssue", canIssue)
        );
        return vcEntity(datastoreEntityId, jwt);
    }

    public static VcEntity vcVerificationV1(UUID datastoreEntityId, String subject, String canVerify) throws Exception {
        var jwt = createSignedJwt(
            subject,
            "TrustStatementVerificationV1",
            Map.of("prefLang", "de-CH", "canVerify", canVerify)
        );
        return vcEntity(datastoreEntityId, jwt);
    }

    public static VcEntity vcIdentityV1(UUID datastoreEntityId, String subject) throws Exception {
        var jwt = createSignedJwt(
            subject,
            "TrustStatementIdentityV1",
            Map.of(
                "entityName",
                Map.of(
                    "en",
                    "Example Organization",
                    "de-CH",
                    "Beispielorganisation",
                    "fr-CH",
                    "Exemple d'organisation",
                    "it-CH",
                    "Organizzazione di esempio",
                    "rm-CH",
                    "organisaziun exemplarica"
                ),
                "isStateActor",
                true
            )
        );
        return vcEntity(datastoreEntityId, jwt);
    }

    private static VcEntity vcEntity(UUID datastoreEntityId, SignedJWT jwt) {
        var sdjwt = createSdjwt(jwt);
        return new VcEntity(datastoreEntityId, sdjwt.getCredentialJwt(), toJsonNode(jwt.getPayload()));
    }

    private static SDJWT createSdjwt(SignedJWT jwt) {
        var disclosures = List.of(
            new Disclosure("Hc72Ej5a6_HeyrBC03VifOIx2S_VBJP9606zXfcLlEo"),
            new Disclosure("QcI7xo0NUPyzyJdNiELTurUR-IP3NE3YV9ZuTruBi0c"),
            new Disclosure("efNvKsCzkTDNT8CYk4GSoZXvOPLka9b-_t8e1amVxq0"),
            new Disclosure("k0NQ-QFui9PXZ0UR9ZZH7LVAMNDG9-XqbkSF0ZLZwvY")
        );
        return new SDJWT(jwt.serialize(), disclosures);
    }

    private static SignedJWT createSignedJwt(String subject, String vct, Map<String, Object> customClaims)
        throws Exception {
        var validFrom = Instant.now();
        var validTo = validFrom.plus(1, ChronoUnit.DAYS);
        var claims = new HashMap<>(
            Map.ofEntries(
                entry("vct", vct),
                entry("_sd_alg", "sha-256"),
                entry(
                    "iss",
                    "did:tdw:QmbF3pVGdkZkpZCmVhV2cntPAKcRUrdtq7Qrc2RgE6GUgr:identifier-reg-d.trust-infra.swiyu.admin.ch:api:v1:did:8a4c76f7-c255-41dd-ad87-f05daddd5a07"
                ),
                entry("exp", validTo.getEpochSecond()), // expires
                entry("nbf", validFrom.getEpochSecond()), // not before
                entry("iat", validFrom.getEpochSecond()), // issued at
                entry("sub", subject),
                entry(
                    "status",
                    Map.of(
                        "status_list",
                        Map.of(
                            "type",
                            "SwissTokenStatusList-1.0",
                            "uri",
                            "https://status-reg-d.trust-infra.swiyu.admin.ch/api/v1/statuslist/0075c371-c9b4-4182-b07c-3007568afc4c.jwt",
                            "idx",
                            326
                        )
                    )
                )
            )
        );
        claims.putAll(customClaims);
        var claimsSet = JWTClaimsSet.parse(claims);

        var jwt = new SignedJWT(
            new JWSHeader.Builder(JWSAlgorithm.ES256).type(new JOSEObjectType("vc+sd-jwt")).keyID("kid-1234").build(),
            claimsSet
        );
        var signer = new ECDSASigner(generateECKey());
        jwt.sign(signer);
        return jwt;
    }

    private static ECKey generateECKey() throws Exception {
        // Generate a key pair using the P-256 curve
        var keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(Curve.P_256.toECParameterSpec());
        var keyPair = keyPairGenerator.generateKeyPair();

        // Build the ECKey using the generated key pair
        return new ECKey.Builder(Curve.P_256, (ECPublicKey) keyPair.getPublic())
            .privateKey((ECPrivateKey) keyPair.getPrivate())
            .keyID("example-key-id")
            .build();
    }

    private static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper
            .getFactory()
            .configure(com.fasterxml.jackson.core.json.JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true);
        return mapper;
    }

    private static JsonNode toJsonNode(Payload payload) {
        return getObjectMapper().valueToTree(payload.toJSONObject());
    }
}
