/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data.service;

import ch.admin.bj.swiyu.registry.trust.data.domain.TrustStatementFilter;
import ch.admin.bj.swiyu.registry.trust.data.domain.VcEntity;
import ch.admin.bj.swiyu.registry.trust.data.domain.VcEntityRepository;
import ch.admin.bj.swiyu.registry.trust.data.domain.datastore.DatastoreStatus;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class VcEntityService {

    private final VcEntityRepository vcEntityRepository;

    @Transactional(readOnly = true)
    public List<String> getTrustStatementsForDid(String did, Boolean filterActive) {
        // Note: without this restriction it would return also the statements
        // of newer types like TrustStatementIdentity. In order not to break
        // contracts, we stick to the old behavior. Once the mobile wallet has migrated and enforced
        // the new identity endpoint, we can remove this method completely
        var restrictedVct = List.of("TrustStatementMetadataV1");
        var filter = toFilter(filterActive, TrustStatementFilter.builder().did(did).vct(restrictedVct));
        return vcEntityRepository.search(filter.build()).stream().map(VcEntity::getRawVc).toList();
    }

    @Transactional(readOnly = true)
    public List<String> getIdentityTrustStatementsForDid(String did, Boolean filterActive) {
        var restrictedVct = List.of("TrustStatementIdentityV1");
        var filter = toFilter(filterActive, TrustStatementFilter.builder().did(did).vct(restrictedVct));
        return vcEntityRepository.search(filter.build()).stream().map(VcEntity::getRawVc).toList();
    }

    @Transactional(readOnly = true)
    public List<String> getTrustStatementsForIssuanceVcSchema(String vcSchemaIdentifier, Boolean activeOnly) {
        var filter = toFilter(activeOnly, TrustStatementFilter.builder().canIssue(vcSchemaIdentifier));
        return vcEntityRepository.search(filter.build()).stream().map(VcEntity::getRawVc).toList();
    }

    @Transactional(readOnly = true)
    public List<String> getTrustStatementsForVerificationVcSchema(String vcSchemaIdentifier, Boolean activeOnly) {
        var filter = toFilter(activeOnly, TrustStatementFilter.builder().canVerify(vcSchemaIdentifier));
        return vcEntityRepository.search(filter.build()).stream().map(VcEntity::getRawVc).toList();
    }

    private static TrustStatementFilter.TrustStatementFilterBuilder toFilter(
        Boolean activeOnly,
        TrustStatementFilter.TrustStatementFilterBuilder filter
    ) {
        if (Boolean.TRUE.equals(activeOnly)) {
            filter.maxNotBefore(Instant.now()).minExpiration(Instant.now()).status(DatastoreStatus.ACTIVE);
        }
        return filter;
    }
}
