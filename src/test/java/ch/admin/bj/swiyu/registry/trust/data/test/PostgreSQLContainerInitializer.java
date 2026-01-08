/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */
package ch.admin.bj.swiyu.registry.trust.data.test;

import lombok.extern.slf4j.*;
import org.jetbrains.annotations.*;
import org.springframework.context.*;
import org.springframework.test.context.support.*;
import org.testcontainers.containers.*;
import org.testcontainers.utility.*;

@Slf4j
public class PostgreSQLContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static PostgreSQLContainer<?> database;

    private static PostgreSQLContainer<?> getDatabase() {
        if (database == null) {
            database = new PostgreSQLContainer<>(
                DockerImageName.parse("docker-hub.nexus.bit.admin.ch/postgres:15.8").asCompatibleSubstituteFor(
                    "postgres:15.8"
                )
            );
            database.start();
        }
        return database;
    }

    public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
            configurableApplicationContext,
            "spring.datasource.url=" + getDatabase().getJdbcUrl(),
            "spring.datasource.username=" + getDatabase().getUsername(),
            "spring.datasource.password=" + getDatabase().getPassword()
        );
    }
}
