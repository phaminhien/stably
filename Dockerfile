FROM openjdk:8-jdk-alpine
COPY ./ /app
WORKDIR /app
RUN chmod +x /app/mvnw
ENTRYPOINT ["./mvnw","spring-boot:run"]