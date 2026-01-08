/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VcEntityRepository extends JpaRepository<VcEntity, Long>, VcEntitySearchRepository {}
