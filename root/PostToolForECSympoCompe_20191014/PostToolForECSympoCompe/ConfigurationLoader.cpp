#include "ConfigurationLoader.h"

ECSC::ConfigurationLoader::ConfigurationLoader()
{
}

ECSC::ConfigurationLoader::~ConfigurationLoader()
{
}

bool ECSC::ConfigurationLoader::execute(std::string filepath, Configuration& config)
{
	std::ifstream ifs(filepath);
	if (ifs.fail())
	{
		std::cerr << "Error: Can not load " << filepath << "." << std::endl;
		return false;
	}
	std::string str((std::istreambuf_iterator<char>(ifs)), std::istreambuf_iterator<char>());

	std::string err;
	auto json = json11::Json::parse(str, err);
	if (err.empty() == false)
	{
		std::cout << err << std::endl;
	}

	config.group_name = json["group name"].string_value();
	config.number_of_solutions = json["number of solutions"].int_value();
	config.number_of_generations = json["number of generations"].int_value();
	config.number_of_runs = json["number of runs"].int_value();
	config.digits_of_generation = json["digits of generation"].int_value();
	config.digits_of_run = json["digits of run"].int_value();
	config.number_of_objectives = json["number of objectives"].int_value();
	config.number_of_variables = json["number of variables"].int_value();
	config.number_of_constraints = json["number of constraints"].int_value();
	config.filepath = json["filepath"].string_value();
	config.run_id_pre = json["run id pre"].string_value();
	config.run_id_post = json["run id post"].string_value();
	config.variables_file_pre = json["variables file pre"].string_value();
	config.objectives_file_pre = json["objectives file pre"].string_value();
	config.constraints_file_pre = json["constraints file pre"].string_value();
	config.variables_file_post = json["variables file post"].string_value();
	config.objectives_file_post = json["objectives file post"].string_value();
	config.constraints_file_post = json["constraints file post"].string_value();
	config.header_of_output_file = json["header of output file"].string_value();

	auto& n_obj = config.number_of_objectives;

	if (json["reference point"].is_array() == true)
	{
		auto ref_point = json["reference point"].array_items();
		if (ref_point.size() != n_obj)
		{
			std::cout << "Error: Dimension of reference point is not matched with number of objectives." << std::endl;
			return false;
		}
		config.reference_point.resize(n_obj, 0, 0);
		for (int i = 0; i < n_obj; i++)
		{
			config.reference_point.objective(i) = std::stod(ref_point.at(i).dump());
		}
	}

	if (json["max point"].is_array() == true)
	{
		auto max_point = json["max point"].array_items();
		if (max_point.size() != n_obj)
		{
			std::cout << "Error: Dimension of max point is not matched with number of objectives." << std::endl;
			return false;
		}
		config.max_point.resize(n_obj, 0, 0);
		for (int i = 0; i < n_obj; i++)
		{
			config.max_point.objective(i) = std::stod(max_point.at(i).dump());
		}
	}

	if (json["min point"].is_array() == true)
	{
		auto min_point = json["min point"].array_items();
		if (min_point.size() != n_obj)
		{
			std::cout << "Error: Dimension of min point is not matched with number of objectives." << std::endl;
			return false;
		}
		config.min_point.resize(n_obj, 0, 0);
		for (int i = 0; i < n_obj; i++)
		{
			config.min_point.objective(i) = std::stod(min_point.at(i).dump());
		}
	}

	return true;
}
