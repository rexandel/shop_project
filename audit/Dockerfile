FROM eclipse-temurin:21-jre

WORKDIR /app

# Устанавливаем curl для healthcheck
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Копируем миграции
COPY db/migration /app/db/migration

# Копируем JAR аудит сервиса
COPY audit/build/libs/audit-service.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]