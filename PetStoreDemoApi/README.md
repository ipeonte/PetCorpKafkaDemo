# Pet Store Demo API

## Build
mvn clean test
mvn clean install -P release

## Authentication & Authorization tests

|                 | ROLE_USER(0) | ROLE_PET_HANDLER(01) | ROLE_PET_MANAGER(01) | ANONYMOUS |
|-----------------|--------------|----------------------|----------------------|-----------|
|GET /pets        |         PASS |                 PASS |                 PASS |       401 |
|GET /all_pets    |          403 |                 PASS |                 PASS |       401 |
|GET /pet/{id}    |         PASS |                 PASS |                 PASS |       401 |
|POST /pet        |          403 |                 PASS |                 PASS |       401 |
|DELETE /pet/{id} |          403 |                  403 |                 PASS |       401 |
|UI Login         |          YES |                  YES |                  YES |      ERROR|
