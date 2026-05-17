# AGENTS.md
Rules for AI coding agents working in the PureSound backend codebase.

This file should stay stable over time. Prefer durable rules and decision principles over snapshots of the current folder tree or exhaustive file inventories.

## Project Rules
- Treat this project as a Java 21 Spring Boot 3.5 backend for a music streaming product.
- Use Gradle through the included wrapper for builds, tests, and application tasks.
- Keep the existing package root `com.puresound.backend`.
- Prefer existing project patterns over introducing new abstractions, libraries, or folder conventions.
- Keep changes scoped to the requested feature or fix.
- Do not edit unrelated files as part of opportunistic cleanup.
- Do not add new dependencies unless the task clearly requires them and existing Spring Boot, MapStruct, Lombok, or project utilities cannot solve the problem.
- Do not weaken security, validation, transaction, or type boundaries to make a change easier.

## Code Placement Rules
- Put REST controllers under `src/main/java/com/puresound/backend/api`.
- Put OpenAPI endpoint documentation helpers under `src/main/java/com/puresound/backend/api_docs`.
- Put application and integration configuration under `src/main/java/com/puresound/backend/config`.
- Put request and response DTOs under `src/main/java/com/puresound/backend/dto`.
- Put JPA entities under `src/main/java/com/puresound/backend/entity/jpa`.
- Put Redis entities or cache models under `src/main/java/com/puresound/backend/entity/redis`.
- Put JPA repositories under `src/main/java/com/puresound/backend/repository/jpa`.
- Put Redis repositories under `src/main/java/com/puresound/backend/repository/redis`.
- Put business service interfaces and implementations under `src/main/java/com/puresound/backend/service`.
- Put MapStruct mappers and decorators under `src/main/java/com/puresound/backend/mapper`.
- Put Spring Security, JWT, OAuth2, cookie, converter, and auth-event code under `src/main/java/com/puresound/backend/security`.
- Put reusable constants and enums under `src/main/java/com/puresound/backend/constant`.
- Put shared utility classes under `src/main/java/com/puresound/backend/util`.
- Put environment-specific YAML config under `src/main/resources`.
- Put API and validation translations under `src/main/resources/i18n`.
- Before creating a new folder, check whether the nearest existing module boundary already fits.
- Create a new folder only when the feature has a clear domain boundary that does not fit an existing module.

## API & Response Rules
- Do not put business logic in controllers.
- Controllers should validate input, call services, and return typed `ResponseEntity<ApiResponse<T>>` responses.
- Use `ApiResponseFactory` to create API responses.
- Preserve the `ApiResponse<T>` wrapper convention for successful and error responses.
- Use `ApiMessage` constants for response messages instead of hardcoded message strings.
- Keep endpoint paths versioned under `/api/v1` unless the feature explicitly requires a separate surface.
- Use `@Valid` on request bodies that have Jakarta validation annotations.
- Keep request and response shapes explicit with DTO records or classes.
- Do not return JPA entities directly from controllers.
- Do not expose raw exception messages, stack traces, secrets, or internal infrastructure details in API responses.

## Service & Transaction Rules
- Keep business rules in service interfaces and their `Default...Service` implementations.
- Prefer constructor injection through Lombok `@RequiredArgsConstructor` and final fields.
- Use `@Transactional` for write flows or multi-step persistence changes that must commit atomically.
- Keep read-only service methods free of accidental writes.
- Keep service methods focused on one domain responsibility.
- Coordinate cross-domain behavior through service dependencies instead of reaching into unrelated repositories from controllers.
- Do not duplicate business rules across controllers, services, and repositories.
- Avoid returning persistence entities from service methods when a DTO is the established boundary.

## Persistence Rules
- Use Spring Data JPA repositories for relational persistence.
- Use Redis repositories or cache services for Redis-backed listener collection, token, OTP, weather, and session-style data.
- Keep custom JPQL queries inside repository interfaces.
- Use `@Modifying` for repository update/delete queries.
- Prefer repository methods and focused queries over ad hoc persistence logic in services.
- Keep entity relationships, audit fields, and status transitions consistent with existing entities.
- Do not bypass repositories with raw JDBC or manual SQL unless the task explicitly requires it.
- Do not change schema-sensitive entity fields casually; consider migrations, existing data, and API compatibility.

## Mapping Rules
- Use MapStruct for DTO/entity mapping.
- Use `GlobalMapperConfig` for mapper configuration.
- Use mapper decorators only when mapping needs non-trivial enrichment or derived data.
- Keep manual mapping in services minimal and local to business-specific values.
- Do not hide database queries or service calls inside simple mapper methods.
- Keep response DTOs shaped for API consumers rather than leaking persistence implementation details.

## Validation & i18n Rules
- Use Jakarta Bean Validation annotations for request validation.
- Use message keys such as `{EMAIL_NOT_BLANK}` for validation messages.
- Add or update both `messages.properties` and `messages_vi.properties` when adding user-visible API or validation messages.
- Add new API message keys to `ApiMessage` before using them in responses or exceptions.
- Keep validation that depends only on request shape in DTOs.
- Keep validation that depends on persistence, authentication, or business state in services.
- Do not duplicate regex or validation rules across unrelated DTOs if an existing constant or helper fits.

