#!/bin/sh

# TARGET = graal or jvm
TARGET=$1

docker build . -f Dockerfile.$TARGET -t onestore-$TARGET
echo
echo
echo "To run the docker container execute:"
echo "    $ docker run -p 8080:8080 -v <data_path>:/data onestore-"$TARGET
