# Используем официальный образ OpenJDK для запуска Java-приложений
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# Копируем собранный .jar файл в контейнер
COPY target/operation-server-1.0-SNAPSHOT.jar app.jar

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]

# Указываем порт, на котором будет работать приложение (по умолчанию)
EXPOSE 4444