## Security Rules
- Keep auth, JWT, OAuth2, CORS, cookie, and authentication-provider behavior under `security` and related config classes.
- Do not bypass `SecurityConfig` or `BypassSecurity` conventions for protected endpoints.
- Do not hardcode tokens, secrets, cookie names, OAuth credentials, or callback URLs in code.
- Use `JwtTokenProvider`, `CookieService`, and existing authentication tokens/providers for auth flows.
- Keep refresh-token, stream-token, blacklist, and stream-session behavior in the established security and token services.
- Do not add public endpoints without checking whether they belong in `BypassSecurity.PUBLIC_ENDPOINTS`.
- Do not weaken CORS, CSRF, session, or resource-server settings unless the task explicitly requires it.
- Avoid logging sensitive values such as passwords, OTPs, tokens, cookies, authorization headers, or credentials.

## Configuration & External Services Rules
- Put durable defaults in `application.yml`.
- Put environment-specific values in `application-dev.yml`, `application-staging.yml`, or `application-prod.yml`.
- Keep secrets in environment variables or `.env.*` files, not hardcoded Java or YAML literals.
- Preserve existing integration boundaries for MySQL, Redis, MinIO, Cloudinary, mail, Nominatim, WeatherAPI, and OAuth providers.
- Use existing config beans for HTTP clients, object storage, JSON, JPA, Redis, and OpenAPI.
- Do not introduce a new external service client when an existing service/config boundary already fits.
- Keep Docker, staging, and production deployment files aligned with application profile expectations.

## Error Handling Rules
- Route expected domain failures through `ApplicationException` subclasses such as `BadRequestException`, `UnauthorizedException`, `ConflictException`, and `NotFoundException`.
- Attach the appropriate `ApiMessage` and `LogLevel` to expected application exceptions.
- Let `GlobalExceptionHandler` translate application, validation, auth, and unexpected errors into `ApiResponse<T>`.
- Do not catch and silently swallow exceptions unless the fallback behavior is explicit and intentional.
- Do not expand production-facing debug logs.
- Prefer structured, useful logs that avoid sensitive request data.
- Keep unexpected failures as unexpected; do not mask real bugs with broad fallback responses in feature code.

## Testing Rules
- Put tests under `src/test/java` using JUnit Platform and Spring Boot test conventions.
- Add focused tests for new business rules, validation behavior, repository queries, and security-sensitive flows.
- Prefer service-level tests for business behavior and controller tests for request/response contracts.
- Keep tests deterministic and independent of production infrastructure.
- Do not require real external services for normal automated tests unless the task explicitly sets up test containers or mocks.
- Run `./gradlew test` or `gradlew.bat test` when changes affect Java behavior.
- Documentation-only changes do not require runtime tests.

## Implementation Flow Rules
When adding a backend-backed API feature:

1. Add or update constants and API message keys in `constant`.
2. Add or update request/response DTOs in `dto`.
3. Add or update entities and repositories only if persistence changes are required.
4. Add or update MapStruct mappers when crossing entity/DTO boundaries.
5. Add or update service interfaces and `Default...Service` implementations for business logic.
6. Add or update REST controllers in `api`.
7. Add or update translations in `messages.properties` and `messages_vi.properties`.
8. Add focused tests for the changed behavior.

When adding a persistence-backed behavior:

1. Confirm whether the data belongs in JPA, Redis, or an existing cache/service boundary.
2. Add repository methods or cache operations at the data-access boundary.
3. Keep transaction boundaries in services.
4. Map entities to DTOs before returning data through API layers.
5. Preserve existing audit, status, and ownership conventions.

When adding an auth or streaming behavior:

1. Reuse existing JWT, cookie, token, and stream-session services.
2. Keep endpoint access rules aligned with `SecurityConfig` and `BypassSecurity`.
3. Avoid duplicating token parsing, cookie writing, or blacklist logic.
4. Add tests or targeted verification for unauthorized, expired, and missing-token cases.

## Anti-Patterns To Avoid
- Business logic inside REST controllers.
- Returning JPA entities directly from APIs.
- Hardcoded response strings instead of `ApiMessage`.
- Missing i18n entries for new API or validation messages.
- Raw, untyped API responses.
- New ad hoc response wrappers.
- New ad hoc authentication, JWT, cookie, or OAuth2 code.
- Public endpoints added without checking security conventions.
- Secrets or environment-specific values committed in source code.
- Repository access from controllers.
- Broad `catch` blocks that hide real failures.
- Silent exception swallowing.
- Production-facing debug logs or sensitive log values.
- Manual DTO/entity mapping when an existing MapStruct mapper fits.
- Unnecessary new dependencies or framework changes.
- Large unrelated refactors mixed into feature work.
- Folder creation based only on preference instead of a clear domain boundary.
