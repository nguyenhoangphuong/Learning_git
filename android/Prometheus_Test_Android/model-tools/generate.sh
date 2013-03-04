#!/bin/bash
echo "Generating java class for the model"


NAME=SignIn
echo Checking size of model
java -jar graphwalker-standalone.jar offline -f "$NAME".graphml -g Shortest_non_optimized -x -s edge_coverage:100 | wc -l
echo "Generating java class"
java -jar graphwalker-standalone.jar Source -f "$NAME".graphml -t junit.template > "$NAME"API.java
 
			
