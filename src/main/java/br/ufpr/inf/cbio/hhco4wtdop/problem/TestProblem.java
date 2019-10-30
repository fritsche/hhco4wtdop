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
package br.ufpr.inf.cbio.hhco4wtdop.problem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class TestProblem extends AbstractDoubleProblem {

    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints;

    public TestProblem() {
        setNumberOfVariables(100);
        setNumberOfObjectives(5);
        setNumberOfConstraints(1);
        setName("Test");

        List<Double> lowerLimit = new ArrayList<>(Collections.nCopies(100, 0.0));
        List<Double> upperLimit = new ArrayList<>(Collections.nCopies(100, 5.0));

        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);

        overallConstraintViolationDegree = new OverallConstraintViolation<>();
        numberOfViolatedConstraints = new NumberOfViolatedConstraints<>();
    }

    @Override
    public void evaluate(DoubleSolution solution) {

        double[] fx = new double[solution.getNumberOfObjectives()];
        double[] x = new double[solution.getNumberOfVariables()];
        double[] xf = new double[solution.getNumberOfVariables()];

        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            x[i] = solution.getVariableValue(i);
            xf[i] = Math.floor(x[i]);
            for (int j = 0; j < solution.getNumberOfObjectives(); j++) {
                fx[j] += (xf[i] == (double) j) ? (1.0) : (0.0);
            }
        }
        for (int j = 0; j < solution.getNumberOfObjectives(); j++) {
            solution.setObjective(j, fx[j]);
        }

        evaluateConstraints(solution);
    }

    public void evaluateConstraints(DoubleSolution solution) {
        double constraint = 0.0;
        double[] x = new double[solution.getNumberOfVariables()];
        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            x[i] = solution.getVariableValue(i);
            constraint += x[i] - Math.floor(x[i]) - 0.5;
        }

        double overallConstraintViolation = 0.0;
        int violatedConstraints = 0;
        if (constraint < 0.0) {
            overallConstraintViolation += constraint;
            violatedConstraints++;
        }

        overallConstraintViolationDegree.setAttribute(solution, overallConstraintViolation);
        numberOfViolatedConstraints.setAttribute(solution, violatedConstraints);
    }
}
