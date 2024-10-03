FROM golang:1.23-alpine AS builder

RUN mkdir /app

COPY . /app

WORKDIR /app

RUN CGO_ENABLED=0 go build -o goQueryService ./cmd/

FROM golang:1.23-alpine

RUN mkdir /app

COPY --from=builder /app/goQueryService /app
COPY . /app

EXPOSE 1903

CMD ["/app/goQueryService"]