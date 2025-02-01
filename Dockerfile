# Используем официальный образ с Java
FROM openjdk:22-jdk

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем файлы проекта в контейнер
COPY . .

# Делаем gradlew исполняемым
RUN chmod +x gradlew

# Сборка проекта
RUN ./gradlew build

# Запускаем приложение (замените на свою команду)
CMD ["java", "-jar", "build/libs/teacher_app-1.0-SNAPSHOT.jar"]
