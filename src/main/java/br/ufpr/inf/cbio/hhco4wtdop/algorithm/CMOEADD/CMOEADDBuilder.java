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
package br.ufpr.inf.cbio.hhco4wtdop.algorithm.CMOEADD;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmBuilder;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 * @param <S>
 */
public class CMOEADDBuilder<S extends Solution<?>> implements AlgorithmBuilder<CMOEADD<S>> {

    private final Problem<S> problem;
    private CrossoverOperator crossover;
    private MutationOperator mutation;
    private int populationSize;
    private int maxEvaluations;

    public CMOEADDBuilder(Problem<S> problem) {
        this.problem = problem;
    }

    @Override
    public CMOEADD<S> build() {
        return new CMOEADD(this);
    }

    public Problem getProblem() {
        return this.problem;
    }

    public CrossoverOperator getCrossover() {
        return crossover;
    }

    public CMOEADDBuilder setCrossover(CrossoverOperator crossover) {
        this.crossover = crossover;
        return this;
    }

    public MutationOperator getMutation() {
        return mutation;
    }

    public CMOEADDBuilder setMutation(MutationOperator mutation) {
        this.mutation = mutation;
        return this;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public CMOEADDBuilder setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
        return this;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public CMOEADDBuilder setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
        return this;
    }

}
