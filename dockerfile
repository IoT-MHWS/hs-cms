FROM openjdk:17-alpine

RUN mkdir -p /app/services

WORKDIR /app

COPY "build/libs/hs-cms-*.jar" application.jar

COPY "services/" services

ENTRYPOINT ["java", "-jar", "application.jar"]
