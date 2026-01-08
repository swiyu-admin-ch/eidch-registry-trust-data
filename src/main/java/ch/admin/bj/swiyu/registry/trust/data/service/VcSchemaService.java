/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */
package ch.admin.bj.swiyu.registry.trust.data.service;

import ch.admin.bj.swiyu.registry.trust.data.common.exception.*;
import ch.admin.bj.swiyu.registry.trust.data.domain.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;

@Slf4j
@Service
@AllArgsConstructor
public class VcSchemaService {

    private final VcSchemaEntityRepository vcSchemaEntityRepository;

    public String getVcSchema(String vcSchemaPath) throws ResourceNotFoundException, ResourceDeactivatedException {
        var vcSchema = vcSchemaEntityRepository
            .findByPath(vcSchemaPath)
            .orElseThrow(() -> new ResourceNotFoundException(vcSchemaPath, ResourceType.VC_SCHEMA));
        if (vcSchema.getStatus().compareTo(VcSchemaStatus.DEACTIVATED) == 0) {
            throw new ResourceDeactivatedException(vcSchemaPath, ResourceType.VC_SCHEMA);
        }

        return vcSchema.getFile();
    }
}
