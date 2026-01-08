/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */
package ch.admin.bj.swiyu.registry.trust.data.domain;

import java.util.*;
import org.springframework.data.jpa.repository.*;

public interface VcSchemaEntityRepository extends JpaRepository<VcSchema, Long> {
    Optional<VcSchema> findByPath(String path);
}
