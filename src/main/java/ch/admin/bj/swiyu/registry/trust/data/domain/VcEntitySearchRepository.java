/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data.domain;

import java.util.List;

public interface VcEntitySearchRepository {
    List<VcEntity> search(TrustStatementFilter filter);
}
