# Используем официальный образ OpenJDK
FROM openjdk:21-jdk-slim

# Создаем рабочую директорию
WORKDIR /app

# Копируем JAR-файл в контейнер
COPY target/serviceRegistryEureka-0.0.1-SNAPSHOT.jar eureka-server-app.jar

# Открываем порт, на котором работает приложение
EXPOSE 8761

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "eureka-server-app.jar"]