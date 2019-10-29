#!/usr/bin/env bash

set -e

source root/scripts/seeds.sh

if [ "$#" -ne 3 ]; then
	echo "Expected: <algorithm[NSGAII]> <problem[WindTurbineDesign|Water]> <replace[true|false]>"
	exit 1
fi

dir=$(pwd)
execute="bash $dir/root/scripts/addbatch.sh"

mvn package -T 1C -DskipTests

# read args
algorithm=$1
problem=$2
replace=$3 # execute and replace if result exists
host=$(hostname)

# set constants
runs=21
jar=target/HHCO4WTDOP-1.0-SNAPSHOT-jar-with-dependencies.jar
main=br.ufpr.inf.cbio.hhco4wtdop.runner.Main
javacommand="java -Duser.language=en -cp $jar -Xmx1g $main"

seed_index=0
for (( id = 0; id < $runs; id++ )); do
    # each objective, problem and independent run (id) uses a different seed
    seed=${seeds[$seed_index]}
    # different algorithms on the same problem instance uses the same seed
    output="$dir/root/experiment/$host/"
    file="$output/FUN$id.tsv"
    if [ ! -s $file ] || [ "$replace" = true ]; then
        params="-s $seed -id $id -P $output -a $algorithm -p $problem"
        $execute "$javacommand $params 2>> $algorithm.$seed.log"
    fi
    seed_index=$((seed_index+1))
done

rm -f job.log
