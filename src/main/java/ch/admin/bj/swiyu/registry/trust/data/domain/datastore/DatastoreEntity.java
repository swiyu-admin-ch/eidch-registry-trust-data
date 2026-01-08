/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data.domain.datastore;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A DatastoreEntity is the generic anchor for different files which are managed by this datastore.
 * <p>
 * It allows for unified handling of the most common management actions which we want to perform on our stored data.
 * For example: Deleting or deactivation of entries.
 */
@Entity
@Getter
@Table(name = "datastore_entity")
@NoArgsConstructor // JPA
public class DatastoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private DatastoreStatus status;

    // for test purposes only
    public DatastoreEntity(DatastoreStatus status) {
        this.status = status;
    }
}
