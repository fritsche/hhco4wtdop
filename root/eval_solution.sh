
if [ "$#" -ne 8 ]; then
	echo -e "Expected <alg> <id> <fe> <v_1> ... <v_32> "
    echo -e "\talg: algorithm name"
    echo -e "\tid: independent run id"
    echo -e "\tfe: fitness evaluation index (C style)"
    echo -e "\tv_i: variable values separated by space"
	exit 1
fi
