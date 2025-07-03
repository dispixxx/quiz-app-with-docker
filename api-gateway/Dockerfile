FROM maven:3.8.8-amazoncorretto-21 AS build
COPY pom.xml /build/
# Создаем рабочую директорию
WORKDIR /build/
RUN mvn dependency:go-offline
COPY src /build/src/
RUN mvn package -DskipTests

FROM openjdk:21-jdk-slim
ARG JAR_FILE=/build/target/*.jar
# Открываем порт, на котором работает приложение
EXPOSE 8765
COPY --from=build $JAR_FILE /opt/api-gateway/api-gateway-app.jar

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "/opt/api-gateway/api-gateway-app.jar"]
