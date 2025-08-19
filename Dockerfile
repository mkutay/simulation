# Use Eclipse Temurin as the builder (more secure and maintained)
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Create lib directory first to avoid errors if it doesn't exist
RUN mkdir -p lib

# Copy project source and dependencies
COPY src/ ./src/
COPY lib/*.jar ./lib/
COPY src/simulation_data.json ./simulation_data.json

# Compile the Java sources. 
# Adjust the classpath, output directory, and source files as needed.
RUN mkdir out && javac -d out -cp "./lib/*" $(find src -name "*.java")

# Final stage: use a minimal JRE image for runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Install curl for health checks
RUN apk add --no-cache curl

# Create a non-root user for security
RUN addgroup -g 1001 -S appgroup && \
    adduser -S appuser -u 1001 -G appgroup

# Copy compiled classes and the lib folder from builder stage
COPY --from=builder /app/out/ ./out/
COPY --from=builder /app/lib/ ./lib/
COPY --from=builder /app/simulation_data.json ./simulation_data.json

# Change ownership to the non-root user
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose the WebSocket port
EXPOSE 8080

# Add health check for the WebSocket server
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/ || exit 1

# Run in headless mode with server optimizations
CMD ["java", \
  "-Djava.awt.headless=true", \
  "-server", \
  "-XX:+UseContainerSupport", \
  "-XX:MinRAMPercentage=50", \
  "-XX:MaxRAMPercentage=80", \
  "-Dfile.encoding=UTF-8", \
  "-Duser.timezone=UTC", \
  "-cp", "./out:./lib/*", \
  "Main"]
