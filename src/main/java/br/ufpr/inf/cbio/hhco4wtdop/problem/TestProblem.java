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
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private int evaluationsCount;
    private int feasibleCount;

    public TestProblem() {
        setNumberOfVariables(32);
        setNumberOfObjectives(5);
        setNumberOfConstraints(10);
        setName("Test");

        List<Double> lowerLimit = new ArrayList<>(Collections.nCopies(100, 0.0));
        List<Double> upperLimit = new ArrayList<>(Collections.nCopies(100, 5.0));

        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);

        overallConstraintViolationDegree = new OverallConstraintViolation<>();
        numberOfViolatedConstraints = new NumberOfViolatedConstraints<>();

        evaluationsCount = 0;
        feasibleCount = 0;
    }

    @Override
    public void evaluate(DoubleSolution solution) {

        double[] fx = new double[solution.getNumberOfObjectives()];
        double[] x = new double[solution.getNumberOfVariables()];
        double[] xf = new double[solution.getNumberOfVariables()];

        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            x[i] = solution.getVariableValue(i);
            for (int j = 0; j < solution.getNumberOfObjectives(); j++) {
                fx[j] += Math.abs(x[i] - (double) j);
            }
        }
        for (int j = 0; j < solution.getNumberOfObjectives(); j++) {
            solution.setObjective(j, fx[j]);
        }

        evaluateConstraints(solution);
    }

    public void evaluateConstraints(DoubleSolution solution) {
        double[] constraint = new double[getNumberOfConstraints()];
        double[] x = new double[solution.getNumberOfVariables()];
        for (int i = 0; i < this.getNumberOfConstraints(); i++) {
            x[i] = solution.getVariableValue(i);
            constraint[i] = x[i] - Math.floor(x[i]) - (1.0 - (1.0 / ((double) i + 2.0)));
        }

        double overallConstraintViolation = 0.0;
        int violatedConstraints = 0;
        for (int i = 0; i < getNumberOfConstraints(); i++) {
            if (constraint[i] < 0.0) {
                overallConstraintViolation += constraint[i];
                violatedConstraints++;
            }
        }
        if (overallConstraintViolation == 0.0) {
            feasibleCount++;
        }
        evaluationsCount++;

        Logger.getLogger(WindTurbineDesign.class.getName()).log(Level.INFO, "{0}: {1}, {2}, {3}%",
                new Object[]{evaluationsCount, violatedConstraints, feasibleCount, feasibleCount / (double) evaluationsCount * 100});

        overallConstraintViolationDegree.setAttribute(solution, overallConstraintViolation);
        numberOfViolatedConstraints.setAttribute(solution, violatedConstraints);
    }
}
