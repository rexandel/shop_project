# Простой вариант - собираем локально, в Docker только запускаем
# Для сборки локально выполните: ./gradlew build -x test
FROM debian:bullseye-slim

# Устанавливаем Java 17 из пакетов Debian
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    openjdk-17-jre-headless \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Копируем собранный jar (соберите локально: ./gradlew build -x test)
COPY build/libs/shop_project-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
