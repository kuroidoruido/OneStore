#!/bin/sh

# TARGET = graal or jvm
TARGET=$1

docker build . -f Dockerfile.$TARGET -t onestore-$TARGET
echo
echo
echo "To run the docker container execute:"
echo "    $ docker run -name onestore -p 8080:8080 -v ${PWD}/data:/data -d onestore-"$TARGET
