FROM openjdk:25-slim-bullseye
WORKDIR /app

ADD broadcaster-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java","-jar", "/app/app.jar"]