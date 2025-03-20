FROM bellsoft/liberica-openjdk-alpine:21
WORKDIR /app
COPY ./build/libs/event-service.jar /app/event-service.jar
ENTRYPOINT ["java", "-jar", "event-service.jar"]