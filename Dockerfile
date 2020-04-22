FROM openjdk:8-jdk-alpine
COPY ./ /app
WORKDIR /app
ENTRYPOINT ["./mvnw","spring-boot:run"]