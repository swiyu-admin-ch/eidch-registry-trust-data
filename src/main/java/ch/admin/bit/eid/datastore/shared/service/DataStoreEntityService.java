/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bit.eid.datastore.shared.service;

import ch.admin.bit.eid.datastore.shared.exceptions.ResourceNotFoundException;
import ch.admin.bit.eid.datastore.shared.exceptions.ResourceNotReadyException;
import ch.admin.bit.eid.datastore.shared.model.entity.DatastoreEntity;
import ch.admin.bit.eid.datastore.shared.model.enums.DatastoreStatusEnum;
import ch.admin.bit.eid.datastore.shared.repository.DataStoreEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class DataStoreEntityService {

    private final DataStoreEntityRepository dataStoreEntityRepository;

    public void checkCanEdit(DatastoreEntity entry) throws ResourceNotReadyException {
        if (entry.getStatus() == DatastoreStatusEnum.DISABLED) throw new ResourceNotReadyException(
                entry.getId().toString(),
                DatastoreEntity.class
        );
    }

    public void checkCanShow(DatastoreEntity entry) throws ResourceNotReadyException {
        this.checkCanEdit(entry);
        if (
                !(entry.getStatus() != DatastoreStatusEnum.SETUP && entry.getStatus() != DatastoreStatusEnum.DEACTIVATED)
        ) throw new ResourceNotReadyException(entry.getId().toString(), DatastoreEntity.class);
    }

    public DatastoreEntity getDataStoreEntity(UUID id) {
        return this.dataStoreEntityRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id.toString(), DatastoreEntity.class)
        );
    }
}
