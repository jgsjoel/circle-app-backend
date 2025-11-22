# Build stage
FROM golang:1.21-alpine AS builder

# Set environment variables
ENV CGO_ENABLED=0 GOOS=linux GOARCH=amd64

# Create app directory
WORKDIR /app

# Copy go.mod and go.sum first to leverage caching
COPY go.mod go.sum ./

# Download dependencies
RUN go mod download

# Copy source code
COPY . .

# Build the binary
RUN go build -o mqtt-service .

# Final stage
FROM alpine:latest

# Set working directory
WORKDIR /app

# Copy the built binary from the builder
COPY --from=builder /app/mqtt-service .

# Expose port 3000
EXPOSE 3000

# Run the service
CMD ["./mqtt-service"]
