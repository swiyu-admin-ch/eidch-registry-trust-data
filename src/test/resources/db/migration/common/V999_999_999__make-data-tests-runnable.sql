/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */
-- note: this script does remove fields required in the business service which are ommited in the data part of the registry
ALTER TABLE datastore_entity
    ALTER COLUMN created_at DROP NOT NULL,
    ALTER COLUMN created_by DROP NOT NULL,
    ALTER COLUMN last_modified_at DROP NOT NULL,
    ALTER COLUMN last_modified_by DROP NOT NULL;

ALTER TABLE vc_entity
    ALTER COLUMN created_at DROP NOT NULL,
    ALTER COLUMN created_by DROP NOT NULL,
    ALTER COLUMN last_modified_at DROP NOT NULL,
    ALTER COLUMN last_modified_by DROP NOT NULL;

ALTER TABLE vc_schema
    ALTER COLUMN created_at DROP NOT NULL,
    ALTER COLUMN created_by DROP NOT NULL,
    ALTER COLUMN last_modified_at DROP NOT NULL,
    ALTER COLUMN last_modified_by DROP NOT NULL;

ALTER TABLE non_compliance_list
    ALTER COLUMN created_at DROP NOT NULL,
    ALTER COLUMN created_by DROP NOT NULL,
    ALTER COLUMN last_modified_at DROP NOT NULL,
    ALTER COLUMN last_modified_by DROP NOT NULL;


