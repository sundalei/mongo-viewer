FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src

FROM build AS test
RUN mvn verify

FROM build AS package
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre-jammy AS final
RUN addgroup --system spring && adduser --system --ingroup spring springuser

# Copy the entrypoint script and make it executable (as root)
COPY --chown=springuser:spring entrypoint.sh /usr/local/bin/entrypoint.sh
RUN chmod +x /usr/local/bin/entrypoint.sh

USER springuser
WORKDIR /app

ARG JAR_FILE=target/*.jar
COPY --from=package --chown=springuser:spring /app/${JAR_FILE} app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s \
  CMD curl -f http://localhost:8080/hello || exit 1

# Set the entrypoint to our new script
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]

CMD ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
