-- used by application-local.yml
-- background: we are having a custom schema name (data) and the schema is not present on startup
-- in the database
CREATE SCHEMA IF NOT EXISTS data;