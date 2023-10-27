#FROM maven:3.8.2-eclipse-temurin-17 AS build
#COPY . .
#RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
COPY target/payment.jar payment.jar
ENTRYPOINT ["java","-jar","payment.jar"]