#!/bin/bash

if [ "$#" -ne 2 ]; then
        echo "Expected backup <origin> and <destination>"
        exit 1
fi

from=$1
to=$2

while true; do
	rsync -amv --include='gen*_pop_*_eval.txt' --include='*/' --exclude='*' $from/ $to/ --remove-source-files
	rsync -amv --include='*.tsv' --include='*/' --exclude='*' $from/ $to/ --remove-source-files
        sleep 600
done
