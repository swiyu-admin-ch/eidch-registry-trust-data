/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableJpaRepositories
@EntityScan
@Slf4j
public class Application {

    public static void main(String[] args) {
        var app = SpringApplication.run(Application.class, args);
        var env = app.getEnvironment();
        var appName = env.getProperty("spring.application.name");
        var serverPort = env.getProperty("server.port");
        log.info(
            """

            ----------------------------------------------------------------------------
            \t'{}' is running!\s
            \tProfile(s): \t\t\t\t{}
            \tSwaggerUI:   \t\t\t\thttp://localhost:{}/swagger-ui.html
            ----------------------------------------------------------------------------""",
            appName,
            env.getActiveProfiles(),
            serverPort
        );
    }
}
