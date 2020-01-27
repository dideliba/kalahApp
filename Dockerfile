#FROM openjdk:8-jre-alpine
FROM maven:3.5-jdk-8
VOLUME /tmp
#ARG DEPENDENCY=target/dependency
#COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY ${DEPENDENCY}/META-INF /app/META-INF
#COPY ${DEPENDENCY}/BOOT-INF/classes /app

ARG JAR_FILE
COPY ${JAR_FILE} kalah-0.0.1-SNAPSHOT.jar


EXPOSE 8080
ENTRYPOINT ["java","-XX:+UnlockExperimentalVMOptions","-XX:+UseCGroupMemoryLimitForHeap","-XX:MaxRAMFraction=1","-XshowSettings:vm","-jar","/kalah-0.0.1-SNAPSHOT.jar"]


