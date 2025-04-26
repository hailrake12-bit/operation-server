# Используем официальный образ OpenJDK для запуска Java-приложений
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# Копируем собранный .jar файл в контейнер
COPY target/operation-server-1.0-SNAPSHOT.jar app.jar

# Копируем questions.txt в контейнер (в директорию /app)
COPY src/main/resources/questions.txt /app/questions.txt
COPY src/main/resources/books.txt /app/books.txt

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]

# Указываем порт, на котором будет работать приложение (по умолчанию)
EXPOSE 4444
