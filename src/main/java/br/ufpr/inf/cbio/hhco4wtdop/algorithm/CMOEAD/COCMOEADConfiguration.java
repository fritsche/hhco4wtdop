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

import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEADBuilder;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.Problem;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class COCMOEADConfiguration extends CMOEADConfiguration {

    @Override
    public COCMOEAD configure(int popSize, int maxFitnessEvaluations, Problem problem) {
        setProblem((DoubleProblem) problem);

        setup();

        return (COCMOEAD) new COCMOEADBuilder(problem, MOEADBuilder.Variant.MOEAD)
                .setCrossover(getCrossover())
                .setMutation(getMutation())
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

}
