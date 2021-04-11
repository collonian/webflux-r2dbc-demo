#build stage
#FROM gradle:jdk-alpine as build-stage
FROM openjdk:11-jdk as build-stage

WORKDIR /home/gradle/project

ENV GRADLE_USER_HOME /home/gradle/project

COPY . /home/gradle/project

RUN chmod +x gradlew
RUN ./gradlew build

#production stage
FROM openjdk:11-jre

WORKDIR /home/gradle/project

EXPOSE 8080

COPY --from=build-stage /home/gradle/project/build/libs/project-0.0.1-SNAPSHOT.jar .

ENTRYPOINT java $JAVA_OPTS -jar project-0.0.1-SNAPSHOT.jar
