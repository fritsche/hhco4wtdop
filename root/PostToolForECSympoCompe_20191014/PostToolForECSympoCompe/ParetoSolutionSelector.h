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

#include "Configuration.h"
#include "SolutionSet.h"
#include "Solution.h"

#include <vector>

namespace ECSC
{
	class ParetoSolutionSelector
	{
		int comporse_dominance(Solution& si, Solution& sj, const int n_obj);

	public:
		ParetoSolutionSelector();

		~ParetoSolutionSelector();

		bool execute(SolutionSet& solutions,SolutionSet& pareto, Configuration& config);
	};
}
