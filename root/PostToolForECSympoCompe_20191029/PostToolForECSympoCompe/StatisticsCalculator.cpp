#include "StatisticsCalculator.h"

bool ECSC::StatisticsCalculator::calc_median(std::vector<double> v, double& med, int& index)
{
	/*size_t size = v.size();
	double *t = new double[size];
	std::copy(v.begin(), v.end(), t);
	std::sort(t, &t[size]);
	double result = size % 2 ? t[size / 2] : (t[(size / 2) - 1] + t[size / 2]) / 2;
	delete[] t;*/
	
	std::vector<int> indexs(v.size());
	for (int i = 0; i < v.size(); i++)
	{
		indexs[i] = i;
	}
	quicksort(v, indexs, 0, v.size() - 1);
	med = v.size() % 2 ? v[v.size() / 2] : (v[(v.size() / 2) - 1] + v[v.size() / 2]) / 2;

	return true;
}

double ECSC::StatisticsCalculator::med3(double x, double y, double z)
{
	if (x < y) {
		if (y < z) return y; else if (z < x) return x; else return z;
	}
	else {
		if (z < y) return y; else if (x < z) return x; else return z;
	}
}

void ECSC::StatisticsCalculator::quicksort(std::vector<double>& a, std::vector<int>& index, int left, int right)
{
	if (left < right) {
		int i = left, j = right;
		double tmp;
		double pivot = med3(a[i], a[i + (j - i) / 2], a[j]);
		int index_tmp;
		while (1) {
			while (a[i] < pivot) i++;
			while (pivot < a[j]) j--;
			if (i >= j) break;

			tmp = a[i];
			a[i] = a[j]; 
			a[j] = tmp;

			index_tmp = index[i];
			index[i] = index[j];
			index[j] = index_tmp;

			i++; 
			j--;
		}
		quicksort(a, index, left, i - 1);
		quicksort(a, index, j + 1, right);
	}
}

bool ECSC::StatisticsCalculator::calc_max(std::vector<double>& v, double& max, int& index)
{
	max = -1.0;
	index = -1;
	for (int i = 0; i < v.size(); i++)
	{
		if (max < v.at(i))
		{
			max = v.at(i);
			index = i;
		}
	}
	return max;
}

ECSC::StatisticsCalculator::StatisticsCalculator()
{
}

ECSC::StatisticsCalculator::~StatisticsCalculator()
{
}

bool ECSC::StatisticsCalculator::execute(ResultsArchive & results_archive, int & median_run, int & maximum_run, double& median, double& max, Configuration & config)
{
	auto& n_gen = config.number_of_generations;

	median = 0;
	calc_median(results_archive.archiving_hypervolume(n_gen - 1), median, median_run);
	if (median_run == -1)
	{
		return false;
	}

	max = 0.0;
	calc_max(results_archive.archiving_hypervolume(n_gen - 1), max, maximum_run);
	if (maximum_run == -1)
	{
		return false;
	}

	return true;
}
