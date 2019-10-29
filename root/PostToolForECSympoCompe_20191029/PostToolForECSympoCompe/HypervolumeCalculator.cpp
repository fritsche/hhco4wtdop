#include "HypervolumeCalculator.h"


int ECSC::HypervolumeCalculator::comporse_points(Solution & si, Solution & sj, const int n_obj, bool maximizing)
{
	for (int i = n_obj - 1; i >= 0; i--)
	{
		if (is_better(si.objective(i), sj.objective(i), maximizing))
		{
			return -1;
		}
		if (is_better(sj.objective(i), si.objective(i), maximizing))
		{
			return 1;
		}
	}
	return 0;
}

bool ECSC::HypervolumeCalculator::is_better(const double & v1, const double & v2, bool maximizing)
{
	if (maximizing == true)
	{
		return (v1 > v2);
	}
	return (v2 > v1);
}

void ECSC::HypervolumeCalculator::sort_front(SolutionSet & pareto_front, bool maximizing)
{
	std::sort(pareto_front.begin(), pareto_front.end(), [this, maximizing](Solution  si, Solution  sj)
		{
			auto n_obj = si.get_number_of_objectives();
			return (comporse_points(si, sj, n_obj, maximizing) == -1);
		});
}

double ECSC::HypervolumeCalculator::calclate_2dhv(SolutionSet & pareto_front, const Solution& ref_point)
{
	double hv = std::abs( (pareto_front.at(0).objective(0) - ref_point.objective(0))
		    * (pareto_front.at(0).objective(1) - ref_point.objective(1)) );
	for (int i = 1; i < pareto_front.number_of_solutions(); i++)
	{
		hv += std::abs( (pareto_front.at(i).objective(0) - ref_point.objective(0))
			     * (pareto_front.at(i).objective(1) - pareto_front.at(i-1).objective(1)));
	}
	return hv;
}

double ECSC::HypervolumeCalculator::calclate_hv(SolutionSet & pareto_front, const Solution& ref_point)
{
	double volume = 0.0;
	if (pareto_front.number_of_solutions() > 1)
	{
		sort_front(pareto_front);
	}
	
	if (currentDimension_ == 2)
	{
		volume = calclate_2dhv(pareto_front, ref_point);
	}
	else
	{
		volume = 0.0;
		currentDimension_--;
		for (int i = pareto_front.number_of_solutions() - 1; i >= 0; i--)
		{
			volume += std::abs(pareto_front.at(i).objective(currentDimension_)
				- ref_point.objective(currentDimension_))
				* get_execlusive_hv(pareto_front, i, ref_point);
		}
		currentDimension_++;
	}

	return volume;
}

double ECSC::HypervolumeCalculator::get_execlusive_hv(SolutionSet & pareto_front, const int index, const Solution & ref_point)
{
	double volume;
	
	volume = get_inclusive_hv(pareto_front.at(index), ref_point);
	if (pareto_front.number_of_solutions() > index + 1)
	{
		make_dominated_bit(pareto_front, index);
		double v = calclate_hv(fs_[currentDeep_ - 1], ref_point);
		volume -= v;
		currentDeep_--;
	}

	return volume;
}


double ECSC::HypervolumeCalculator::get_inclusive_hv(Solution & p, const Solution & ref_point)
{
	double volume = 1.0;
	for (int i = 0; i < currentDimension_; i++)
	{
		volume *= std::abs(p.objective(i) - ref_point.objective(i));
	}
	return volume;
}

