FROM gradle:8.6.0-jdk21

WORKDIR /usr/app
COPY . .
RUN ./gradlew build

ENTRYPOINT [ "./gradlew", "run" ]