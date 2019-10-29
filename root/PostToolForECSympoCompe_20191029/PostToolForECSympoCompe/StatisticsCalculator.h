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
#pragma once

#include "ResultsArchive.h"
#include "Configuration.h"

#include <algorithm>
#include <vector>

namespace ECSC
{
	class StatisticsCalculator
	{
	private:
		bool calc_median(std::vector<double> v, double& med, int& index);
		double med3(double x,  double y, double z);
		void quicksort(std::vector<double>&  a,std::vector<int>& index, int left, int right);
		bool calc_max(std::vector<double>& v, double& max, int& index);


	public:
		StatisticsCalculator();

		~StatisticsCalculator();

		bool execute(ResultsArchive& results_archive, int& median_run, int& maximum_run, double& median, double& max, Configuration& config);
	};
}
