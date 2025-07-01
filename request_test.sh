#!/bin/bash

LOG_FILE="client_requests.log"
> "$LOG_FILE"  # Clear previous log

for i in {1..10}; do
  (
    start=$(date +%s%3N)  # Start time in milliseconds
    response=$(curl -s http://localhost:4000/)
    end=$(date +%s%3N)    # End time in milliseconds
    duration=$((end - start))
    timestamp=$(date "+%Y-%m-%d %H:%M:%S")

    echo "[$timestamp] Request $i | Time Taken: ${duration}ms | Response: $response" >> "$LOG_FILE"
  ) &
done

wait
echo "✅ All requests completed. Check '$LOG_FILE'."