void ECSC::HypervolumeCalculator::make_dominated_bit(SolutionSet & pareto_front, const int p)
{
	int z = pareto_front.number_of_solutions() - 1 - p;
	auto& n_obj = pareto_front.at(0).get_number_of_objectives();

	fs_[currentDeep_].resize_solution_set(z);
	auto& sp = pareto_front.at(p);
	for (int i = 0; i < z; i++)
	{
		auto& sq = pareto_front.at(p + 1 + i);
		for (int j = 0; j < currentDimension_; j++)
		{
			fs_[currentDeep_].at(i).objective(j) = worse(sp.objective(j), sq.objective(j), false);
		}
	}

	Solution t;
	int n_point_in_fs_current_deep = 1;

	for (int i = 1; i < z; i++)
	{
		int j = 0;
		bool keep = true;

		auto& pi = fs_[currentDeep_].at(i);
		while (j < n_point_in_fs_current_deep && keep)
		{
			auto& pj = fs_[currentDeep_].at(j);
			auto dominance = dominates_2way(pi, pj);
			if (dominance == -1)
			{
				t = pj;
				n_point_in_fs_current_deep--;
				auto itr_pj = fs_[currentDeep_].begin() + j;
				auto itr_p_last = fs_[currentDeep_].begin() + n_point_in_fs_current_deep;
				*itr_pj = *itr_p_last;
				fs_[currentDeep_].add(n_point_in_fs_current_deep, t);
			}
			else if (dominance == 0)
			{
				j++;
			}
			else
			{
				keep = false;
			}
		}
		if (keep)
		{
			t = fs_[currentDeep_].at(n_point_in_fs_current_deep);
			auto itr_pi = fs_[currentDeep_].begin() + i;
			auto itr_p_last = fs_[currentDeep_].begin() + n_point_in_fs_current_deep;
			*itr_p_last = *itr_pi;
			fs_[currentDeep_].add(i, t);
			n_point_in_fs_current_deep++;
		}
	}
	
	fs_[currentDeep_].resize_solution_set(n_point_in_fs_current_deep);
	/*SolutionSet t_set(fs_[currentDeep_].capacity());
	for (int i = 0; i < n_point_in_fs_current_deep; i++)
	{
		t_set.add(fs_[currentDeep_].at(i));
	}
	fs_[currentDeep_] = t_set;*/

	currentDeep_++;
}

double ECSC::HypervolumeCalculator::worse(const double x, const double y, bool maximizing)
{
	double result = NAN;
	if (maximizing)
	{
		if (x > y)
		{
			result = y;
		}
		else
		{
			result = x;
		}
	}
	else
	{
		if (x > y)
		{
			result = x;
		}
		else
		{
			result = y;
		}
	}
	return result;
}

int ECSC::HypervolumeCalculator::dominates_2way(const Solution & p, const Solution & q)
{
	for (int i = currentDimension_ - 1; i >= 0; i--)
	{
		if (p.objective(i) < q.objective(i))
		{
			for (int j = i - 1; j >= 0; j--)
			{
				if (q.objective(j) < p.objective(j))
				{
					return 0;
				}
			}
			return -1;
		}
		else if (q.objective(i) < p.objective(i))
		{
			for (int j = i - 1; j >= 0; j--)
			{
				if (p.objective(j) < q.objective(j))
				{
					return 0;
				}
			}
			return 1;
		}
	}
	return 2;
}

int ECSC::HypervolumeCalculator::comporse_dominance(Solution & si, Solution & sj, const int n_obj)
{
	int flag = 0;
	bool dominate_i = false;
	bool dominate_j = false;
	for (int k = 0; k < n_obj; k++)
	{
		auto& value1 = si.objective(k);
		auto& value2 = sj.objective(k);

		if (value1 < value2) {
			flag = -1;
		}
		else if (value1 > value2) {
			flag = 1;
		}
		else {
			flag = 0;
		}

		if (flag == -1) {
			dominate_i = true;
		}

		if (flag == 1) {
			dominate_j = true;
		}
	}

	if (dominate_i == dominate_j) {
		return 0; //No one dominate the other
	}
	if (dominate_i == true) {
		return -1; // solution i dominate
	}
	return 1;    // solution j dominate 
}


double ECSC::HypervolumeCalculator::hv_for_asolution(SolutionSet& solutions, const Solution& ref_point, const int n_obj)
{
	auto& p = solutions.at(0);
	auto& r = ref_point;

	double volume = 1.0;
	for (int i = 0; i < n_obj; i++) {
		volume *= std::abs(p.objective(i) - r.objective(i));
	}
	return volume;
}

double ECSC::HypervolumeCalculator::hv_for_2solution(SolutionSet& solutions, const Solution& ref_point, const int n_obj)
{
	auto& p = solutions.at(0);
	auto& q = solutions.at(1);
	auto& r = ref_point;

	double vp = 1;
	double vq = 1;
	double vpq = 1;
	for (int i = 0; i < n_obj; i++) {
		vp *= std::abs(p.objective(i) - r.objective(i));
		vq *= std::abs(q.objective(i) - r.objective(i));
		vpq *= std::abs(worse(p.objective(i), q.objective(i), false) - r.objective(i));
	}
	return vp + vq - vpq;
}

