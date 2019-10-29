#!/bin/bash

# if any error stop!
set -e

# validate input size
if [ "$#" -ne 35 ]; then
	echo -e "Expected <alg> <id> <fe> <v_1> ... <v_32> "
    echo -e "\talg: algorithm name"
    echo -e "\tid: independent run id"
    echo -e "\tfe: fitness evaluation index (C style)"
    echo -e "\tv_i: variable values separated by space"
	exit 1
fi

# read input
alg=$1; shift
id=$1; shift
id=$(printf "%03d" $id)
fe=$1; shift
fe=$(printf "%04d" $fe)
vars=( "$@" )

# create algorithm result folder
folder="root/experiment/"$alg"/interface/work_"$id"th"
mkdir -p $folder

# create vars file
echo ${vars[*]} | tr " " "\t" > $folder/pop_vars_eval.txt

# evaluate objs and cons
source root/jpnsecCompetition2019/bin/activate
python root/wisdem/windturbine_MOP.py $folder &> /dev/null

# output objs and cons
cat $folder/pop_objs_eval.txt
cat $folder/pop_cons_eval.txt

# rename output files
cd $folder
for file in pop_*; do
    mv $file "gen"$fe"_"$file
done
cd - &> /dev/null
