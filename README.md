![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.2-brightgreen?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)
![License](https://img.shields.io/badge/License-MIT-yellow)
# Corporate Admin Panel

Админ-панель корпоративного сервиса на **Spring Boot 3**:
управление пользователями, ролями и правами доступа (RBAC),
JWT-аутентификация, аудит действий, продемонстрированы **SOLID** и паттерны GoF.

## Стек

- Java 21 (Liberica / Temurin)
- Spring Boot 3.3.2
- Spring Security + JWT (jjwt 0.12.6)
- Spring Data JPA / Hibernate 6.5
- PostgreSQL 16 (Docker)
- Lombok
- Maven

## Возможности

- JWT-аутентификация (`/api/auth/login`), проверка текущего пользователя (`/api/auth/me`)
- CRUD пользователей (`/api/users`)
- CRUD ролей (`/api/roles`)
- CRUD разрешений (`/api/permissions`)
- Назначение ролей пользователям и прав ролям
- Аудит действий (async Observer)
- Кэширование проверки прав (Decorator)
- Валидация связей через Chain of Responsibility
- Авторизация по правам через `@PreAuthorize` + authorities из JWT

## Архитектура и SOLID

| Принцип | Где применяется |
|---|---|
| **S**RP | `UserService`, `RoleService`, `PermissionService` — каждый про своё |
| **O**CP | Новые стратегии проверки прав добавляются без правок существующих |
| **L**SP | Все `PermissionEvaluationStrategy` взаимозаменяемы |
| **I**SP | Узкие интерфейсы сервисов вместо «god-interface» |
| **D**IP | Сервисы зависят от интерфейсов (`UserRepository`, `PasswordEncoder`), не от реализаций |

## Паттерны проектирования

| Паттерн | Класс |
|---|---|
| **Strategy** | `pattern.strategy.PermissionEvaluationStrategy` + `RbacEvaluationStrategy` |
| **Factory** | `pattern.strategy.PermissionStrategyFactory` |
| **Decorator** | `pattern.decorator.CachedPermissionService` |
| **Chain of Responsibility** | `pattern.chain.UserExistsValidator` → `RoleExistsValidator` |
| **Observer** | `pattern.observer.AuditEventPublisher` → `AuditLogListener` |
| **Facade** | `service.AccessControlFacade` |
| **Repository** | `domain.repository.*` |
| **DTO / Mapper** | `dto.*`, `mapper.*` |
| **Builder** | Lombok `@Builder` на сущностях |
| **Singleton** | Spring-бины (scope по умолчанию) |
| **Template Method** | `BaseEntity` + JPA `@EntityListeners` |

## Запуск

### 1. Поднять PostgreSQL

```bash
docker compose up -d
```

### 2. Запустить приложение

```bash
./mvnw spring-boot:run
```

Или в IntelliJ IDEA: **⌘ + R** (Run `AdminPanelApplication`).

Приложение стартует на `http://localhost:8080`.

При первом запуске `DataInitializer` создаст:
- пользователя `admin / admin123` с ролью `SUPER_ADMIN`
- 4 базовых permissions: `USER_READ`, `USER_WRITE`, `ROLE_MANAGE`, `PERMISSION_MANAGE`

## API

Все защищённые эндпоинты требуют заголовок `Authorization: Bearer <token>`.

### Аутентификация

| Метод | URL | Доступ | Описание |
|---|---|---|---|
| POST | `/api/auth/login` | публичный | Логин, возвращает JWT |
| GET | `/api/auth/me` | любой авторизованный | Текущий пользователь |

### Пользователи

| Метод | URL | Право |
|---|---|---|
| GET | `/api/users` | `USER_READ` |
| GET | `/api/users/{id}` | `USER_READ` |
| POST | `/api/users` | `USER_WRITE` |
| DELETE | `/api/users/{id}` | `USER_WRITE` |
| POST | `/api/users/{id}/roles/{roleId}` | `ROLE_MANAGE` |
| DELETE | `/api/users/{id}/roles/{roleId}` | `ROLE_MANAGE` |

### Роли

| Метод | URL | Право |
|---|---|---|
| GET | `/api/roles` | `ROLE_MANAGE` |
| GET | `/api/roles/{id}` | `ROLE_MANAGE` |
| POST | `/api/roles` | `ROLE_MANAGE` |
| DELETE | `/api/roles/{id}` | `ROLE_MANAGE` |
| POST | `/api/roles/{id}/permissions/{pid}` | `PERMISSION_MANAGE` |
| DELETE | `/api/roles/{id}/permissions/{pid}` | `PERMISSION_MANAGE` |

### Разрешения

| Метод | URL | Право |
|---|---|---|
| GET | `/api/permissions` | `PERMISSION_MANAGE` |
| GET | `/api/permissions/{id}` | `PERMISSION_MANAGE` |
| POST | `/api/permissions` | `PERMISSION_MANAGE` |
| DELETE | `/api/permissions/{id}` | `PERMISSION_MANAGE` |

## Примеры

### Логин и получение токена

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"admin","password":"admin123"}' | \
  python3 -c "import sys,json;print(json.load(sys.stdin)['token'])")
