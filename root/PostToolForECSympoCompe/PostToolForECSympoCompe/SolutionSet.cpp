#include "SolutionSet.h"

ECSC::SolutionSet::SolutionSet()
{
	_capacity = 0;
	_number_of_solutions = 0;
}

ECSC::SolutionSet::SolutionSet(int capacity)
{
	resize_capacity(capacity);
}

ECSC::SolutionSet::~SolutionSet()
{
	destroy();
}

void ECSC::SolutionSet::destroy()
{
	std::vector<Solution>().swap(_solution_set);
	_capacity = 0;
	_number_of_solutions = 0;
}

void ECSC::SolutionSet::erase(int index)
{
	_solution_set.erase(_solution_set.begin() + index);
	--_capacity;
	--_number_of_solutions;
}

void ECSC::SolutionSet::resize_capacity(int capacity)
{
	_capacity = capacity;
	_number_of_solutions = 0;
	_solution_set.resize(_capacity);
}

void ECSC::SolutionSet::set_solutions(int n_obj, int n_var, int n_con)
{
	for (int i = 0; i < _capacity; i++)
	{
		_solution_set.at(i).resize(n_obj, n_var, n_con);
	}
}

void ECSC::SolutionSet::set_solutions(int n, int n_obj, int n_var, int n_con)
{
	_number_of_solutions = n;
	for (int i = 0; i < n; i++)
	{
		_solution_set.at(i).resize(n_obj, n_var, n_con);
	}
}

bool ECSC::SolutionSet::remove(int index)
{
	if (index >= _capacity || index >= _number_of_solutions)
	{
		std::cout << "SolutionSet: Error: index is out of bound." << std::endl;
		return false;
	}
	for (auto i = _solution_set.begin() + index; i != _solution_set.begin() + _number_of_solutions - 1; i++)
	{
		auto next = (i + 1);
		*i = *next;
	}
	_number_of_solutions--;
}

//bool ECSC::SolutionSet::remove_until_last(int index)
//{
//	if (index > _number_of_solutions)
//	{
//		std::cout << "SolutionSet.remove: Error: index is out of bound." << std::endl;
//		return false;
//	}
//	_number_of_solutions = index;
//	return true;
//}

bool ECSC::SolutionSet::add(int index, Solution solution)
{
	if (index >= _capacity)
	{
		std::cout << "SolutionSet: Error: index is out of bound." << std::endl;
		return false;
	}
	if (index >= _number_of_solutions)
	{
		return add(solution);
	}

	_solution_set.at(index) = solution;

	return true;
}

bool ECSC::SolutionSet::add(Solution solution)
{
	if (_number_of_solutions == _capacity)
	{
		std::cout << "SolutionSet: Error: SolutionSet is full." << std::endl;
		return false;
	}
	
	_solution_set.at(_number_of_solutions) = solution;
	_number_of_solutions++;

	return true;
}

bool ECSC::SolutionSet::push_back(Solution solution)
{
	if (_number_of_solutions < _capacity)
	{
		return add(solution); // return true;
	}
	else if (_number_of_solutions == _capacity)
	{
		_solution_set.push_back(solution);
		_capacity++;
		_number_of_solutions++;

		return true;
	}

	std::cout << "SolutionSet: Error: Can not push-back." << std::endl;
	return false;
}

void ECSC::SolutionSet::join(SolutionSet solutions)
{
	int size_a = solutions.number_of_solutions();
	for (int i = 0; i < size_a; i++)
	{
		push_back(solutions.at(i));
	}
}
