#include "SolutionSetWriter.h"

ECSC::SolutionSetWriter::SolutionSetWriter()
{
}

ECSC::SolutionSetWriter::~SolutionSetWriter()
{
}

bool ECSC::SolutionSetWriter::execute(SolutionSet & solutions, const std::string& path, Configuration & config)
{
	std::ofstream ofs(path);

	auto& n_obj = config.number_of_objectives;
	auto& n_var = config.number_of_variables;
	auto& n_con = config.number_of_constraints;

	ofs << config.header_of_output_file << std::endl;

	for (int i = 0; i < solutions.number_of_solutions(); i++)
	{
		std::string line = "";
		double acv = 0;
		for (int j = 0; j < n_obj; j++)
		{
			line += std::to_string(solutions.at(i).objective(j));
			line += ",";
		}
		for (int j = 0; j < n_var; j++)
		{
			line += std::to_string(solutions.at(i).variable(j));
			line += ",";
		}
		for (int j = 0; j < n_con; j++)
		{
			line += std::to_string(solutions.at(i).constraint(j));
			acv += std::min(0.0, solutions.at(i).constraint(j));
			line += ",";
		}
		line += std::to_string(acv);
		ofs << line << std::endl;
	}

	ofs.close();

	return true;
}
