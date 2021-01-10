#!/bin/bash

GIT=$(command -v git)
HEAD=$(command -v head)
CUT=$(command -v cut)

JAR_ARTIFACT="out/artifacts/Hajj_simulation_jar/Hajj-simulation.jar"

if [ -z $"GIT" ] || [ -z $"HEAD" ] || [ -z $"CUT" ]; then
	echo "Tools missing"
	exit 1
else
	tar -cvf Hajj-Simulation-$(git log | head -n1 | cut -d' ' -f2).tar.gz $JAR_ARTIFACT run.sh
fi
