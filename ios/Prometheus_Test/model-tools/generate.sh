#!/bin/bash
#
echo "Generating java class for the model"

MYPATH=../src/main/resources/model/homescreen/SleepTileRemoving
NAME=SleepTileRemoving
echo Checking size of model
java -jar graphwalker-standalone.jar offline -f "$MYPATH".graphml -g Shortest_non_optimized -x -s edge_coverage:100 | wc -l
echo "Generating java class"
java  -jar graphwalker-standalone.jar Source -f "$MYPATH".graphml -t junit.template > "$NAME"API.java

