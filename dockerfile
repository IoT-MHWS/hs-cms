FROM openjdk:17-alpine

COPY "build/libs/hs-cms-*.jar" application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]
