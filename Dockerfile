FROM openjdk:11.0.10-jre-slim-buster
VOLUME [ "/data" ]
WORKDIR /app
EXPOSE 8080
COPY ./target/ebgsys-0.0.1-SNAPSHOT.jar .
ENTRYPOINT [ "java", "-jar", "ebgsys-0.0.1-SNAPSHOT.jar" ]