FROM openjdk:17-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} electro-store.jar
ENTRYPOINT ["java", "-jar", "/electro-store.jar"]