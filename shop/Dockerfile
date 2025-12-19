FROM eclipse-temurin:21-jre

WORKDIR /app

# Устанавливаем curl для healthcheck (если его нет, но обычно он есть или можно установить через apt-get)
# RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
# В eclipse-temurin:21-jre curl может отсутствовать, проверим.
# Лучше явно установить.
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Копируем миграции
COPY db/migration /app/db/migration

# Копируем JAR приложения shop
COPY shop/build/libs/shop_project-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
