FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Устанавливаем curl для healthcheck
RUN apk add --no-cache curl

# Копируем миграции
COPY db/migration /app/db/migration

# Копируем JAR приложения shop
COPY shop/build/libs/shop_project-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
