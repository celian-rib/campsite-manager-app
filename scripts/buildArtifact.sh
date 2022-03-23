#!/bin/bash

echo "Starting project build script"

echo "Downloading javafx"
curl https://download2.gluonhq.com/openjfx/18/openjfx-18_linux-x64_bin-sdk.zip > javafx-sdk.zip

echo "Unzipping"
tar -xf javafx-sdk.zip
mv javafx-sdk-* javafx-sdk

mvn clean package -DskipTests
