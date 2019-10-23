#include "ResultsWriter.h"

ECSC::ResultsWriter::ResultsWriter()
{
}

ECSC::ResultsWriter::~ResultsWriter()
{
}

bool ECSC::ResultsWriter::execute(ResultsArchive & results_archive, const int& run, const std::string& path, Configuration & config)
{
	std::ofstream ofs(path);
	for (int i = 0; i < config.number_of_generations; i++)
	{
		auto& ar_hv = results_archive.archiving_hypervolume(run, i);
		auto& hv = results_archive.hypervolume(run, i);
		auto& n_f = results_archive.number_of_feasibles(run, i);
		ofs << std::to_string(i + 1) << ","
			<< std::to_string((i + 1) * config.number_of_solutions) << ","
			<< std::to_string(hv) << ","
			<< std::to_string(ar_hv) << ","
			<< std::to_string(n_f) << std::endl;
	}
	ofs.close();

	return true;
}
