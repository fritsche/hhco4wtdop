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
package br.ufpr.inf.cbio.hhco4wtdop.algorithm.CMOEADSBX;

import br.ufpr.inf.cbio.hhco.config.AlgorithmConfiguration;
import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class CMOEADSBXConfiguration implements AlgorithmConfiguration<CMOEADSBX> {

    private MutationOperator<DoubleSolution> mutation;
    private CrossoverOperator<DoubleSolution> crossover;
    private DoubleProblem problem;

    @Override
    public CMOEADSBX configure(int popSize, int maxFitnessEvaluations, Problem problem) {
        this.problem = (DoubleProblem) problem;

        setup();

        return (CMOEADSBX) new CMOEADSBXBuilder(problem, CMOEADSBXBuilder.Variant.MOEAD)
                .setCrossover(crossover)
                .setMutation(mutation)
                .setMaxEvaluations(maxFitnessEvaluations)
                .setPopulationSize(popSize)
                .setResultPopulationSize(popSize)
                .setNeighborhoodSelectionProbability(0.9)
                .setMaximumNumberOfReplacedSolutions(2)
                .setNeighborSize(20)
                .setFunctionType(AbstractMOEAD.FunctionType.TCHE)
                .setDataDirectory("WeightVectors")
                .build();
    }

    @Override
    public void setup() {
       double crossoverProbability = 0.9;
        double crossoverDistributionIndex = 20.0;
        crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

        double mutationProbability = 1.0 / problem.getNumberOfVariables();
        double mutationDistributionIndex = 20.0;
        mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);
    }

    public MutationOperator<DoubleSolution> getMutation() {
        return mutation;
    }

    public void setMutation(MutationOperator<DoubleSolution> mutation) {
        this.mutation = mutation;
    }

    public CrossoverOperator getCrossover() {
        return crossover;
    }

    public void setCrossover(DifferentialEvolutionCrossover crossover) {
        this.crossover = crossover;
    }

    public DoubleProblem getProblem() {
        return problem;
    }

    public void setProblem(DoubleProblem problem) {
        this.problem = problem;
    }

}
