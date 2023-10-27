FROM eclipse-temurin:17-jdk-alpine
COPY target/payment.jar payment.jar
ENTRYPOINT ["java","-jar","payment.jar"]