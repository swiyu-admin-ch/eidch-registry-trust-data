/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */
package ch.admin.bj.swiyu.registry.trust.data.domain;

import jakarta.persistence.*;
import java.util.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.*;

@Entity
@NoArgsConstructor // JPA
@AllArgsConstructor // Tests
@Getter
@Table(name = "vc_schema")
@EntityListeners(AuditingEntityListener.class)
public class VcSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "path")
    private String path;

    @Column(name = "file")
    private String file;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private VcSchemaStatus status;
}
