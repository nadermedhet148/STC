#!/bin/bash

echo "Building for latest version"

./mvnw clean install

docker build -t files_management .

docker tag files_management files_management:latest
