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
package br.ufpr.inf.cbio.hhco4wtdop.algorithm.CHHCO.utils;

import br.ufpr.inf.cbio.hhco.metrics.utilityfunction.UtilityFunction;
import java.util.List;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 * @param <S>
 */
public class R2WithConstraints<S extends Solution<?>> {

    private final Front referenceParetoFront;
    private final double[][] lambda;
    private final UtilityFunction function;
    private final double utilityFunctionWorseValue;
    private final OverallConstraintViolation<DoubleSolution> constraintViolation = new OverallConstraintViolation();

    public R2WithConstraints(double[][] lambda, Front referenceParetoFront, UtilityFunction function, double utilityFunctionWorseValue) {
        this.referenceParetoFront = referenceParetoFront;
        this.lambda = lambda;
        this.function = function;
        this.utilityFunctionWorseValue = utilityFunctionWorseValue;
    }

    private Front getNormalizedFront(Front front, double[] maximumValues, double[] minimumValues) {

        Front normalizedFront = new ArrayFront(front);
        int numberOfPointDimensions = front.getPoint(0).getDimension();

        for (int i = 0; i < front.getNumberOfPoints(); i++) {
            for (int j = 0; j < numberOfPointDimensions; j++) {
                if ((maximumValues[j] - minimumValues[j]) == 0) {
                    normalizedFront.getPoint(i).setValue(j, 0.0);
                } else {

                    normalizedFront.getPoint(i).setValue(j, (front.getPoint(i).getValue(j)
                            - minimumValues[j]) / (maximumValues[j] - minimumValues[j]));
                }
            }
        }
        return normalizedFront;
    }

    public double r2(List<S> population) {
        Front front = new ArrayFront(population);
        double[] minimumValues;
        double[] maximumValues;

        if (this.referenceParetoFront != null) {
            // STEP 1. Obtain the maximum and minimum values of the Pareto front
            maximumValues = FrontUtils.getMaximumValues(this.referenceParetoFront);
            minimumValues = FrontUtils.getMinimumValues(this.referenceParetoFront);

            front = getNormalizedFront(front, maximumValues, minimumValues);
        }

        int numberOfObjectives = front.getPoint(0).getDimension();

        // STEP 3. compute all the matrix of Tschebyscheff values if it is null
        double[][] matrix = new double[front.getNumberOfPoints()][lambda.length];
        for (int i = 0; i < front.getNumberOfPoints(); i++) {
            for (int j = 0; j < lambda.length; j++) {
                double constraint = constraintViolation.getAttribute((DoubleSolution) population.get(i));
                if (constraint < 0) {
                    matrix[i][j] = utilityFunctionWorseValue + Math.abs(constraint);
                } else {
                    matrix[i][j] = function.execute(lambda[j], front.getPoint(i), numberOfObjectives);
                }
            }
        }

        double sum = 0.0;
        for (int i = 0; i < lambda.length; i++) {
            double tmp = matrix[0][i];
            for (int j = 1; j < front.getNumberOfPoints(); j++) {
                tmp = Math.min(tmp, matrix[j][i]);
            }
            sum += tmp;
        }
        return sum / (double) lambda.length;
    }

}
