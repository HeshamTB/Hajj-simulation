#!/bin/bash

jav=$(command -v java)

if [ -z "$jav" ]; then
	>&2 echo "Can't find java installation"
else
	java -jar out/artifacts/Hajj_simulation_jar/Hajj-simulation.jar
fi
