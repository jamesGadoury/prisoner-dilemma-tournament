FROM gradle:8.6.0-jdk21-alpine

WORKDIR /usr/app
COPY . .
RUN ./gradlew build
RUN tar -xvf ./build/distributions/prisoner-dilemma-tournament-1.0-SNAPSHOT.tar

ENTRYPOINT [ "./prisoner-dilemma-tournament-1.0-SNAPSHOT/bin/prisoner-dilemma-tournament" ]
