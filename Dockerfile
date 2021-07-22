FROM openjdk:11-jre-slim

RUN apt-get update && apt-get install -y curl

RUN mkdir -p /opt/helidon

ARG JAR_FILE=socks-shop.jar
COPY target/${JAR_FILE} /opt/helidon/application.jar
COPY target/libs /opt/helidon/libs

HEALTHCHECK --start-period=10s --timeout=60s --retries=10 --interval=5s CMD curl -f http://localhost:8080/health/ready || exit 1

ENV JAVA_OPTS="-Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses=true"

EXPOSE 8080
ENTRYPOINT exec java -jar $JAVA_OPTS /opt/helidon/application.jar