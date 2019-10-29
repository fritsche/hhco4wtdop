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

#include "Solution.h"

#include <vector>
#include <iostream>
#include <stdexcept>

namespace ECSC
{
	class SolutionSet
	{
	private:
		std::vector<Solution> _solution_set;
		int _capacity;
		int _number_of_solutions;


	public:
		SolutionSet();

		SolutionSet(int capacity);

		~SolutionSet();

		void destroy();

		void erase(int index);

		void resize_capacity(int capacity);

		void set_solutions(int n_obj, int n_var, int n_con);

		void set_solutions(int n, int n_obj, int n_var, int n_con);

		bool remove(int index);

		void resize_solution_set(int n) { _number_of_solutions = n; }

		bool add(int index, Solution solution);

		bool add(Solution solution);

		bool push_back(Solution solution);

		void join(SolutionSet solutions);

		int capacity() { return _capacity; }

		int number_of_solutions() { return _number_of_solutions; }

		std::vector<Solution>::iterator begin() { return _solution_set.begin(); }

		std::vector<Solution>::iterator end() { return (_solution_set.begin() + _number_of_solutions); }

		Solution& at(int index) 
		{
			if (index >= _number_of_solutions)
			{
				throw std::out_of_range("SolutionSet: Error: index is out of bound.");
			}
			return _solution_set.at(index); 
		}
		const Solution& at(int index) const 
		{ 
			if (index >= _number_of_solutions)
			{
				throw std::out_of_range("SolutionSet: Error: index is out of bound.");
			}
			return _solution_set.at(index); 
		}
	};
}