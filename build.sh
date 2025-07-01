#!/bin/sh
set -e

cd "$(dirname "$0")"

# Step 1: Build the project
mvn -B clean package

# Step 2: Run the fat jar
JAR_FILE=$(find target -name '*jar-with-dependencies.jar' | head -n 1)
exec java -jar "$JAR_FILE" "$@"
