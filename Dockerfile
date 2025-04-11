# Stage 1: Build the JAR
FROM maven:3.9.5-eclipse-temurin-17-alpine as builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the JAR
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar electronic-store.jar
ENTRYPOINT ["java", "-Xmx256m", "-Xms128m", "-jar", "electronic-store.jar"]
#ENTRYPOINT ["java", "-jar", "electronic-store.jar"]