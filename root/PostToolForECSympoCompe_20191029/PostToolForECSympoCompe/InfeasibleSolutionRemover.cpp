#include "InfeasibleSolutionRemover.h"

ECSC::InfeasibleSolutionRemover::InfeasibleSolutionRemover()
{
}

ECSC::InfeasibleSolutionRemover::~InfeasibleSolutionRemover()
{
}

bool ECSC::InfeasibleSolutionRemover::execute(SolutionSet & solutions, Configuration& config)
{
	auto& n_con = config.number_of_constraints;
	for (int i = 0; i < solutions.number_of_solutions(); i++)
	{
		for (int j = 0; j < n_con; j++)
		{
			if (solutions.at(i).constraint(j) < 0.0)
			{
				solutions.remove(i);
				i--;
				break;
			}
		}
	}

	return true;
}
