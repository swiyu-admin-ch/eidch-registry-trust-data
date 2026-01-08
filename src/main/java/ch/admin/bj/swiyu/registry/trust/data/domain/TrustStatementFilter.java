/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */
package ch.admin.bj.swiyu.registry.trust.data.domain;

import ch.admin.bj.swiyu.registry.trust.data.domain.datastore.*;
import java.time.*;
import java.util.*;
import lombok.*;

@Builder
@Getter
public class TrustStatementFilter {

    private String did;
    private Instant maxNotBefore;
    private Instant minExpiration;
    private DatastoreStatus status;
    private List<String> vct;
    private String canIssue;
    private String canVerify;
}
