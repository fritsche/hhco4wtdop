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

import org.uma.jmetal.algorithm.multiobjective.moead.MOEADBuilder;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class CMOEADBuilder extends MOEADBuilder {

    public CMOEADBuilder(Problem<DoubleSolution> problem, Variant variant) {
        super(problem, variant);
    }

    @Override
    public CMOEAD build() {
        return new CMOEAD(problem, populationSize, resultPopulationSize, maxEvaluations, mutation,
                crossover, functionType, dataDirectory, neighborhoodSelectionProbability,
                maximumNumberOfReplacedSolutions, neighborSize);
    }
}
