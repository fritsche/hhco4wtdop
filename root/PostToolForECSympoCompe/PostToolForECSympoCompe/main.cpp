/*
Postprocessing Tool for Evolutionary Computation Symposium Competition 2019

Author:
    Tatsumasa ISHIKAWA (tishikawa@flab.isas.jaxa.jp)

Copyright (c) 2019 Tatsumasa ISHIKAWA

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published
by the Free Software Foundation, either version 2 of the License.

This program is WITHOUT ANY WARRANTY.
See the GNU Lesser General Public License for more details.
You should have received a copy of the GNU Lesser General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


#include "ConfigurationLoader.h"
#include "Configuration.h"
#include "SolutionSetLoader.h"
#include "SolutionSet.h"
#include "InfeasibleSolutionRemover.h"
#include "ParetoSolutionSelector.h"
#include "HypervolumeCalculator.h"
#include "SolutionSetWriter.h"
#include "SolutionNormalizer.h"
#include "ResultsArchive.h"
#include "StatisticsCalculator.h"
#include "ResultsWriter.h"

#include <string>
#include <iostream>
#include <fstream>
#include <cstring>


int main(int argc, char *argv[])
{
	bool skip_infeasible_remover = false;
	bool skip_solution_normalizer = false;
	bool is_written_all_pareto_set = false;
	bool is_written_marge_pareto_set = false;
	std::string config_path = "config.json";
	int n_thread = 1;
	for (int i = 1; i < argc; i++)
	{
		if (!strcmp(argv[i], "-n"))
		{
			i++;
			n_thread = std::stoi(argv[i]);
		}
		else if (!strcmp(argv[i],"-skp-ir"))
		{
			skip_infeasible_remover = true;
		}
		else if (!strcmp(argv[i],"-skp-sn"))
		{
			skip_solution_normalizer = true;
		}
		else if (!strcmp(argv[i],"-cf"))
		{
			i++;
			config_path = argv[i];
		}
		else if (!strcmp(argv[i], "-waps")) // write all pareto set
		{
			is_written_all_pareto_set = true;
		}
		else if (!strcmp(argv[i], "-wmps")) // write marge pareto set
		{
			is_written_marge_pareto_set = true;
		}
		else
		{
			std::cout << "Unknown option " << argv[i] << std::endl;
			return -1;
		}
	}

	ECSC::Configuration config;
	ECSC::ConfigurationLoader cl;
	std::cout << "load configration file ..." << std::endl;
	if (cl.execute(config_path, config) == false)
	{
		std::cout << "can not load configration file " << config_path << "." << std::endl;
		std::cout << "done..." << std::endl;
		return -1;
	}

	ECSC::SolutionSet solutions;
	ECSC::SolutionSet unbounded_archive;
	ECSC::SolutionSet pareto;
	ECSC::SolutionSet pareto_by_archive;
	std::vector<ECSC::SolutionSet> pareto_fronts(config.number_of_runs);
	ECSC::ResultsArchive results_archive(config.number_of_runs, config.number_of_generations);

	ECSC::SolutionSetLoader solution_set_loader;
	ECSC::InfeasibleSolutionRemover infiasible_solution_remover;
	ECSC::ParetoSolutionSelector pareto_solution_selector;
	ECSC::SolutionNormalizer solution_normalizer;
	ECSC::HypervolumeCalculator hypervolume_calculator;

	unbounded_archive.resize_capacity(config.number_of_solutions * config.number_of_generations);
	unbounded_archive.set_solutions(config.number_of_objectives, config.number_of_variables, config.number_of_constraints);

	std::cout << "compute hypervolumes ..." << std::endl;

	double hv = 0;
	double hv_by_archive = 0;
	int run, gen;
	int return_value = 0;
#ifdef _OPENMP
#pragma omp parallel num_threads(n_thread)
#pragma omp for private(run, gen, solutions, pareto, unbounded_archive, hv, hv_by_archive, pareto_by_archive, solution_set_loader, infiasible_solution_remover, pareto_solution_selector, solution_normalizer, hypervolume_calculator)
#endif
	for (run = 0; run < config.number_of_runs; run++)
	{
		for (gen = 0; gen < config.number_of_generations; gen++)
		{
			if (solution_set_loader.execute(solutions, config, run, gen) == false)
			{
				std::cout << "done ..." << std::endl;
				return_value = -1;
			}

			if (skip_infeasible_remover == false)
			{
				if (infiasible_solution_remover.execute(solutions, config) == false)
				{
					std::cout << "done ..." << std::endl;
					return_value = -1;
				}
			}

			unbounded_archive.join(solutions);

			results_archive.number_of_feasibles(run, gen) = solutions.number_of_solutions();
		}

		if (pareto_solution_selector.execute(unbounded_archive, pareto_by_archive, config) == false)
		{
			std::cout << "done ..." << std::endl;
			return_value = -1;
		}

		pareto_fronts.at(run) = pareto_by_archive;

		if (skip_solution_normalizer == false)
		{
			if (solution_normalizer.execute(pareto_by_archive, config) == false)
			{
				std::cout << "done ..." << std::endl;
				return_value = -1;
			}
		}

		if (hypervolume_calculator.execute(pareto_by_archive, hv_by_archive, config) == false)
		{
			std::cout << "done ..." << std::endl;
			return_value = -1;
		}

		results_archive.archiving_hypervolume(run, config.number_of_generations - 1) = hv_by_archive;


		pareto_by_archive.destroy();
		solutions.destroy();
		unbounded_archive.resize_solution_set(0);
		
		std::cout << hv_by_archive << std::endl;
	}

	if (return_value == -1)
	{
		return -1;
	}

	int median_run = 0;
	int max_run = 0;
	double median_value;
	double max_value;
	ECSC::StatisticsCalculator statistics_calculator;
	if (statistics_calculator.execute(results_archive, median_run, max_run, median_value, max_value, config) == false)
	{
		std::cout << "done ..." << std::endl;
		return -1;
	}

	std::cout << "compute hypervolume of population in each generations (only median run and best run) ..." << std::endl;
	int index[2];
	index[0] = median_run;
	index[1] = max_run;
	int i = 0;
#ifdef _OPENMP
#pragma omp parallel num_threads(n_thread)
#pragma omp for private(i, run, gen, solutions, pareto, unbounded_archive, hv, hv_by_archive, pareto_by_archive, solution_set_loader, infiasible_solution_remover, pareto_solution_selector, solution_normalizer, hypervolume_calculator)
#endif
	for (i = 0; i < 2; i++)
	{
		run = index[i];
		for (gen = 0; gen < config.number_of_generations; gen++)
		{
			if (solution_set_loader.execute(solutions, config, run, gen) == false)
			{
				std::cout << "done ..." << std::endl;
				return_value = -1;
			}

			if (skip_infeasible_remover == false)
			{
				if (infiasible_solution_remover.execute(solutions, config) == false)
				{
					std::cout << "done ..." << std::endl;
					return_value = -1;
				}
			}

			unbounded_archive.join(solutions);

			if (pareto_solution_selector.execute(solutions, pareto, config) == false)
			{
				std::cout << "done ..." << std::endl;
				return_value = -1;
			}
			if (pareto_solution_selector.execute(unbounded_archive, pareto_by_archive, config) == false)
			{
				std::cout << "done ..." << std::endl;
				return_value = -1;
			}

			if (skip_solution_normalizer == false)
			{
				if (solution_normalizer.execute(pareto, config) == false)
				{
					std::cout << "done ..." << std::endl;
					return_value = -1;
				}
				if (solution_normalizer.execute(pareto_by_archive, config) == false)
				{
					std::cout << "done ..." << std::endl;
					return_value = -1;
				}
			}

			if (hypervolume_calculator.execute(pareto, hv, config, true) == false)
			{
				std::cout << "done ..." << std::endl;
				return_value = -1;
			}
			results_archive.hypervolume(run, gen) = hv;
			if (hypervolume_calculator.execute(pareto_by_archive, hv, config, true) == false)
			{
				std::cout << "done ..." << std::endl;
				return_value = -1;
			}
			results_archive.archiving_hypervolume(run, gen) = hv;

			solutions.destroy();
			pareto.destroy();
			pareto_by_archive.destroy();
		}

		unbounded_archive.resize_solution_set(0);

		std::cout << run << "th run is finished." << std::endl;
	}
	if (return_value == -1)
	{
		return -1;
	}


	ECSC::SolutionSetWriter solutions_writer;
	
	if (solutions_writer.execute(pareto_fronts.at(median_run), config.output_filepath_of_m2(), config) == false)
	{
		std::cout << "done ..." << std::endl;
		return -1;
	}
	if (is_written_all_pareto_set == true)
	{
		for (int i = 0; i < pareto_fronts.size(); i++)
		{
			std::string filename = "pareto_set_" + std::to_string(i) + "th_run.csv";
			if (solutions_writer.execute(pareto_fronts.at(i), filename, config) == false)
			{
				std::cout << "done ..." << std::endl;
				return -1;
			}
		}
	}
	if (is_written_marge_pareto_set == true)
	{
		ECSC::SolutionSet marge_ss;
		for (int i = 0; i < pareto_fronts.size(); i++)
		{
			marge_ss.join(pareto_fronts.at(i));
		}
		ECSC::SolutionSet marge_ps;
		if (pareto_solution_selector.execute(marge_ss, marge_ps, config) == false)
		{
			std::cout << "done ..." << std::endl;
			return_value = -1;
		}
		std::string filename = "marge_pareto_set.csv";
		if (solutions_writer.execute(marge_ps, filename, config) == false)
		{
			std::cout << "done ..." << std::endl;
			return -1;
		}
	}
	

	ECSC::ResultsWriter results_writer;
	if (results_writer.execute(results_archive, median_run, config.output_filepath_of_m3(), config) == false)
	{
		std::cout << "done ..." << std::endl;
		return -1;
	}
	if (results_writer.execute(results_archive, max_run, config.output_filepath_of_m5(), config) == false)
	{
		std::cout << "done ..." << std::endl;
		return -1;
	}

	auto n_gens = config.number_of_generations;
	std::ofstream ofs_m1(config.output_filepath_of_m1());
	ofs_m1 << std::to_string(median_value) << std::endl;
	ofs_m1.close();

	std::ofstream ofs_m4(config.output_filepath_of_m4());
	ofs_m4 << std::to_string(max_value) << std::endl;
	ofs_m4.close();

	std::cout << "median hypervolume value: "
		<< median_value
		<< " in " << std::to_string(median_run) << "th run"
		<< std::endl
		<< "best hypervolume value: "
		<< max_value
		<< " in " << std::to_string(max_run) << "th run"
		<< std::endl;
	std::cout << "post-processing is completed! done ..." << std::endl;

	return 0;
}
