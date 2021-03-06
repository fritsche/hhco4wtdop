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

import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.ConstraintMOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEADD;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEADDRA;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEADSTM;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmBuilder;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class CMOEADSBXBuilder implements AlgorithmBuilder<AbstractMOEAD<DoubleSolution>> {

    public enum Variant {
        MOEAD, ConstraintMOEAD, MOEADDRA, MOEADSTM, MOEADD
    };

    protected Problem<DoubleSolution> problem;

    /**
     * T in Zhang & Li paper
     */
    protected int neighborSize;
    /**
     * Delta in Zhang & Li paper
     */
    protected double neighborhoodSelectionProbability;
    /**
     * nr in Zhang & Li paper
     */
    protected int maximumNumberOfReplacedSolutions;

    protected MOEAD.FunctionType functionType;

    protected CrossoverOperator<DoubleSolution> crossover;
    protected MutationOperator<DoubleSolution> mutation;
    protected String dataDirectory;

    protected int populationSize;
    protected int resultPopulationSize;

    protected int maxEvaluations;

    protected int numberOfThreads;

    protected Variant moeadVariant;

    /**
     * Constructor
     */
    public CMOEADSBXBuilder(Problem<DoubleSolution> problem, Variant variant) {
        this.problem = problem;
        populationSize = 300;
        resultPopulationSize = 300;
        maxEvaluations = 150000;
        crossover = new DifferentialEvolutionCrossover();
        mutation = new PolynomialMutation(1.0 / problem.getNumberOfVariables(), 20.0);
        functionType = MOEAD.FunctionType.TCHE;
        neighborhoodSelectionProbability = 0.1;
        maximumNumberOfReplacedSolutions = 2;
        dataDirectory = "";
        neighborSize = 20;
        numberOfThreads = 1;
        moeadVariant = variant;
    }

    /* Getters/Setters */
    public int getNeighborSize() {
        return neighborSize;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public int getResultPopulationSize() {
        return resultPopulationSize;
    }

    public String getDataDirectory() {
        return dataDirectory;
    }

    public MutationOperator<DoubleSolution> getMutation() {
        return mutation;
    }

    public CrossoverOperator<DoubleSolution> getCrossover() {
        return crossover;
    }

    public MOEAD.FunctionType getFunctionType() {
        return functionType;
    }

    public int getMaximumNumberOfReplacedSolutions() {
        return maximumNumberOfReplacedSolutions;
    }

    public double getNeighborhoodSelectionProbability() {
        return neighborhoodSelectionProbability;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public CMOEADSBXBuilder setPopulationSize(int populationSize) {
        this.populationSize = populationSize;

        return this;
    }

    public CMOEADSBXBuilder setResultPopulationSize(int resultPopulationSize) {
        this.resultPopulationSize = resultPopulationSize;

        return this;
    }

    public CMOEADSBXBuilder setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;

        return this;
    }

    public CMOEADSBXBuilder setNeighborSize(int neighborSize) {
        this.neighborSize = neighborSize;

        return this;
    }

    public CMOEADSBXBuilder setNeighborhoodSelectionProbability(double neighborhoodSelectionProbability) {
        this.neighborhoodSelectionProbability = neighborhoodSelectionProbability;

        return this;
    }

    public CMOEADSBXBuilder setFunctionType(MOEAD.FunctionType functionType) {
        this.functionType = functionType;

        return this;
    }

    public CMOEADSBXBuilder setMaximumNumberOfReplacedSolutions(int maximumNumberOfReplacedSolutions) {
        this.maximumNumberOfReplacedSolutions = maximumNumberOfReplacedSolutions;

        return this;
    }

    public CMOEADSBXBuilder setCrossover(CrossoverOperator<DoubleSolution> crossover) {
        this.crossover = crossover;

        return this;
    }

    public CMOEADSBXBuilder setMutation(MutationOperator<DoubleSolution> mutation) {
        this.mutation = mutation;

        return this;
    }

    public CMOEADSBXBuilder setDataDirectory(String dataDirectory) {
        this.dataDirectory = dataDirectory;

        return this;
    }

    public CMOEADSBXBuilder setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;

        return this;
    }

    public AbstractMOEAD<DoubleSolution> build() {
        AbstractMOEAD<DoubleSolution> algorithm = null;
        if (moeadVariant.equals(Variant.MOEAD)) {
            algorithm = new CMOEADSBX(problem, populationSize, resultPopulationSize, maxEvaluations, mutation,
                    crossover, functionType, dataDirectory, neighborhoodSelectionProbability,
                    maximumNumberOfReplacedSolutions, neighborSize);
        } else if (moeadVariant.equals(Variant.ConstraintMOEAD)) {
            algorithm = new ConstraintMOEAD(problem, populationSize, resultPopulationSize, maxEvaluations, mutation,
                    crossover, functionType, dataDirectory, neighborhoodSelectionProbability,
                    maximumNumberOfReplacedSolutions, neighborSize);
        } else if (moeadVariant.equals(Variant.MOEADDRA)) {
            algorithm = new MOEADDRA(problem, populationSize, resultPopulationSize, maxEvaluations, mutation,
                    crossover, functionType, dataDirectory, neighborhoodSelectionProbability,
                    maximumNumberOfReplacedSolutions, neighborSize);
        } else if (moeadVariant.equals(Variant.MOEADSTM)) {
            algorithm = new MOEADSTM(problem, populationSize, resultPopulationSize, maxEvaluations, mutation,
                    crossover, functionType, dataDirectory, neighborhoodSelectionProbability,
                    maximumNumberOfReplacedSolutions, neighborSize);
        } else if (moeadVariant.equals(Variant.MOEADD)) {
            algorithm = new MOEADD<>(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                    functionType, dataDirectory, neighborhoodSelectionProbability,
                    maximumNumberOfReplacedSolutions, neighborSize);
        }
        return algorithm;
    }
}
