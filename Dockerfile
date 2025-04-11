# Stage 1: Build the JAR
FROM maven:3.9.5-eclipse-temurin-17-alpine as builder
COPY . .
RUN mvn clean package -DskipTests


# Stage 2: Run the JAR
FROM openjdk:17-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} electro-store.jar
ENTRYPOINT ["java", "-jar", "/electro-store.jar"]
