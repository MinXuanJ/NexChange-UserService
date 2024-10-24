FROM openjdk:21-jdk

LABEL authors="NexChange"

ARG JAR_NAME=NexChange-UserService.jar
COPY target/${JAR_NAME} /usr/app/app.jar

WORKDIR /usr/app

ENTRYPOINT ["java", "-jar", "app.jar"]
