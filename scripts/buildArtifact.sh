#!/bin/bash

# java --module-path "/Users/celian/Documents/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -jar ../target/flots-blancs-1.0.0.jar

LAUNCH_CMD="java --module-path"
MODULE_ARGS="--add-modules javafx.controls,javafx.fxml -jar"

echo "Starting project build script"

echo "Downloading javafx"
curl https://download2.gluonhq.com/openjfx/18/openjfx-18_linux-x64_bin-sdk.zip > javafx-sdk.zip

echo "Unzipping"
tar -xf javafx-sdk.zip
mv javafx-sdk-* javafx-sdk

mvn clean package -DskipTests

mkdir flotblancs
mv target/flots-blancs-1.0.0.jar flotblancs
