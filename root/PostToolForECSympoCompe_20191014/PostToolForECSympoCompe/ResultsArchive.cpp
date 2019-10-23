#include "ResultsArchive.h"

ECSC::ResultsArchive::ResultsArchive(const int n_run, const int n_gen)
{
	_archiving_hypervolumes.resize(n_gen);
	for (int i = 0; i < _archiving_hypervolumes.size(); i++)
	{
		_archiving_hypervolumes.at(i).resize(n_run, 0);
	}

	_hypervolumes.resize(n_gen);
	for (int i = 0; i < _hypervolumes.size(); i++)
	{
		_hypervolumes.at(i).resize(n_run, 0);
	}

	_number_of_feasibles.resize(n_gen);
	for (int i = 0; i < _number_of_feasibles.size(); i++)
	{
		_number_of_feasibles.at(i).resize(n_run, 0);
	}
}

ECSC::ResultsArchive::~ResultsArchive()
{
	destroy();
}

void ECSC::ResultsArchive::destroy()
{
	std::vector<std::vector<double>>().swap(_archiving_hypervolumes);
	std::vector<std::vector<double>>().swap(_hypervolumes);
	std::vector<std::vector<double>>().swap(_number_of_feasibles);
}
