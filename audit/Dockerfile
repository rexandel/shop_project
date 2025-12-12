FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Устанавливаем curl для healthcheck
RUN apk add --no-cache curl

# Копируем миграции
COPY db/migration /app/db/migration

# Копируем JAR аудит сервиса
COPY audit/build/libs/audit-service.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]