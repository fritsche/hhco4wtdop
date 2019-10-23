#include "SolutionNormalizer.h"



ECSC::SolutionNormalizer::SolutionNormalizer()
{
}


ECSC::SolutionNormalizer::~SolutionNormalizer()
{
}

bool ECSC::SolutionNormalizer::execute(SolutionSet & solutions, Configuration & config)
{
	auto& n_obj = config.number_of_objectives;
	for (int i = 0; i < solutions.number_of_solutions(); i++)
	{
		auto& s = solutions.at(i);
		for (int j = 0; j < n_obj; j++)
		{
			auto& min = config.min_point.objective(j);
			auto& max = config.max_point.objective(j);
			s.objective(j) = (s.objective(j) - min) / (max - min);
		}
	}
	return true;
}
