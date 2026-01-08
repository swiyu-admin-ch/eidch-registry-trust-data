# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## 1.4.2

### Changed

- Improved Exception Handling
- Upgraded to JEAP 27.4.1

## 1.4.1

### Changed

- Removed @Lob annotation on NonComplianceList since it leads to error "permission denied for large object"

## 1.4.0

### Added

- Added Non-Compliant-Actors API

## 1.3.2

### Changed

- Update maven from 3.9.10 to 3.9.11
- Update maven-wrapper from 3.3.2 to 3.3.4
- Update maven-wrapper from 3.3.2 to 3.3.4
- Update maven-wrapper from 3.3.2 to 3.3.4
- Update postgres from 15.13 to 15.14
- Update org.apache.maven.plugins:maven-compiler-plugin from 3.14.0 to 3.14.1
- Update org.apache.maven.plugins:maven-surefire-plugin from 3.5.2 to 3.5.4
- Update org.apache.tomcat.embed:tomcat-embed-core from 11.0.9 to 11.0.11
- Update org.bouncycastle:bcpkix-jdk18on from 1.81 to 1.82
- Update com.nimbusds:nimbus-jose-jwt from 10.3.1 to 10.5
- Update org.springdoc:springdoc-openapi-starter-common from 2.8.9 to 2.8.13
- Update org.springdoc:springdoc-openapi-starter-webmvc-ui from 2.8.9 to 2.8.13
- Update com.diffplug.spotless:spotless-maven-plugin from 2.45.0 to 3.0.0
- Update ch.admin.bit.jeap:jeap-spring-boot-parent from 26.68.0 to 27.2.0


## 1.3.1

### Changed

- add schema for response of getVcSchemaMethod in VcSchemaV1Controller

## 1.3.0

### Added

- Adding vc schema retrieval endpoint

## 1.2.4

### Changed

- adapted issuance and verification trust statement endpoints to use query param instead of path variable for vc schema
  identifier

## 1.2.3

### Changed

- Update maven from 3.9.9 to 3.9.10
- Update org.apache.tomcat.embed:tomcat-embed-core from 10.1.42 to 11.0.9
- Update org.bouncycastle:bcpkix-jdk18on from 1.80 to 1.81
- Update com.nimbusds:nimbus-jose-jwt from 9.40 to 10.3.1
- Update org.springdoc:springdoc-openapi-starter-common from 2.8.8 to 2.8.9
- Update org.springdoc:springdoc-openapi-starter-webmvc-ui from 2.8.8 to 2.8.9
- Update com.diffplug.spotless:spotless-maven-plugin from 2.44.4 to 2.45.0
- Update ch.admin.bit.jeap:jeap-spring-boot-parent from 26.64.2 to 26.68.0

## 1.2.2

### Changed

- fixed encoding issue in new trust statement REST APIs

## 1.2.1

### Changed

- FeatureFlag `EIDARTFE_671_672_TRUST_PROTOCOL` is now configurable via env variable
  `FEATURES_EIDARTFE_671_672_TRUST_PROTOCOL`
- improved local development database schema initialization

## 1.2.0

### Added

- Added new endpoints for querying trust statements by VCT (TrustStatementIdentityV1,TrustStatementIssuanceV1,
  TrustStatementVerificationV1).
  Previous Metadata V1 endpoint will be removed once mobile wallet is enforcing the new endpoints.

## 1.1.13

### Other

- Update Interface Summaries

## 1.1.12

### Changed

- Downgrade maven-sunfire-plugin due to archunit incompatibility

## 1.1.11

### Changed

- Update maven from 3.9.6 to 3.9.9
- Update postgres from 15.8 to 15.13
- Update org.apache.maven.plugins:maven-compiler-plugin from 3.12.1 to 3.14.0
- Update com.tngtech.archunit:archunit-junit5 from 1.4.0 to 1.4.1
- Update org.flywaydb:flyway-database-postgresql from 10.17.1 to 11.8.2
- Update org.flywaydb:flyway-core from 10.17.1 to 11.8.2
- Update com.h2database:h2 from 2.2.224 to 2.3.232
- Update net.logstash.logback:logstash-logback-encoder from 7.2 to 8.1
- Update org.springdoc:springdoc-openapi-starter-common from 2.8.5 to 2.8.8
- Update org.springdoc:springdoc-openapi-starter-webmvc-ui from 2.8.5 to 2.8.8
- Update org.projectlombok:lombok from 1.18.34 to 1.18.38
- Update org.postgresql:postgresql from 42.7.3 to 42.7.5
- Update ch.admin.bit.jeap:jeap-spring-boot-parent from 26.47.0 to 26.50.1

## 1.1.10

### Changed

- Internal test improvements and dependency updates

## 1.1.9

### Other

- Fixed major or critical sonar issues

## 1.1.8

### Other

- Added spotless plugin

## 1.1.7

### Changed

- internal improvements for local development

## 1.1.6

### Fixed

- Reverted VcType enum sonar issue fix as it was causing issues. Will be done later together with authoring service.

### Changed

- Using noop instead of bcrypt in prometheus password configuration in application.yml

## 1.1.5

### Changed

- Align entrypoint.sh with one from already refactored service. Replace STAGE with MY_SPRING_PROFILES

## 1.1.4

### Changed

- Set version of spring-security-crypto specifically to 6.4.4 to resolve vulnerability CVE-2025-22228

## 1.1.3

### Other Changes

- Refactored code structure to use JEAP, since it is now open-source

## 1.1.2

### Changed

- The data service now only returns active registry entries

## 1.1.1

### Changed

- Update springdoc-openapi-starter-webmvc-ui and springdoc-openapi-starter-common version 2.6.0 to 2.8.5

## 1.1.0

### Added

- Extending prometheus export with metrics for build

## 1.0.0

- Initial Release 
