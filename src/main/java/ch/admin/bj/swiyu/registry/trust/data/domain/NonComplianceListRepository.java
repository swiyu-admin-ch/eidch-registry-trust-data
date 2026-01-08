/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data.domain;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NonComplianceListRepository extends JpaRepository<NonComplianceList, UUID> {
    Optional<NonComplianceList> findTopByOrderByPublishedAtDesc();
}
