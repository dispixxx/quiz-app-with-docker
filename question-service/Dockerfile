FROM maven:3.8.8-amazoncorretto-21 AS build
COPY pom.xml /build/
# Создаем рабочую директорию
WORKDIR /build/
RUN mvn dependency:go-offline
COPY src /build/src/
RUN mvn package -DskipTests

#Копируем JAR-файл в контейнер
#COPY target/question-service-0.0.1-SNAPSHOT.jar question-service-app.jar

# Используем официальный образ OpenJDK
FROM openjdk:21-jdk-slim
ARG JAR_FILE=/build/target/*.jar
# Открываем порт, на котором работает приложение
EXPOSE 8080
COPY --from=build $JAR_FILE /opt/question-service/question-service-app.jar

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "/opt/question-service/question-service-app.jar"]