double ECSC::HypervolumeCalculator::hv_for_3solution(SolutionSet & solutions, const Solution & ref_point, const int n_obj)
{
	auto& p = solutions.at(0);
	auto& q = solutions.at(1);
	auto& s = solutions.at(2);
	auto& r = ref_point;

	double vp = 1;
	double vq = 1;
	double vs = 1;
	double vpq = 1;
	double vps = 1;
	double vqs = 1;
	double vpqs = 1;

	for (int i = 0; i < n_obj; i++) {
		vp *= std::abs(p.objective(i) - r.objective(i));
		vq *= std::abs(q.objective(i) - r.objective(i));
		vs *= s.objective(i);
		if (p.objective(i) > q.objective(i)) {
			if (q.objective(i) > s.objective(i)) {
				vpq *= std::abs(q.objective(i) - r.objective(i));
				vps *= std::abs(s.objective(i) - r.objective(i));
				vqs *= std::abs(s.objective(i) - r.objective(i));
				vpqs *= std::abs(s.objective(i) - r.objective(i));
			}
			else {
				vpq *= std::abs(q.objective(i) - r.objective(i));
				vps *= std::abs(worse(p.objective(i), s.objective(i), false) - r.objective(i));
				vqs *= std::abs(q.objective(i) - r.objective(i));
				vpqs *= std::abs(q.objective(i) - r.objective(i));
			}
		}
		else if (p.objective(i) > s.objective(i)) {
			vpq *= std::abs(p.objective(i) - r.objective(i));
			vps *= std::abs(s.objective(i) - r.objective(i));
			vqs *= std::abs(s.objective(i) - r.objective(i));
			vpqs *= std::abs(s.objective(i) - r.objective(i));
		}
		else {
			vpq *= std::abs(p.objective(i) - r.objective(i));
			vps *= std::abs(p.objective(i) - r.objective(i));
			vqs *= std::abs(worse(q.objective(i), s.objective(i), false) - r.objective(i));
			vpqs *= std::abs(p.objective(i) - r.objective(i));
		}

	}
	return vp + vq + vs - vpq - vps - vqs + vpqs;

	return 0.0;
}


ECSC::HypervolumeCalculator::HypervolumeCalculator()
{
}

ECSC::HypervolumeCalculator::~HypervolumeCalculator()
{
}

bool ECSC::HypervolumeCalculator::execute(SolutionSet & pareto_front, double & hv, Configuration & config)
{
	Solution& ref_point = config.reference_point;
	int& n_obj = config.number_of_objectives;
	hv = 0.0;

	if (pareto_front.number_of_solutions() == 0)
	{
		hv = 0.0;
		return true;
	}

	if (ref_point.get_number_of_objectives() != n_obj)
	{
		std::cout << "HypervolumeCalculator: Error: Dimension of reference point is bad value." << std::endl;
		return false;
	}
	if (pareto_front.at(0).get_number_of_objectives() != n_obj)
	{
		std::cout << "HypervolumeCalculator: Error: Dimension of solution is bad value." << std::endl;
		return false;
	}

	SolutionSet non_nominated_by_ref_point(pareto_front.number_of_solutions());
	for (int i = 0; i < pareto_front.number_of_solutions(); i++)
	{
		if (comporse_dominance(pareto_front.at(i), ref_point, n_obj) == -1)
		{
			non_nominated_by_ref_point.add(pareto_front.at(i));
		}
	}

	if (non_nominated_by_ref_point.number_of_solutions() == 0)
	{
		hv = 0.0;
		return true;
	}
	else if (non_nominated_by_ref_point.number_of_solutions() == 1)
	{
		hv = hv_for_asolution(non_nominated_by_ref_point, ref_point, n_obj);
		return true;
	}
	else if (non_nominated_by_ref_point.number_of_solutions() == 2)
	{
		hv = hv_for_2solution(non_nominated_by_ref_point, ref_point, n_obj);
		return true;
	}
	else if (non_nominated_by_ref_point.number_of_solutions() == 3)
	{
		hv = hv_for_3solution(non_nominated_by_ref_point, ref_point, n_obj);
		return true;
	}

	fs_.resize(non_nominated_by_ref_point.number_of_solutions() - (OPT / 2 + 1));
	for (int i = 0; i < fs_.size(); i++)
	{
		fs_.at(i).resize_capacity(non_nominated_by_ref_point.number_of_solutions());
		fs_.at(i).set_solutions(n_obj, 0, 0);
	}
	currentDeep_ = 0;
	currentDimension_ = n_obj;

	hv = calclate_hv(pareto_front, ref_point);


	return true;
}
