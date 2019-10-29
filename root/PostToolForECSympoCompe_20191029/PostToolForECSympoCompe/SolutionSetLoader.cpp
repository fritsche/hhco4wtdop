#include "SolutionSetLoader.h"

int ECSC::SolutionSetLoader::count_fileline(std::ifstream & ifs)
{
	int n_line = 0;
	std::string line_str;
	while (true)
	{
		getline(ifs, line_str);
		n_line++;
		if (ifs.eof())
		{
			break;
		}
	}

	ifs.clear();
	ifs.seekg(0, std::ios_base::beg);

	return n_line;
}

void ECSC::SolutionSetLoader::skip_header(std::ifstream & ifs, int n_header)
{
	std::string line_str;
	for (int line_count = 0; line_count < n_header; line_count++)
	{
		getline(ifs, line_str);
	}
}

ECSC::SolutionSetLoader::SolutionSetLoader()
{
}

ECSC::SolutionSetLoader::~SolutionSetLoader()
{
}

bool ECSC::SolutionSetLoader::execute(SolutionSet& solutions, Configuration& config, int n_run, int n_gen, int n_header)
{
	std::string filepath_obj = config.input_filepath_of_objective(n_run, n_gen);
	std::ifstream i_obj_file(filepath_obj);
	if (!i_obj_file)
	{
		std::cout << "SolutionSetLoader: Error: Can not read " << filepath_obj << "." << std::endl;
		return false;
	}

	std::string filepath_var = config.input_filepath_of_variable(n_run, n_gen);
	std::ifstream i_var_file(filepath_var);
	if (!i_var_file)
	{
		std::cout << "SolutionSetLoader: Error: Can not read " << filepath_var << "." << std::endl;
		return false;
	}

	std::string filepath_con = config.input_filepath_of_constraint(n_run, n_gen);
	std::ifstream i_con_file(filepath_con);
	if (!i_con_file)
	{
		std::cout << "SolutionSetLoader: Error: Can not read " << filepath_con << "." << std::endl;
		return false;
	}

	int n_line_obj_file = count_fileline(i_obj_file);
	int n_line_var_file = count_fileline(i_var_file);
	int n_line_con_file = count_fileline(i_con_file);
	if (n_line_obj_file != n_line_var_file ||
		n_line_obj_file != n_line_con_file ||
		n_line_var_file != n_line_con_file)
	{
		std::cout << "SolutionSetLoader: Error: number of lines in each files is missmached." << std::endl;
	}

	int n_line = n_line_obj_file;

	skip_header(i_obj_file, n_header);
	skip_header(i_var_file, n_header);
	skip_header(i_con_file, n_header);
	
	solutions.resize_capacity(config.number_of_solutions);
	auto& n_obj = config.number_of_objectives;
	auto& n_var = config.number_of_variables;
	auto& n_con = config.number_of_constraints;
	solutions.set_solutions(n_obj, n_var, n_con);
	
	for (int line_count = 0; line_count < config.number_of_solutions; line_count++)
	{
		Solution a_solution(n_obj, n_var, n_con);

		std::string line;
		std::string cell;

		getline(i_obj_file, line);
		std::stringstream stream_obj(line);
		for (int i = 0; i < n_obj; i++)
		{
			getline(stream_obj, cell, '\t');
			a_solution.objective(i) = std::stod(cell);
		}

		getline(i_var_file, line);
		std::stringstream stream_var(line);
		for (int i = 0; i < n_var; i++)
		{
			getline(stream_var, cell, '\t');
			a_solution.variable(i) = std::stod(cell);
		}

		getline(i_con_file, line);
		std::stringstream stream_con(line);
		for (int i = 0; i < n_con; i++)
		{
			getline(stream_con, cell, '\t');
			a_solution.constraint(i) = std::stod(cell);
		}

		solutions.add(a_solution);
	}

	i_obj_file.close();
	i_var_file.close();
	i_con_file.close();

	return true;
}

