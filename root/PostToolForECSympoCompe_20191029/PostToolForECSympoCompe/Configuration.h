/*
Postprocess
 Tool for Evolutionary Computation Symposium Competition 2019
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

#pragma once

#include "Solution.h"

#include <string>
#include <sstream>
#include <iomanip>
#include <iostream>

namespace ECSC
{
	/*
	Only supported CASE 2 in post script for JPNSEC Competition 2019.

	** in post_windturbine_rev01.r [H. Fukumoto, T. Kohira, and T. Tatsukawa 2019] **

	 CASE 2: result file for a run is divided by generation
	 example:
	  isSoleFile <- F
	  nrun <- 21
	  ngen <- 100
	  filepath <- "./interface/"
	  runidpre <- "work_"
	  runidpost <- "th/"
	  varsfilepre <- "gen"
	  varsfilepost <- "_pop_vars_eval.txt"
	  generationdig <- 4
	  runiddigit <- 3
	  then file name will be:
	    ./interface/work_000th/gen0000_pop_vars_eval.txt
	  ~ ./interface/work_000th/gen0099_pop_vars_eval.txt
	  ~ ./interface/work_020th/gen0000_pop_vars_eval.txt
	  ~ ./interface/work_020th/gen0099_pop_vars_eval.txt

	*/

	class Configuration
	{
	public:
		std::string group_name;
		int number_of_solutions;
		int number_of_generations;
		int number_of_runs;
		std::string filepath;
		std::string run_id_pre;
		std::string run_id_post;
		std::string variables_file_pre;
		std::string objectives_file_pre;
		std::string constraints_file_pre;
		std::string variables_file_post;
		std::string objectives_file_post;
		std::string constraints_file_post;
		std::string header_of_output_file;
		int digits_of_generation;
		int digits_of_run;
		int number_of_objectives;
		int number_of_variables;
		int number_of_constraints;

		Solution reference_point;
		Solution max_point;
		Solution min_point;

		Configuration();

		~Configuration();

		void initialize();

		std::string input_filepath_of_objective(int nrun, int ngen);
		std::string input_filepath_of_variable(int nrun, int ngen);
		std::string input_filepath_of_constraint(int nrun, int ngen);

		std::string output_filepath_of_m1();
		std::string output_filepath_of_m2();
		std::string output_filepath_of_m3();
		std::string output_filepath_of_m4();
		std::string output_filepath_of_m5();
	};
}
