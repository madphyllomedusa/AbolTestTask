# core/Dockerfile
FROM eclipse-temurin:21

# Установка рабочей директории
WORKDIR /app

# Копирование собранного JAR файла в контейнер
COPY target/core-0.0.1-SNAPSHOT.jar /app/core.jar

# Указание порта, на котором будет работать сервис
EXPOSE 8080

# Команда для запуска приложения
ENTRYPOINT ["sh", "-c", "java -jar core.jar"]
