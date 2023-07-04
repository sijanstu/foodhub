#run jarfile from target/foodhub-0.0.1-SNAPSHOT.jar
FROM openjdk:18-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/foodhub-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} foodhub.jar
ENTRYPOINT ["java","-jar","/foodhub.jar"]