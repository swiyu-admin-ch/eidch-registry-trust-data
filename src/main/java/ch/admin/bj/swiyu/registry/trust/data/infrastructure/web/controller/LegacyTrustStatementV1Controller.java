/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data.infrastructure.web.controller;

import ch.admin.bj.swiyu.registry.trust.data.service.VcEntityService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

/**
 * @deprecated The only method getVCEntries was moved to {@link TrustStatementV1Controller} in version 1.2.0.
 */
@ConditionalOnProperty(
    prefix = "features",
    value = "EIDARTFE_671_672_TRUST_PROTOCOL",
    havingValue = "false",
    matchIfMissing = true
)
@Deprecated(since = "1.2.0")
@RestController
@RequestMapping("/api/v1/truststatements")
@AllArgsConstructor
@Tag(name = "Trust Statement", description = "Returns Trust Statement VC entries from the datastore.")
public class LegacyTrustStatementV1Controller /* NOSONAR */{

    private final VcEntityService vcEntityService;

    @Timed
    @GetMapping(value = "/{did}")
    @Operation(
        summary = "IF-007.001 - Get a vc entry from the datastore.",
        description = "Get a vc entry from the datastore."
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
    public List<String> getVCEntries(
        @PathVariable(name = "did") String did, // swagger is handling the path encoding
        @RequestParam(defaultValue = "true") Boolean filterActive
    ) {
        return this.vcEntityService.getTrustStatementsForDid(did, filterActive);
    }
}
