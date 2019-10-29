#include "ParetoSolutionSelector.h"





int ECSC::ParetoSolutionSelector::comporse_dominance(Solution & si, Solution & sj, const int n_obj)
{
	int flag = 0;
	bool dominate_i = false;
	bool dominate_j = false;
	for (int k = 0; k < n_obj; k++)
	{
		auto& value1 = si.objective(k);
		auto& value2 = sj.objective(k);

		if (value1 < value2) {
			flag = -1;
		}
		else if (value1 > value2) {
			flag = 1;
		}
		else {
			flag = 0;
		}

		if (flag == -1) {
			dominate_i = true;
		}

		if (flag == 1) {
			dominate_j = true;
		}
	}

	if (dominate_i == dominate_j) {
		return 0; //No one dominate the other
	}
	if (dominate_i == true) {
		return -1; // solution i dominate
	}
	return 1;    // solution j dominate 
}

ECSC::ParetoSolutionSelector::ParetoSolutionSelector()
{
}

ECSC::ParetoSolutionSelector::~ParetoSolutionSelector()
{
}

bool ECSC::ParetoSolutionSelector::execute(SolutionSet & solutions, SolutionSet & pareto, Configuration & config)
{
	auto& n_obj = config.number_of_objectives;
	std::vector<bool> is_dominated(solutions.number_of_solutions(), false);

	for (int i = 0; i < solutions.number_of_solutions(); i++)
	{
		if (is_dominated.at(i) == true)
		{
			continue;
		}

		auto& si = solutions.at(i);

		for (int j = 0; j < solutions.number_of_solutions(); j++)
		{
			if (is_dominated.at(j) == true)
			{
				continue;
			}
			if (i == j) 
			{
				continue;
			}

			auto& sj = solutions.at(j);
			
			int flag = comporse_dominance(si, sj, n_obj);
			if (flag == -1)
			{
				is_dominated.at(j) = true;
			}
			if (flag == 1)
			{
				is_dominated.at(i) = true;
			}
		}

		if (is_dominated.at(i) == false)
		{
			pareto.push_back(solutions.at(i));
		}
	}

	return true;
}
