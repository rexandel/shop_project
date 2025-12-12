# Структура микросервисной архитектуры

## Иерархия проекта

```
shop_project/                      # Корневой проект (родительский Gradle)
├── settings.gradle                # Включает модули: shop и audit
├── build.gradle                   # Конфиг для многомодульного проекта
├── docker-compose.yml             # Оркестрация контейнеров
├── gradle/                        # Gradle wrapper
├── initdb.d/                      # SQL инициализация БД
├── test-requests/                 # JSON примеры для тестирования
│
├── shop/                          # МОДУЛЬ 1: Order Management Service
│   ├── build.gradle               # Dependencies и конфиг сборки shop
│   ├── Dockerfile                 # Docker образ shop сервиса
│   ├── gradlew                    # Gradle wrapper для shop
│   ├── gradlew.bat
│   ├── gradle/                    # Gradle wrapper properties
│   └── src/
│       └── main/
│           ├── java/
│           │   └── org/example/shop_project/
│           │       ├── ShopProjectApplication.java
│           │       ├── BLL/                  # Business Logic Layer
│           │       │   └── orders/
│           │       │       ├── models/
│           │       │       └── service/
│           │       ├── controllers/          # REST API endpoints
│           │       │   └── orders/
│           │       ├── DAL/                  # Data Access Layer
│           │       │   └── orders/
│           │       │       ├── entity/
│           │       │       ├── jdbc/
│           │       │       ├── model/
│           │       │       └── repository/
│           │       ├── DTO/                  # Data Transfer Objects
│           │       │   ├── requests/
│           │       │   └── responses/
│           │       ├── rabbit/               # RabbitMQ Producer
│           │       │   ├── RabbitMqProducer.java
│           │       │   └── message/
│           │       ├── validators/           # Валидация входных данных
│           │       ├── config/               # Spring конфигурация
│           │       │   ├── AppConfig.java
│           │       │   ├── RabbitMqConfig.java
│           │       │   ├── OpenApiConfig.java
│           │       │   └── properties/
│           │       └── common/               # Утилиты
│           │           └── JsonUtil.java
│           └── resources/
│               ├── application.yml          # Prod конфиг
│               ├── application-dev.yml      # Dev конфиг
│               └── db/migration/
│                   └── V1__init_order_tables.sql
│
└── audit/                         # МОДУЛЬ 2: Audit Logging Service
    ├── build.gradle               # Dependencies и конфиг сборки audit
    ├── Dockerfile                 # Docker образ audit сервиса
    ├── gradlew
    ├── gradlew.bat
    ├── gradle/
    └── src/
        └── main/
            ├── java/
            │   └── org/example/
            │       ├── AuditApplication.java
            │       └── audit_service/
            │           ├── OrderCreatedMessage.java
            │           ├── config/
            │           │   ├── AppConfig.java
            │           │   ├── RabbitMqConfig.java
            │           │   └── property/
            │           ├── DAL/
            │           │   └── AuditLogOrderDAO.java
            │           ├── DTO/
            │           │   └── AuditLogOrderRequest.java
            │           ├── model/
            │           │   └── AuditLogOrder.java
            │           ├── rabbit/
            │           │   └── consumer/     # RabbitMQ Consumer
            │           └── service/
            │               └── AuditLogService.java
            └── resources/
                ├── application-dev.yml
                ├── application.yml
                └── db/migration/
                    └── V1__init_log_tables.sql
```

---

## Описание модулей

### 📦 **shop** - Order Management Service (порт 8080)
**Назначение:** Управление заказами, основной бизнес-сервис

**Основные компоненты:**
- **OrderController** - REST API (`/api/v1/orders`)
  - `POST /batch-create` - создание заказов
  - `POST /query` - поиск и фильтрация заказов
- **OrderService** - бизнес-логика работы с заказами
- **OrderRepository** - JDBC и JPA доступ к заказам
- **RabbitMqProducer** - публикация событий о созданных заказах
- **RabbitMqConfig** - конфигурация очередей RabbitMQ

**Технологии:**
- Spring Boot 3.2.0
- Spring Data JPA
- Spring AMQP (RabbitMQ)
- Flyway (миграции БД)
- PostgreSQL
- Swagger/OpenAPI

---

### 📦 **audit** - Audit Logging Service (порт 8081)
**Назначение:** Логирование всех событий заказов в режиме реального времени

**Основные компоненты:**
- **AuditLogService** - сохранение логов в БД
- **RabbitMQ Consumer** - слушает события из очереди `oms.order.created`
- **AuditLogOrderDAO** - доступ к таблице логов аудита
- **RabbitMqConfig** - конфигурация слушателя

**Технологии:**
- Spring Boot 3.2.0
- Spring AMQP (RabbitMQ)
- Spring Data JPA
- PostgreSQL

---

## Сборка проекта

### Собрать оба модуля:
```bash
gradlew buildAll
```

### Собрать только shop:
```bash
gradlew :shop:build
# или с boot JAR:
gradlew :shop:bootJar
```

### Собрать только audit:
```bash
gradlew :audit:build
# или с boot JAR:
gradlew :audit:bootJar
```

---

## Docker Compose

Запуск всей инфраструктуры:
```bash
docker-compose up
```

**Сервисы:**
- `postgres:5432` - основная БД (публичный порт 5433)
- `pgbouncer:5432` - connection pool (публичный порт 15432)
- `rabbitmq:5672` - message broker (управление на http://localhost:15672)
- `app:8080` - shop сервис
- `audit:8081` - audit сервис

---

## Flow событий

```
1. Клиент отправляет POST /api/v1/orders/batch-create
2. shop сохраняет заказы в orders и order_items таблицы
3. shop публикует OrderCreatedMessage в RabbitMQ (очередь: oms.order.created)
4. audit слушает эту очередь
5. audit получает событие и сохраняет логи в audit_log_order таблицу
```

---

## Конфигурация

Оба модуля используют одну БД `Marketplace` с таблицами:
- `orders` - заказы
- `order_items` - позиции заказов  
- `audit_log_order` - логи аудита

**Профили:**
- `dev` - локальное подключение (localhost:5432)
- `prod` - подключение через PGBouncer (localhost:15432)
