# Используем официальный образ с Java
FROM openjdk:17-jdk AS build

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем файлы проекта в контейнер
COPY . .

# Делаем gradlew исполняемым
RUN chmod +x gradlew

# Сборка проекта
RUN ./gradlew build

# Указываем порт, который приложение будет слушать
EXPOSE 8080

# Запускаем приложение
CMD ["java", "-jar", "build/libs/teacher_app-1.0-SNAPSHOT.jar"]