```

### Список пользователей

```bash
curl -s http://localhost:8080/api/users -H "Authorization: Bearer $TOKEN"
```

### Создать пользователя

```bash
curl -s -X POST http://localhost:8080/api/users \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"username":"alice","password":"alice123","fullName":"Alice","email":"alice@corp.local"}'
```

### Создать роль и дать ей право

```bash
curl -s -X POST http://localhost:8080/api/roles \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"name":"AUDITOR","description":"Read-only"}'

curl -s -X POST http://localhost:8080/api/roles/2/permissions/1 \
  -H "Authorization: Bearer $TOKEN"
```

## Модель прав

```
User  ──< user_roles >──  Role  ──< role_permissions >── Permission
```

JWT содержит список authorities (плоский набор permissions). При запросе
`JwtAuthenticationFilter` кладёт их в `SecurityContext` — далее `@PreAuthorize`
проверяет через `hasAuthority('CODE')`.

## Аудит

Все мутации публикуют `AuditEvent`, слушатель `AuditLogListener` (async)
пишет строку в `audit_log`. Бизнес-логика не знает о таблице аудита.

```bash
docker exec -it admin-pg psql -U postgres -d admin_panel \
  -c "SELECT actor, action, target, details, timestamp FROM audit_log ORDER BY id DESC LIMIT 10;"
```

## Структура

```
src/main/java/com/corporate/admin/
├── AdminPanelApplication.java
├── config/
│   ├── SecurityConfig.java
│   └── DataInitializer.java
├── security/
│   ├── JwtService.java
│   └── JwtAuthenticationFilter.java
├── domain/
│   ├── entity/       (BaseEntity, User, Role, Permission, AuditLog)
│   └── repository/   (User, Role, Permission, AuditLog)
├── dto/
│   ├── request/      (LoginRequest, CreateUserRequest, CreateRoleRequest, CreatePermissionRequest)
│   └── response/     (AuthResponse, UserResponse, RoleResponse, PermissionResponse, ApiError)
├── mapper/           (UserMapper, RoleMapper, PermissionMapper)
├── service/
│   ├── UserService.java, RoleService.java, PermissionService.java
│   ├── AccessControlFacade.java
│   └── impl/         (UserServiceImpl, RoleServiceImpl, PermissionServiceImpl)
├── pattern/
│   ├── strategy/     (PermissionEvaluationStrategy, RbacEvaluationStrategy, PermissionStrategyFactory)
│   ├── decorator/    (PermissionServiceDecorator, CachedPermissionService)
│   ├── chain/        (AccessValidator, AbstractAccessValidator, UserExistsValidator, RoleExistsValidator)
│   └── observer/     (AuditEvent, AuditEventPublisher, AuditLogListener)
├── controller/       (AuthController, UserController, RoleController, PermissionController)
└── exception/        (NotFoundException, ConflictException, AccessDeniedException, GlobalExceptionHandler)
```

## Технические решения

- **`@EnableJpaAuditing`** — автозаполнение `createdAt` / `updatedAt` в `BaseEntity`
- **`@EnableAsync`** — асинхронный аудит
- **`@EnableCaching`** — готовность к кэшированию (например, через Redis)
- **`@EnableMethodSecurity`** — работа `@PreAuthorize`
- **Stateless session** — JWT, никаких HTTP-сессий
- **BCrypt** — хэш паролей
- **`ddl-auto: update`** — для разработки (в проде нужно Flyway)

