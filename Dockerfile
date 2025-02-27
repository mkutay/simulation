# Use an OpenJDK image as the builder
FROM openjdk:21-jdk AS builder
WORKDIR /app

# Copy project source, libraries, and env files
COPY src/ ./src/
COPY lib/ ./lib/
COPY .env .env

# Compile the Java sources. 
# Adjust the classpath, output directory, and source files as needed.
RUN mkdir out && javac -d out -cp "./lib/*" $(find src -name "*.java")

# Final stage: use a JRE image for runtime
FROM openjdk:21-slim
WORKDIR /app

# Copy compiled classes and the lib folder from builder stage
COPY --from=builder /app/out/ ./out/
COPY --from=builder /app/lib/ ./lib/
COPY --from=builder /app/.env .env

# Run the Main class from the root package
CMD ["java", "-cp", "./out:./lib/*", "Main"]
