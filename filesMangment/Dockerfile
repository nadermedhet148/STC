FROM openjdk:17-jdk-slim

ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS

ADD entrypoint.sh entrypoint.sh
ADD target/filesMangment-*.jar /app/service.jar

RUN chmod 755 ./entrypoint.sh

ENTRYPOINT ./entrypoint.sh
