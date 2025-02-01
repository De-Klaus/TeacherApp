# Используем официальный образ с Java
FROM openjdk:17-jdk AS build

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем файлы проекта в контейнер
COPY . .

# Делаем gradlew исполняемым (если файл gradlew существует)
RUN chmod +x gradlew

# Устанавливаем необходимые зависимости для Gradle (если нужно)
RUN apt-get update && apt-get install -y curl

# Устанавливаем Gradle (если это необходимо, если gradlew не используется)
# RUN curl -sSL https://get.gradle.org/download/gradle-7.5-bin.zip -o gradle.zip
# RUN unzip gradle.zip && rm gradle.zip

# Сборка проекта
RUN ./gradlew build

# Указываем порт, который приложение будет слушать
EXPOSE 8080

# Запускаем приложение
CMD ["java", "-jar", "build/libs/teacher_app-1.0-SNAPSHOT.jar"]
