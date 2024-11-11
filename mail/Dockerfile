# mail/Dockerfile
FROM eclipse-temurin:21
# Установка рабочей директории
WORKDIR /app

# Копирование собранного JAR файла в контейнер
COPY target/mail-0.0.1-SNAPSHOT.jar /app/mail.jar

# Указание порта, на котором будет работать сервис
EXPOSE 8081

# Команда для запуска приложения
ENTRYPOINT ["sh", "-c", "java -jar mail.jar"]
