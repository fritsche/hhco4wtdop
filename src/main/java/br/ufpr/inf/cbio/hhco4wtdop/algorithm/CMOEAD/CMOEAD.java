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
package br.ufpr.inf.cbio.hhco4wtdop.algorithm.CMOEAD;

import br.ufpr.inf.cbio.hhco4wtdop.util.ConstraintAndFitnessComparator;
import java.util.Comparator;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.solutionattribute.impl.Fitness;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class CMOEAD extends MOEAD {

    private Comparator<DoubleSolution> comparator;

    public CMOEAD(Comparator<DoubleSolution> comparator, Problem<DoubleSolution> problem, int populationSize, int resultPopulationSize, int maxEvaluations, MutationOperator<DoubleSolution> mutation, CrossoverOperator<DoubleSolution> crossover, FunctionType functionType, String dataDirectory, double neighborhoodSelectionProbability, int maximumNumberOfReplacedSolutions, int neighborSize) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, mutation, crossover, functionType, dataDirectory, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions, neighborSize);
        this.comparator = comparator;
    }

    public CMOEAD(Problem<DoubleSolution> problem, int populationSize, int resultPopulationSize, int maxEvaluations, MutationOperator<DoubleSolution> mutation, CrossoverOperator<DoubleSolution> crossover, FunctionType functionType, String dataDirectory, double neighborhoodSelectionProbability, int maximumNumberOfReplacedSolutions, int neighborSize) {
        this(new ConstraintAndFitnessComparator<DoubleSolution>(), problem, populationSize, resultPopulationSize, maxEvaluations, mutation, crossover, functionType, dataDirectory, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions, neighborSize);
    }

    @Override
    protected void updateNeighborhood(DoubleSolution individual, int subProblemId, NeighborType neighborType) throws JMetalException {
        int size;
        int time;

        time = 0;

        if (neighborType == NeighborType.NEIGHBOR) {
            size = neighborhood[subProblemId].length;
        } else {
            size = population.size();
        }
        int[] perm = new int[size];

        MOEADUtils.randomPermutation(perm, size);

        for (int i = 0; i < size; i++) {
            int k;
            if (neighborType == NeighborType.NEIGHBOR) {
                k = neighborhood[subProblemId][perm[i]];
            } else {
                k = perm[i];
            }

            fitnessFunction(population.get(k), lambda[k]);
            fitnessFunction(individual, lambda[k]);

            if (comparator.compare(individual, population.get(k)) < 0) {
                population.set(k, (DoubleSolution) individual.copy());
                time++;
            }

            if (time >= maximumNumberOfReplacedSolutions) {
                return;
            }
        }
    }

    public void fitnessFunction(DoubleSolution individual, double[] lambda) throws JMetalException {
        Fitness<DoubleSolution> fitnessAttribute = new Fitness<>();
        double fitness;

        if (null == functionType) {
            throw new JMetalException(" MOEAD.fitnessFunction: unknown type " + functionType);
        } else {
            switch (functionType) {
                case TCHE:
                    double maxFun = -1.0e+30;
                    for (int n = 0; n < problem.getNumberOfObjectives(); n++) {
                        double diff = Math.abs(individual.getObjective(n) - idealPoint.getValue(n));

                        double feval;
                        if (lambda[n] == 0) {
                            feval = 0.0001 * diff;
                        } else {
                            feval = diff * lambda[n];
                        }
                        if (feval > maxFun) {
                            maxFun = feval;
                        }
                    }
                    fitness = maxFun;
                    break;
                case AGG:
                    double sum = 0.0;
                    for (int n = 0; n < problem.getNumberOfObjectives(); n++) {
                        sum += (lambda[n]) * individual.getObjective(n);
                    }
                    fitness = sum;
                    break;
                case PBI:
                    double d1,
                     d2,
                     nl;
                    double theta = 5.0;
                    d1 = d2 = nl = 0.0;
                    for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
                        d1 += (individual.getObjective(i) - idealPoint.getValue(i)) * lambda[i];
                        nl += Math.pow(lambda[i], 2.0);
                    }
                    nl = Math.sqrt(nl);
                    d1 = Math.abs(d1) / nl;
                    for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
                        d2 += Math.pow((individual.getObjective(i) - idealPoint.getValue(i)) - d1 * (lambda[i] / nl), 2.0);
                    }
                    d2 = Math.sqrt(d2);
                    fitness = (d1 + theta * d2);
                    break;
                default:
                    throw new JMetalException(" MOEAD.fitnessFunction: unknown type " + functionType);
            }
        }
        fitnessAttribute.setAttribute(individual, fitness);
    }
    
}
