# Build stage
FROM golang:1.24-alpine AS builder

WORKDIR /app

# Copy go.mod and go.sum first
COPY go.mod go.sum ./

# Download dependencies
RUN go mod download

# Copy source code
COPY . .

# Build the binary
RUN go build -o mqtt-service ./main.go

# Final stage
FROM alpine:latest

WORKDIR /app

COPY --from=builder /app/mqtt-service .

EXPOSE 3000

CMD ["./mqtt-service"]
