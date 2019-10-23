/*
Postprocess Tool for Evolutionary Computation Symposium Competition 2019

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

#include "SolutionSet.h"
#include "Configuration.h"

#include <vector>
#include <algorithm>
#include <iostream>
#include <cmath>

namespace ECSC
{
	class HypervolumeCalculator
	{
	private:
		std::vector<SolutionSet> fs_;
		int currentDeep_;
		int currentDimension_;
		const int OPT = 2;

		int comporse_points(Solution& si, Solution& sj, const int n_obj, bool maximizing = true);
		bool is_better(const double& v1, const double& v2, bool maximizing = true);
		void sort_front(SolutionSet& pareto_front, bool maximizing = true);
		double calclate_2dhv(SolutionSet& pareto_front, const Solution& ref_point);
		double calclate_hv(SolutionSet& pareto_front, const Solution& ref_point);
		double get_execlusive_hv(SolutionSet& pareto_front, const int index, const Solution& ref_point);
		double get_inclusive_hv(Solution& p, const Solution& ref_point);
		void make_dominated_bit(SolutionSet& pareto_front, const int p);
		double worse(const double x, const double y, bool maximizing);
		int dominates_2way(const Solution& p, const Solution& q);
		int comporse_dominance(Solution & si, Solution & sj, const int n_obj);

		double hv_for_asolution(SolutionSet& solutions, const Solution& ref_point, const int n_obj);
		double hv_for_2solution(SolutionSet& solutions, const Solution& ref_point, const int n_obj);

		

	public:
		HypervolumeCalculator();

		~HypervolumeCalculator();

		bool execute(SolutionSet& pareto_front, double& hv, Configuration& config);
	};
}

