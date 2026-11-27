FROM eclipse-temurin:21-jre-noble
WORKDIR /app

RUN groupadd --system spring && \
    useradd --system --gid spring --home-dir /app spring

COPY --chown=spring:spring target/notification-service-*.jar app.jar

USER spring:spring

EXPOSE 8070
ENTRYPOINT ["java", "-XX:+ExitOnOutOfMemoryError", "-jar", "/app/app.jar"]
