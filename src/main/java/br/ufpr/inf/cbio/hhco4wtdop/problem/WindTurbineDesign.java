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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class WindTurbineDesign extends AbstractDoubleProblem {

    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints;

    private final String algorithmName;
    private final String runId;
    private int fecount; // fitness evaluations count

    public WindTurbineDesign(String algorithmName, int runId) {
        this.algorithmName = algorithmName;
        this.runId = Integer.toString(runId);
        this.fecount = 0;
        setNumberOfVariables(32);
        setNumberOfObjectives(5);
        setNumberOfConstraints(22);
        setName("WindTurbineDesign");

        List<Double> lowerLimit = new ArrayList<>(Collections.nCopies(getNumberOfVariables(), 0.0));
        List<Double> upperLimit = new ArrayList<>(Collections.nCopies(getNumberOfVariables(), 1.0));

        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);

        overallConstraintViolationDegree = new OverallConstraintViolation<>();
        numberOfViolatedConstraints = new NumberOfViolatedConstraints<>();
    }

    @Override
    public void evaluate(DoubleSolution solution) {

        StringJoiner command = new StringJoiner(" ");
        command.add("bash root/eval_newsolutions.sh")
                .add(algorithmName)
                .add(runId)
                .add(Integer.toString(fecount++));

        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            command.add(Double.toString(solution.getVariableValue(i)));
        }

        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(command.toString());
            p.waitFor();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String inputLine;
                if ((inputLine = in.readLine()) != null) {
                    String[] objectives = inputLine.split("\t");
                    int index = 0;
                    for (String string : objectives) {
                        solution.setObjective(index++, Double.parseDouble(string));
                    }
                } else {
                    throw new JMetalException("Evaluation module failure! Check evaluation module logs!");
                }
                if ((inputLine = in.readLine()) != null) {
                    double[] constraint = Arrays.stream(inputLine.split("\t"))
                            .mapToDouble(Double::parseDouble)
                            .toArray();

                    double overallConstraintViolation = 0.0;
                    int violatedConstraints = 0;
                    for (int i = 0; i < getNumberOfConstraints(); i++) {
                        if (constraint[i] < 0.0) {
                            overallConstraintViolation += constraint[i];
                            violatedConstraints++;
                        }
                    }

                    Logger.getLogger(WindTurbineDesign.class.getName()).log(Level.INFO, "{0}: {1}\t{2}",
                            new Object[]{fecount - 1, Arrays.toString(solution.getObjectives()), overallConstraintViolation});

                    overallConstraintViolationDegree.setAttribute(solution, overallConstraintViolation);
                    numberOfViolatedConstraints.setAttribute(solution, violatedConstraints);
                } else {
                    throw new JMetalException("Evaluation module failure! Check evaluation module logs!");
                }
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(WindTurbineDesign.class.getName()).log(Level.SEVERE, "Could not execute eval_solutions.sh bash script!", ex);
        }
    }

}
