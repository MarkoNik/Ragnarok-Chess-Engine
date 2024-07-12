#!/bin/bash

# Check if the correct number of arguments is provided
if [ "$#" -lt 2 ]; then
    echo "Usage: $0 <depth> <fen> [moves]"
    exit 1
fi

# Extract arguments
DEPTH=$1
FEN=$2

# Navigate to the directory containing the JAR file
cd "./out/artifacts/my_project_jar"
ls

# Create the input string for the Java program
INPUT="position $FEN\ngo perft $DEPTH"

# Run the Java program and pass the input via stdin
echo $INPUT | java -jar my-project.jar -cp out app.Main
