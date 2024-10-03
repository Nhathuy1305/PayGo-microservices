FROM golang:1.23-alpine AS builder

RUN mkdir /app

COPY . /app

WORKDIR /app

RUN CGO_ENABLED=0 go build -o notificationHandler ./cmd/

FROM golang:1.23-alpine

RUN mkdir /app

COPY --from=builder /app/notificationHandler /app
COPY . /app

CMD ["/app/notificationHandler"]
