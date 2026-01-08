/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data.infrastructure.web.controller;

import ch.admin.bj.swiyu.registry.trust.data.service.*;
import io.micrometer.core.annotation.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ConditionalOnBooleanProperty(prefix = "features", value = "EIDARTFE_754_VC_SCHEMA")
@RestController
@RequestMapping("/api/v1/vc-schema")
@AllArgsConstructor
@Tag(name = "VC Schema", description = "Returns VC schema entries from the datastore.")
public class VcSchemaV1Controller {

    private final VcSchemaService vcSchemaService;

    @Timed
    @GetMapping(value = "/{*vcSchemaPath}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "IF-007.005 - Get vc schema by ID.", description = "Get vc schema by ID.")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = """
                Returns a single vc schema.
                | Representation | Spec |
                |-|-|
                | SD-JWT | [VC Type Metadata](https://datatracker.ietf.org/doc/html/draft-ietf-oauth-sd-jwt-vc-09#name-sd-jwt-vc-type-metadata)|
                """,
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "String"))
            ),
        }
    )
    public String getVcSchema(@PathVariable @NotBlank @Size(min = 2) String vcSchemaPath) {
        return this.vcSchemaService.getVcSchema(vcSchemaPath.substring(1));
    }
}
