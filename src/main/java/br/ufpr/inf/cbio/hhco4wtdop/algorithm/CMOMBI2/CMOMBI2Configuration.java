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
package br.ufpr.inf.cbio.hhco4wtdop.algorithm.CMOMBI2;

import br.ufpr.inf.cbio.hhco.algorithm.MOMBI2.MOMBI2Configuration;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class CMOMBI2Configuration extends MOMBI2Configuration {

    @Override
    public CMOMBI2<?> configure(int popSize, int maxFitnessEvaluations, Problem problem) {
        setProblem(problem);
        setup();
        return new CMOMBI2<>(problem, maxFitnessEvaluations / popSize, getCrossover(), 
                getMutation(), getSelection(), new SequentialSolutionListEvaluator<>(), 
                "WeightVectorsMOMBI2/W" + problem.getNumberOfObjectives() + "D_" + popSize + ".dat");
    }
}
