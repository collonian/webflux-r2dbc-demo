#build stage
#FROM gradle:jdk-alpine as build-stage
FROM openjdk:11-jdk as build-stage

WORKDIR /home/gradle/project

ENV GRADLE_USER_HOME /home/gradle/project

COPY gradlew build.gradle settings.gradle ./
RUN chmod +x gradlew && ./gradlew build || return 0 

COPY . .
RUN chmod +x gradlew && ./gradlew build


#production stage
FROM openjdk:11-jre

WORKDIR /home/gradle/project

EXPOSE 8080

COPY --from=build-stage /home/gradle/project/build/libs/webflux-r2dbc-demo-1.0-SNAPSHOT.jar .

ENTRYPOINT java $JAVA_OPTS -jar webflux-r2dbc-demo-1.0-SNAPSHOT.jar
