FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /build

COPY . .

RUN mvn clean install

FROM openjdk:17-jdk-slim AS runner

WORKDIR /root

COPY --from=builder /build/target/kafka-listener-service-0.0.1-SNAPSHOT.jar .

CMD ["java", "-jar", "kafka-listener-service-0.0.1-SNAPSHOT.jar"]