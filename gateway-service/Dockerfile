FROM eclipse-temurin:21-jre-noble

WORKDIR /app

RUN groupadd --system spring && \
    useradd --system --gid spring --home-dir /app spring

COPY --chown=spring:spring target/*.jar app.jar

USER spring:spring

EXPOSE 8876

ENTRYPOINT ["java", "-XX:+ExitOnOutOfMemoryError", "-jar", "/app/app.jar"]