#include "Solution.h"



ECSC::Solution::Solution()
{
	_number_of_objectives = 0;
	_number_of_variables = 0;
	_number_of_constraints = 0;
}

ECSC::Solution::Solution(int number_of_objectives, int number_of_variables, int number_of_constraints)
{
	_number_of_objectives  = number_of_objectives;
	_number_of_variables   = number_of_variables;
	_number_of_constraints = number_of_constraints;
	_objectives.resize(_number_of_objectives);
	_variables.resize(_number_of_variables);
	_constraints.resize(_number_of_constraints);
}

ECSC::Solution::~Solution()
{
	destroy();
}

void ECSC::Solution::destroy()
{
	std::vector<double>().swap(_objectives);
	std::vector<double>().swap(_variables);
	std::vector<double>().swap(_constraints);
	_number_of_objectives  = 0;
	_number_of_variables   = 0;
	_number_of_constraints = 0;
}

void ECSC::Solution::resize(int number_of_objectives, int number_of_variables, int number_of_constraints)
{
	_number_of_objectives = number_of_objectives;
	_number_of_variables = number_of_variables;
	_number_of_constraints = number_of_constraints;
	_objectives.resize(_number_of_objectives);
	_variables.resize(_number_of_variables);
	_constraints.resize(_number_of_constraints);
}
