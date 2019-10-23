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

#include <vector>
#include <stdexcept>

namespace ECSC
{
	class Solution
	{
	private:
		std::vector<double> _objectives;
		std::vector<double> _variables;
		std::vector<double> _constraints;

		int _number_of_objectives;
		int _number_of_variables;
		int _number_of_constraints;

	public:
		Solution();

		Solution(int number_of_objectives, int number_of_variables, int number_of_constraints);

		~Solution();

		void destroy();

		void resize(int number_of_objectives, int number_of_variables, int number_of_constraints);

		double& objective(const int index) { return _objectives[index]; }
		const double& objective(const int index) const { return _objectives[index]; }

		double& variable(const int index) { return _variables[index]; }
		const double& variable(const int index) const { return _variables[index]; }

		double& constraint(const int index) { return _constraints[index]; }
		const double& constraint(const int index) const { return _constraints[index]; }

		int& get_number_of_objectives() { return _number_of_objectives; }
	};
}
