/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */
package ch.admin.bj.swiyu.registry.trust.data.common.features;

import jakarta.annotation.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.boot.context.properties.*;
import org.springframework.validation.annotation.*;

@Slf4j
@Data
@Validated
@ConfigurationProperties(prefix = "features")
public final class FeaturesProperties {

    @NotNull
    private Boolean eidartfe671672TrustProtocol;

    @NotNull
    private Boolean eidartfe754VcSchema;

    @NotNull
    private Boolean eidartfe1129NonCompliantActors;

    @PostConstruct
    public void logFeatureFlags() {
        log.info(
            """
            Following features are configured:
              EIDARTFE_671_672_TRUST_PROTOCOL:{}
              EIDARTFE_754_VC_SCHEMA:{}
              EIDARTFE_1129_NON_COMPLIANT_ACTORS:{}
            """,
            eidartfe671672TrustProtocol,
            eidartfe754VcSchema,
            eidartfe1129NonCompliantActors
        );
    }
}
