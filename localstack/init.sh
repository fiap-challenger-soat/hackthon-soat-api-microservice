#!/bin/bash

# Cria uma fila no sqs e um bucket no s3
aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name video-queue
aws --endpoint-url=http://localhost:4566 s3api create-bucket --bucket meus-videos