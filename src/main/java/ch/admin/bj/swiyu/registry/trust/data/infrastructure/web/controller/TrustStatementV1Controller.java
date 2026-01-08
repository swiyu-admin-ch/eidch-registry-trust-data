/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data.infrastructure.web.controller;

import ch.admin.bj.swiyu.registry.trust.data.service.*;
import io.micrometer.core.annotation.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.*;
import jakarta.validation.constraints.*;
import java.util.*;
import lombok.*;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.web.bind.annotation.*;

@ConditionalOnProperty(prefix = "features", value = "EIDARTFE_671_672_TRUST_PROTOCOL", havingValue = "true")
@RestController
@RequestMapping("/api/v1/truststatements")
@AllArgsConstructor
@Tag(name = "Trust Statement", description = "Returns Trust Statement VC entries from the datastore.")
public class TrustStatementV1Controller {

    private final VcEntityService vcEntityService;

    /**
     * @deprecated With trust protocol version 1.0 there are now specific endpoints for identity, verification and issuance trust statements.
     */
    @Timed
    @GetMapping(value = "/{did}")
    @Operation(
        summary = "IF-007.001 - Get vc entries for a given DID.",
        description = "Get vc entries for a given DID."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Returns a list of encoded trust statement VCs",
                useReturnTypeSchema = true
            ),
        }
    )
    @Deprecated(since = "1.2.0")
    public List<String> getTrustStatementsForDid/* NOSONAR */(
        @PathVariable(name = "did") String did, // swagger is handling the path encoding
        @RequestParam(defaultValue = "true") Boolean filterActive
    ) {
        return this.vcEntityService.getTrustStatementsForDid(did, filterActive);
    }

    @Timed
    @GetMapping(value = "/identity/{did}")
    @Operation(
        summary = "IF-007.002 - Get identity trust statement entries for a given DID.",
        description = "Get identity trust statement entries for a given DID."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Returns a list of encoded trust statement VCs",
                useReturnTypeSchema = true
            ),
        }
    )
    public List<String> getIdentityTrustStatementsForDid(
        @Parameter(description = "The URL-encoded DID.") @PathVariable(name = "did") String did, // swagger is handling the path encoding
        @RequestParam(defaultValue = "true") Boolean filterActive
    ) {
        return this.vcEntityService.getIdentityTrustStatementsForDid(did, filterActive);
    }

    @Timed
    @GetMapping(value = "/issuance")
    @Operation(
        summary = "IF-007.003 - Get issuance trust statement entries for a given vc schema identifier.",
        description = "Get issuance trust statement entries for a given vc schema identifier."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Returns a list of encoded trust statement VCs",
                useReturnTypeSchema = true
            ),
        }
    )
    public List<String> getTrustStatementsForIssuanceVcSchema(
        @Parameter(description = "The URL-encoded VC schema identifier.") @NotBlank @RequestParam(
            name = "vcSchemaId"
        ) String vcSchemaId,
        @RequestParam(defaultValue = "true") Boolean filterActive
    ) {
        return this.vcEntityService.getTrustStatementsForIssuanceVcSchema(vcSchemaId, filterActive);
    }

    @Timed
    @GetMapping(value = "/verification")
    @Operation(
        summary = "IF-007.004 - Get verification trust statement entries for a given vc schema identifier.",
        description = "Get issuance trust statement entries for a given vc schema identifier."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Returns a list of encoded trust statement VCs",
                useReturnTypeSchema = true
            ),
        }
    )
    public List<String> getTrustStatementsForVerificationVcSchema(
        @Parameter(description = "The URL-encoded VC schema identifier.") @NotBlank @RequestParam(
            name = "vcSchemaId"
        ) String vcSchemaId,
        @RequestParam(defaultValue = "true") Boolean filterActive
    ) {
        return this.vcEntityService.getTrustStatementsForVerificationVcSchema(vcSchemaId, filterActive);
    }
}
