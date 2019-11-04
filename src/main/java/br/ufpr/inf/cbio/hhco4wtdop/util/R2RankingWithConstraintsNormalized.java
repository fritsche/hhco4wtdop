/*
 * Copyright (C) 2019 Gian Fritsche <gmfritsche at inf.ufpr.br>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.ufpr.inf.cbio.hhco4wtdop.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.uma.jmetal.algorithm.multiobjective.mombi.util.AbstractUtilityFunctionsSet;
import org.uma.jmetal.algorithm.multiobjective.mombi.util.Normalizer;
import org.uma.jmetal.algorithm.multiobjective.mombi.util.R2Ranking;
import org.uma.jmetal.algorithm.multiobjective.mombi.util.R2RankingAttribute;
import org.uma.jmetal.algorithm.multiobjective.mombi.util.R2SolutionData;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 * @param <S>
 */
public class R2RankingWithConstraintsNormalized<S extends Solution<?>> extends R2Ranking<S> {

    private List<List<S>> rankedSubpopulations;
    private int numberOfRanks = 0;
    private final Normalizer normalizer;

    public R2RankingWithConstraintsNormalized(AbstractUtilityFunctionsSet<S> utilityFunctions, Normalizer normalizer) {
        super(utilityFunctions);
        this.normalizer = normalizer;
    }

    private double computeNorm(S solution) {
        List<Double> values = new ArrayList<>(solution.getNumberOfObjectives());
        for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
            if (normalizer == null) {
                values.add(solution.getObjective(i));
            } else {
                values.add(this.normalizer.normalize(solution.getObjective(i), i));
            }
        }

        double result = 0.0;
        result = values.stream().map((d) -> Math.pow(d, 2.0)).reduce(result, (accumulator, _item) -> accumulator + _item);

        return Math.sqrt(result);
    }

    @Override
    public R2RankingWithConstraintsNormalized<S> computeRanking(List<S> population) {
        population.forEach((solution) -> {
            R2SolutionData data = new R2SolutionData();
            data.utility = this.computeNorm(solution);
            solution.setAttribute(getAttributeIdentifier(), data);
        });

        for (int i = 0; i < this.getUtilityFunctions().getSize(); i++) {
            for (S solution : population) {
                R2SolutionData solutionData = this.getAttribute(solution);
                solutionData.alpha = this.getUtilityFunctions().evaluate(solution, i);
            }

            Collections.sort(population, (S o1, S o2) -> {
                R2RankingAttribute<S> attribute1 = new R2RankingAttribute<>();
                R2SolutionData data1 = (R2SolutionData) attribute1.getAttribute(o1);
                R2SolutionData data2 = (R2SolutionData) attribute1.getAttribute(o2);
                OverallConstraintViolation<DoubleSolution> violation = new OverallConstraintViolation();
                double v1 = violation.getAttribute((DoubleSolution) o1);
                double v2 = violation.getAttribute((DoubleSolution) o2);
                
                if ((v1 > v2) || ((v1 == v2) && (data1.alpha < data2.alpha))) {
                    return -1;
                } else if ((v2 > v1) || ((v2 == v1) && (data2.alpha < data1.alpha))) {
                    return 1;
                } else {
                    return 0;
                }
            });

            int rank = 1;
            for (S p : population) {
                R2SolutionData r2Data = this.getAttribute(p);
                if (rank < r2Data.rank) {
                    r2Data.rank = rank;
                    numberOfRanks = Math.max(numberOfRanks, rank);
                }
                rank = rank + 1;
            }
        }

        Map<Integer, List<S>> fronts = new TreeMap<>(); // sorted on key
        population.forEach((solution) -> {
            R2SolutionData r2Data = this.getAttribute(solution);
            if (fronts.get(r2Data.rank) == null) {
                fronts.put(r2Data.rank, new LinkedList<>());
            }
            fronts.get(r2Data.rank).add(solution);
        });

        this.rankedSubpopulations = new ArrayList<>(fronts.size());
        Iterator<Integer> iterator = fronts.keySet().iterator();
        while (iterator.hasNext()) {
            this.rankedSubpopulations.add(fronts.get(iterator.next()));
        }

        return this;
    }

    @Override
    public List<S> getSubfront(int rank) {
        return this.rankedSubpopulations.get(rank);
    }

    @Override
    public int getNumberOfSubfronts() {
        return this.rankedSubpopulations.size();
    }
}
