#include "Configuration.h"

ECSC::Configuration::Configuration()
{
	initialize();
}

ECSC::Configuration::~Configuration()
{
}

void ECSC::Configuration::initialize()
{
	group_name = "who";

	number_of_solutions   = 500;
	number_of_generations = 1;
	number_of_runs        = 1;

	filepath             = "./interface/";

	run_id_pre            = "work_";
	run_id_post           = "th/";
	digits_of_run         = 3;

	variables_file_pre    = "gen";
	objectives_file_pre   = "gen";
	constraints_file_pre  = "gen";
	variables_file_post   = "_pop_vars_eval.txt";
	objectives_file_post  = "_pop_objs_eval.txt";
	constraints_file_post = "_pop_cons_eval.txt";
	digits_of_generation  = 4;

	number_of_objectives  = 5;
	number_of_variables   = 32;
	number_of_constraints = 22;

	header_of_output_file = "#Objective1, #Objective2, #Objective3, #Objective4, #Objective5, #Variable1, #Variable2, #Variable3, #Variable4, #Variable5, #Variable6, #Variable7, #Variable8, #Variable9, #Variable10, #Variable11, #Variable12, #Variable13, #Variable14, #Variable15, #Variable16, #Variable17, #Variable18, #Variable19, #Variable20, #Variable21, #Variable22, #Variable23, #Variable24, #Variable25, #Variable26, #Variable27, #Variable28, #Variable29, #Variable30, #Variable31, #Variable32, #Constraint1, #Constraint2, #Constraint3, #Constraint4, #Constraint5, #Constraint6, #Constraint7, #Constraint8, #Constraint9, #Constraint10, #Constraint11, #Constraint12, #Constraint13, #Constraint14, #Constraint15, #Constraint16, #Constraint17, #Constraint18, #Constraint19, #Constraint20, #Constraint21, #Constraint22, #OverallConstraintViolation";

	reference_point.resize(number_of_objectives, 0, 0);
	for (int i = 0; i < number_of_objectives; i++)
	{
		reference_point.objective(i) = 1.1;
	}
	

	max_point.resize(number_of_objectives, 0, 0);
	max_point.objective(0) =  0.00E0;
	max_point.objective(1) =  2.20E6;
	max_point.objective(2) =  1.60E8;
	max_point.objective(3) =  8.60E1;
	max_point.objective(4) =  0.00E0;

	min_point.resize(number_of_objectives, 0, 0);
	min_point.objective(0) = -2.20E7;
	min_point.objective(1) =  8.40E5;
	min_point.objective(2) =  1.60E7;
	min_point.objective(3) =  2.40E1;
	min_point.objective(4) = -2.20E0;
}

std::string ECSC::Configuration::input_filepath_of_objective(int nrun, int ngen)
{
	std::stringstream id_str;
	if (digits_of_run <= 0)
	{
		id_str << "";
	}
	else
	{
		id_str << std::setfill('0') << std::setw(digits_of_run) << std::right << nrun;
	}

	std::stringstream gen_str;
	if (digits_of_generation <= 0)
	{
		gen_str << "";
	}
	else
	{
		gen_str << std::setfill('0') << std::setw(digits_of_generation) << std::right << ngen;
	}

	return filepath + run_id_pre + id_str.str() + run_id_post + objectives_file_pre + gen_str.str() + objectives_file_post;
}

std::string ECSC::Configuration::input_filepath_of_variable(int nrun, int ngen)
{
	std::stringstream id_str;
	if (digits_of_run <= 0)
	{
		id_str << "";
	}
	else
	{
		id_str << std::setfill('0') << std::setw(digits_of_run) << std::right << nrun;
	}

	std::stringstream gen_str;
	if (digits_of_generation <= 0)
	{
		gen_str << "";
	}
	else
	{
		gen_str << std::setfill('0') << std::setw(digits_of_generation) << std::right << ngen;
	}

	return filepath + run_id_pre + id_str.str() + run_id_post + variables_file_pre + gen_str.str() + variables_file_post;
}

std::string ECSC::Configuration::input_filepath_of_constraint(int nrun, int ngen)
{
	std::stringstream id_str;
	if (digits_of_run <= 0)
	{
		id_str << "";
	}
	else
	{
		id_str << std::setfill('0') << std::setw(digits_of_run) << std::right << nrun;
	}

	std::stringstream gen_str;
	if (digits_of_generation <= 0)
	{
		gen_str << "";
	}
	else
	{
		gen_str << std::setfill('0') << std::setw(digits_of_generation) << std::right << ngen;
	}
	return filepath + run_id_pre + id_str.str() + run_id_post + constraints_file_pre + gen_str.str() + constraints_file_post;
}

std::string ECSC::Configuration::output_filepath_of_m1()
{
	return "mean_hv_value.txt";
}

std::string ECSC::Configuration::output_filepath_of_m2()
{
	return "m_prt_" + group_name + ".csv";
}

std::string ECSC::Configuration::output_filepath_of_m3()
{
	return "m_his_" + group_name + ".csv";
}

std::string ECSC::Configuration::output_filepath_of_m4()
{
	return "best_hv_value.txt";
}

std::string ECSC::Configuration::output_filepath_of_m5()
{
	return "m_bst_" + group_name + ".csv";
}
