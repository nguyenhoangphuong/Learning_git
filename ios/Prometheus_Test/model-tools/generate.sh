#!/bin/bash
#
echo "Generating java class for the model"

MODEL_HOME=../src/main/resources/model/

graph_path=$2
MYPATH=$MODEL_HOME$graph_path

NAME=$1

echo Checking size of model
java -jar graphwalker-standalone.jar offline -f "$MYPATH".graphml -g Shortest_non_optimized -x -s edge_coverage:100 | wc -l

echo "Generating java class"
java  -jar graphwalker-standalone.jar Source -f "$MYPATH".graphml -t junit.template > "$NAME"API.java
