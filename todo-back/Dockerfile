FROM openjdk:25-slim-bullseye

WORKDIR /app

ADD todo-back-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java","-jar", "/app/app.jar"]