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

namespace ECSC
{
	class ResultsArchive
	{
	private:
		std::vector<std::vector<double>> _archiving_hypervolumes;

		std::vector<std::vector<double>> _hypervolumes;

		std::vector<std::vector<double>> _number_of_feasibles;

	public:
		ResultsArchive(const int n_run, const int n_gen);

		~ResultsArchive();

		void destroy();

		double& archiving_hypervolume(const int n_run, const int n_gen) { return _archiving_hypervolumes.at(n_gen).at(n_run); }
		const double& archiving_hypervolume (const int n_run, const int n_gen) const { return _archiving_hypervolumes.at(n_gen).at(n_run); }
		std::vector<double>& archiving_hypervolume(const int n_gen) { return _archiving_hypervolumes.at(n_gen); }
		const std::vector<double>& archiving_hypervolume(const int n_gen) const { return _archiving_hypervolumes.at(n_gen); }

		double& hypervolume(const int n_run, const int n_gen) { return _hypervolumes.at(n_gen).at(n_run); }
		const double& hypervolume(const int n_run, const int n_gen) const { return _hypervolumes.at(n_gen).at(n_run); }
		std::vector<double>& hypervolume(const int n_gen) { return _hypervolumes.at(n_gen); }
		const std::vector<double>& hypervolume(const int n_gen) const { return _hypervolumes.at(n_gen); }

		double& number_of_feasibles(const int n_run, const int n_gen) { return _number_of_feasibles.at(n_gen).at(n_run); }
		const double& number_of_feasibles(const int n_run, const int n_gen) const { return _number_of_feasibles.at(n_gen).at(n_run); }
		std::vector<double>& number_of_feasibles(const int n_gen) { return _number_of_feasibles.at(n_gen); }
		const std::vector<double>& number_of_feasibles(const int n_gen) const { return _number_of_feasibles.at(n_gen); }
	};
}

